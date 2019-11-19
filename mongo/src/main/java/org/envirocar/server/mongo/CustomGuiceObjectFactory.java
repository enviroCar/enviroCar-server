/*
 * Copyright (C) 2013-2019 The enviroCar project
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
package org.envirocar.server.mongo;

import com.google.inject.Injector;
import com.mongodb.DBObject;
import dev.morphia.ext.guice.GuiceObjectFactory;
import dev.morphia.mapping.MapperOptions;

import java.util.Objects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class CustomGuiceObjectFactory extends GuiceObjectFactory {
    private MapperOptions options;

    public CustomGuiceObjectFactory(MapperOptions options, Injector injector) {
        super(options.getObjectFactory(), injector);
        this.options = Objects.requireNonNull(options);
    }

    @Override
    public <T> T createInstance(Class<T> clazz, DBObject dbObj) {
        String className = (String) dbObj.get(options.getDiscriminatorField());
        if (className != null) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends T> subclass = (Class<? extends T>) Class.forName(className);
                return super.createInstance(subclass, dbObj);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            return super.createInstance(clazz, dbObj);
        }
    }
}
