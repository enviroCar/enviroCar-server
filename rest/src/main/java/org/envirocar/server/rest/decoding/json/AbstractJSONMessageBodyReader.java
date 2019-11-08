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
package org.envirocar.server.rest.decoding.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.inject.Inject;
import org.envirocar.server.core.exception.BadRequestException;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public abstract class AbstractJSONMessageBodyReader<T> implements MessageBodyReader<T> {
    @Inject
    private ObjectReader reader;
    @Inject
    private JsonNodeFactory factory;
    private final Class<T> classType;

    public AbstractJSONMessageBodyReader(Class<T> classType) {
        this.classType = classType;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return this.classType.isAssignableFrom(type) && mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public T readFrom(Class<T> c, Type gt, Annotation[] a, MediaType mt,
                      MultivaluedMap<String, String> h,
                      InputStream in) throws IOException, WebApplicationException {
        try {
            return decode(reader.readTree(in), mt);
        } catch (JsonParseException e) {
            throw new BadRequestException(e);
        }
    }

    public T decode(JsonNode node, MediaType mediaType, ContextKnowledge knowledge) {
        return decode(node, mediaType);
    }

    public abstract T decode(JsonNode node, MediaType mediaType);
}
