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
package org.envirocar.server.rest.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.locationtech.jts.geom.Geometry;
import org.envirocar.server.core.entities.*;
import org.envirocar.server.rest.UserReference;
import org.envirocar.server.rest.decoding.json.*;
import org.envirocar.server.rest.decoding.UserReferenceDecoder;
import org.envirocar.server.rest.entity.ResetPasswordRequest;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyDecoderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JsonNodeMessageBodyReader.class);
        bind(ContextKnowledgeFactory.class);

        bind(new TypeLiteral<JSONEntityDecoder<User>>() {}).to(UserDecoder.class);
        bind(new TypeLiteral<JSONEntityDecoder<UserReference>>() {}).to(UserReferenceDecoder.class);
        bind(new TypeLiteral<JSONEntityDecoder<Phenomenon>>() {}).to(PhenomenonDecoder.class);
        bind(new TypeLiteral<JSONEntityDecoder<Group>>() {}).to(GroupDecoder.class);
        bind(new TypeLiteral<JSONEntityDecoder<Geometry>>() {}).to(GeoJSONDecoder.class);
        bind(new TypeLiteral<JSONEntityDecoder<Measurement>>() {}).to(MeasurementDecoder.class);
        bind(new TypeLiteral<JSONEntityDecoder<Track>>() {}).to(TrackDecoder.class);
        bind(new TypeLiteral<JSONEntityDecoder<Sensor>>() {}).to(SensorDecoder.class);
        bind(new TypeLiteral<JSONEntityDecoder<ResetPasswordRequest>>() {}).to(ResetPasswordDecoder.class);
        bind(new TypeLiteral<JSONEntityDecoder<Fueling>>() {}).to(FuelingDecoder.class);

        bind(UserDecoder.class);
        bind(UserReferenceDecoder.class);
        bind(PhenomenonDecoder.class);
        bind(GroupDecoder.class);
        bind(GeoJSONDecoder.class);
        bind(MeasurementDecoder.class);
        bind(TrackDecoder.class);
        bind(SensorDecoder.class);
        bind(ResetPasswordDecoder.class);
        bind(FuelingDecoder.class);

    }
}
