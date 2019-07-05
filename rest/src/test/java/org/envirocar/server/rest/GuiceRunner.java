/*
 * Copyright (C) 2013-2018 The enviroCar project
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
package org.envirocar.server.rest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

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
            Object instance = injector.getInstance(getTestClass().getJavaClass());
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

    private List<Module> getModules(Class<?> clazz) throws InitializationError {
        Set<Class<? extends Module>> moduleClasses = new HashSet<>();
        List<FrameworkField> annotatedFields = getTestClass().getAnnotatedFields(Rule.class);
        for (FrameworkField frameworkField : annotatedFields) {
            moduleClasses.addAll(getModulesFromAnnotation(frameworkField.getField().getType()));
        }
        moduleClasses.addAll(getModulesFromAnnotation(clazz));

        List<Module> modules = new ArrayList<>(moduleClasses.size());
        for (Class<? extends Module> moduleClass : moduleClasses) {
            modules.add(instantiate(moduleClass));
        }
        return modules;
    }

    private Collection<Class<? extends Module>> getModulesFromAnnotation(Class<?> clazz) {
        Modules annotation = clazz.getAnnotation(Modules.class);
        if (annotation != null) {
            return Arrays.asList(annotation.value());
        } else {
            return Collections.emptyList();
        }
    }

    private static <T> T instantiate(Class<? extends T> c) throws InitializationError {
        int modifiers = c.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers) || !Modifier.isPublic(modifiers)) {
            throw new InitializationError(String.format("Class %s is not a public class", c));
        }
        Constructor<? extends T> defConstructor = getDefaultConstructor(c);
        if (defConstructor == null) {
            throw new InitializationError(String.format("Class %s has no zero-argument constructor", c));
        }
        if (!Modifier.isPublic(defConstructor.getModifiers())) {
            throw new InitializationError(String.format("Zero-argument constructor of class %s is not public", c));
        }
        try {
            return defConstructor.newInstance();
        } catch (InstantiationException | SecurityException | InvocationTargetException | IllegalArgumentException | IllegalAccessException ex) {
            throw new InitializationError(ex);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> getDefaultConstructor(Class<T> c) {
        Constructor<T> defConstructor = null;
        for (Constructor<?> constructor : c.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                defConstructor = (Constructor<T>) constructor;
                break;
            }
        }
        return defConstructor;
    }
}
