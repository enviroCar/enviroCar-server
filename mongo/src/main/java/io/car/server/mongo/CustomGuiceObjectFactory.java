/*
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.mongo;

import com.github.jmkgreen.morphia.ObjectFactory;
import com.github.jmkgreen.morphia.ext.guice.GuiceObjectFactory;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.inject.Injector;
import com.mongodb.DBObject;

/**
 * TODO JavaDoc
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class CustomGuiceObjectFactory extends GuiceObjectFactory {
    public CustomGuiceObjectFactory(ObjectFactory delegate, Injector injector) {
        super(delegate, injector);
    }

    @Override
    public Object createInstance(Class clazz, DBObject dbObj) {
        String className = (String) dbObj.get(Mapper.CLASS_NAME_FIELDNAME);
        if (className != null) {
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        return super.createInstance(clazz, dbObj);
    }
}
