/*
 * Copyright (C) 2013 The enviroCar project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.envirocar.server.rest.schema;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.InitializationError;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GuiceRunner extends BlockJUnit4ClassRunner {
    private final Injector injector;

    public GuiceRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        this.injector = createInjector(clazz);
    }

    @Override
    protected Object createTest() throws Exception {
        if (injector != null) {
            Object instance =
                    injector.getInstance(getTestClass().getJavaClass());
            List<TestRule> testRules = getTestRules(instance);
            for (TestRule rule : testRules) {
                injector.injectMembers(rule);
            }
            return instance;
        } else {
            return super.createTest();
        }
    }

    private Injector createInjector(Class<?> clazz) throws InitializationError {
        List<Module> modules = getModules(clazz);
        if (!modules.isEmpty()) {
            return Guice.createInjector(Stage.DEVELOPMENT, modules);
        } else {
            return null;
        }
    }

    protected List<Module> getModules(Class<?> clazz) throws InitializationError {
        List<Module> modules = Lists.newLinkedList();
        List<FrameworkField> annotatedFields =
                getTestClass().getAnnotatedFields(Rule.class);
        for (FrameworkField frameworkField : annotatedFields) {
            modules.addAll(getModulesFromAnnotation(frameworkField.getField()
                    .getType()));
        }
        modules.addAll(getModulesFromAnnotation(clazz));
        return modules;
    }

    protected List<Module> getModulesFromAnnotation(Class<?> clazz) throws
            InitializationError {
        Modules annotation = clazz.getAnnotation(Modules.class);
        if (annotation != null) {
            Class<? extends Module>[] classes = annotation.value();
            List<Module> modules = Lists
                    .newArrayListWithExpectedSize(classes.length);
            for (Class<? extends Module> c : classes) {
                modules.add(instantiate(c));
            }
            return modules;
        } else {
            return Collections.emptyList();
        }
    }

    protected Module instantiate(Class<? extends Module> c) throws
            InitializationError {
        int modifiers = c.getModifiers();
        if (Modifier.isAbstract(modifiers) ||
            Modifier.isInterface(modifiers) ||
            !Modifier.isPublic(modifiers)) {
            throw new InitializationError(String
                    .format("Module %s is not a public class", c));
        }
        Constructor<?> defConstructor = null;
        for (Constructor<?> constructor : c.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                defConstructor = constructor;
                break;
            }
        }
        if (defConstructor == null) {
            throw new InitializationError(String
                    .format("Module %s has no zero-argument constructor", c));
        }
        if (!Modifier.isPublic(defConstructor.getModifiers())) {
            throw new InitializationError(String
                    .format("Zero-argument constructor of Module %s is not public", c));
        }
        try {

            return (Module) defConstructor.newInstance();
        } catch (InstantiationException ex) {
            throw new InitializationError(ex);
        } catch (IllegalAccessException ex) {
            throw new InitializationError(ex);
        } catch (IllegalArgumentException ex) {
            throw new InitializationError(ex);
        } catch (InvocationTargetException ex) {
            throw new InitializationError(ex);
        } catch (SecurityException ex) {
            throw new InitializationError(ex);
        }
    }
}
