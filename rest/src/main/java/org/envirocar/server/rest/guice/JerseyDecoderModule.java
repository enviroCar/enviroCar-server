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
package org.envirocar.server.rest.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.vividsolutions.jts.geom.Geometry;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.rest.decoding.json.*;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyDecoderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JsonNodeMessageBodyReader.class).in(Scopes.SINGLETON);
        bind(PhenomenonDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<Phenomenon>>() {
        }).to(PhenomenonDecoder.class);
        bind(GeoJSONDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<Geometry>>() {
        }).to(GeoJSONDecoder.class);
        bind(MeasurementDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<Measurement>>() {
        }).to(MeasurementDecoder.class);
        bind(TrackDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<Track>>() {
        }).to(TrackDecoder.class);
        bind(SensorDecoder.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<JSONEntityDecoder<Sensor>>() {
        }).to(SensorDecoder.class);
        bind(ContextKnowledgeFactory.class).toInstance(new ContextKnowledgeFactory());
    }
}
