/**
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
package io.car.server.rest.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;


/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstracJsonProvider<T> extends AbstractMessageReaderWriterProvider<T> {
    @Inject
    private ObjectReader reader;
    @Inject
    private ObjectWriter writer;
    private final Class<T> classType;

    public AbstracJsonProvider(Class<T> classType) {
        this.classType = classType;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return this.classType.isAssignableFrom(type) && mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return this.classType.isAssignableFrom(type) && mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public T readFrom(Class<T> c, Type gt, Annotation[] a, MediaType mt, MultivaluedMap<String, String> h,
                      InputStream in) throws IOException, WebApplicationException {
        return read(reader.readTree(in), mt);
    }

    @Override
    public void writeTo(T t, Class<?> c, Type gt, Annotation[] a, MediaType mt, MultivaluedMap<String, Object> h,
                        OutputStream out) throws IOException, WebApplicationException {
        writer.writeValue(out, write(t, mt));
        out.flush();
    }

    public abstract T read(JsonNode j, MediaType mediaType);

    public abstract JsonNode write(T t, MediaType mediaType);
}
