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
package io.car.server.mongo.dao;

import java.util.concurrent.ExecutionException;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;

import io.car.server.core.dao.SensorDao;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Sensors;
import io.car.server.mongo.entity.MongoSensor;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoSensorDao extends BasicDAO<MongoSensor, ObjectId> implements
        SensorDao {
    private LoadingCache<String, MongoSensor> cache = CacheBuilder.newBuilder()
            .maximumSize(1000).build(new CacheLoader<String, MongoSensor>() {
        @Override
        public MongoSensor load(String key) throws Exception {
            MongoSensor sensor = createQuery()
                    .field(MongoSensor.NAME)
                    .equal(key)
                    .get();
            if (sensor == null) {
                throw new SensorNotFoundException();
            }
            return sensor;
        }
    });
    @Inject
    public MongoSensorDao(Datastore ds) {
        super(MongoSensor.class, ds);
    }

    @Override
    public Sensor getByName(final String name) {
        try {
            return cache.get(name);
        } catch (ExecutionException ex) {
            if (ex.getCause() instanceof SensorNotFoundException) {
                return null;
            }
            throw new RuntimeException();
        }

    }

    @Override
    public Sensors get() {
        return Sensors.from(find().fetch()).build();
    }

    @Override
    public Sensor create(Sensor sensor) {
        MongoSensor ms = (MongoSensor) sensor;
        save(ms);
        return ms;
    }

    private class SensorNotFoundException extends Exception {
        private static final long serialVersionUID = -2361992444282158843L;
    }
}
