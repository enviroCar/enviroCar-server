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

import static com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider.getCharset;
import static com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider.readFromAsString;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;


/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public abstract class AbstracJsonProvider<T> extends AbstractMessageReaderWriterProvider<T> {
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
        try {
            return read(new JSONObject(readFromAsString(in, mt)), mt);
        } catch (JSONException ex) {
            throw new WebApplicationException(ex, Status.BAD_REQUEST);
        }
    }

    @Override
    public void writeTo(T t, Class<?> c, Type gt, Annotation[] a, MediaType mt, MultivaluedMap<String, Object> h,
                        OutputStream out) throws IOException, WebApplicationException {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(out, getCharset(mt));
            write(t, mt).write(writer);
            writer.flush();
        } catch (JSONException ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
    }

    public abstract T read(JSONObject j, MediaType mediaType) throws JSONException;
    public abstract JSONObject write(T t, MediaType mediaType) throws JSONException;
}
