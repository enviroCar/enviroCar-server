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
package org.envirocar.server.rest.encoding;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.vividsolutions.jts.geom.Geometry;
import org.bson.BSONObject;
import org.envirocar.server.core.*;
import org.envirocar.server.core.dao.*;
import org.envirocar.server.core.entities.*;
import org.envirocar.server.core.util.*;
import org.envirocar.server.mongo.dao.*;
import org.envirocar.server.mongo.entity.*;
import org.envirocar.server.mongo.util.GeoBSON;
import org.envirocar.server.rest.decoding.json.GeoJSONDecoder;
import org.envirocar.server.rest.decoding.json.JSONEntityDecoder;
import org.envirocar.server.rest.decoding.json.MeasurementDecoder;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.mongodb.morphia.converters.TypeConverter;

public class EncodingTestModule extends AbstractModule {

	@Override
	protected void configure() {
        bind(DataService.class).to(DataServiceImpl.class);
        bind(DataService.class).to(DataServiceImpl.class);
        bind(StatisticsService.class).to(StatisticsServiceImpl.class);
        bind(PasswordEncoder.class).to(BCryptPasswordEncoder.class);
        bind(GeometryOperations.class).to(GeodesicGeometryOperations.class);
        Multibinder.newSetBinder(binder(), TypeConverter.class);
        bind(new TypeLiteral<GeometryConverter<BSONObject>>() {
        }).to(GeoBSON.class);
        bind(new TypeLiteral<JSONEntityDecoder<Measurement>>() {
        }).to(MeasurementDecoder.class);
        bind(new TypeLiteral<JSONEntityDecoder<Geometry>>() {
        }).to(GeoJSONDecoder.class);
        install(new FactoryModuleBuilder()
        .implement(Track.class, MongoTrack.class)
        .implement(Measurement.class, MongoMeasurement.class)
        .implement(MeasurementValue.class, MongoMeasurementValue.class)
        .implement(Phenomenon.class, MongoPhenomenon.class)
        .implement(Sensor.class, MongoSensor.class)
        .build(EntityFactory.class));
        bind(TrackDao.class).to(MongoTrackDao.class);
        bind(MeasurementDao.class).to(MongoMeasurementDao.class);
        bind(SensorDao.class).to(MongoSensorDao.class);
        bind(StatisticsDao.class).to(MongoStatisticsDao.class);
        bind(PhenomenonDao.class).to(MongoPhenomenonDao.class);
        bind(TermsOfUseDao.class).to(MongoTermsOfUseDao.class);
        bind(CarSimilarityService.class).to(CarSimilarityServiceImpl.class);
	}


    @Provides
    @Singleton
    public DateTimeFormatter formatter() {
        return ISODateTimeFormat.dateTimeNoMillis();
    }
	
}
