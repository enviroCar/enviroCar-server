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
package org.envirocar.server.rest.guice;

import org.envirocar.server.core.util.GeometryConverter;
import org.envirocar.server.rest.util.GeoJSON;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyCodingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GeoJSON.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<GeometryConverter<JsonNode>>() {
        }).to(GeoJSON.class);
    }

    @Provides
    @Singleton
    public JsonNodeFactory jsonNodeFactory() {
        return JsonNodeFactory.withExactBigDecimals(false);
    }

    @Provides
    @Singleton
    public ObjectReader objectReader(ObjectMapper mapper) {
        return mapper.reader();
    }

    @Provides
    @Singleton
    public ObjectWriter objectWriter(ObjectMapper mapper) {
        return mapper.writer();
    }

    @Provides
    @Singleton
    public ObjectMapper objectMapper(JsonNodeFactory factory) {
        return new ObjectMapper().setNodeFactory(factory)
                .disable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }
}
