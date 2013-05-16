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
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;

import io.car.server.core.exception.ValidationException;
import io.car.server.rest.validation.JSONValidationException;
import io.car.server.rest.validation.Validator;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public abstract class AbstracJsonProvider<T> extends AbstractMessageReaderWriterProvider<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstracJsonProvider.class);
    private final Class<T> classType;
    private final Set<MediaType> readableMediaTypes;
    private final Set<MediaType> writableMediaTypes;

    @Inject
    private Validator<JSONObject> validator;

    public AbstracJsonProvider(Class<T> classType, Set<MediaType> readableMediaTypes, Set<MediaType> writableMediaTypes) {
        this.classType = classType;
        this.readableMediaTypes = readableMediaTypes;
        this.writableMediaTypes = writableMediaTypes;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return this.classType.isAssignableFrom(type) && isCompatible(this.readableMediaTypes, mediaType);
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return this.classType.isAssignableFrom(type) && isCompatible(this.writableMediaTypes, mediaType);
    }

    @Override
    public T readFrom(Class<T> c, Type gt, Annotation[] a, MediaType mt, MultivaluedMap<String, String> h,
                      InputStream in) throws IOException, WebApplicationException {
        try {
            JSONObject j = new JSONObject(readFromAsString(in, mt));
            validator.validate(j, mt);
            return read(j, mt);
        } catch (JSONException ex) {
            throw new WebApplicationException(ex, Status.BAD_REQUEST);
        }
    }

    @Override
    public void writeTo(T t, Class<?> c, Type gt, Annotation[] a, MediaType mt, MultivaluedMap<String, Object> h,
                        OutputStream out) throws IOException, WebApplicationException {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(out, getCharset(mt));
            JSONObject j = write(t, mt);
            try {
                validator.validate(j, mt);
            } catch (JSONValidationException v) {
                log.error("Created invalid response: Error:\n" + v.getError().toString(4) +
                          "\nGenerated Response:\n" + j.toString(4) + "\n", v);
                ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
                // possible bug in jersey: the response does not inherit the
                // content-encoding header when transferred as gzip, so copy the headers
                for (Entry<String, List<Object>> headers : h.entrySet()) {
                    for (Object header : headers.getValue()) {
                        builder.header(headers.getKey(), header);
                    }
                }
                Response headers = builder.type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(v.getError()).build();
                throw new WebApplicationException(headers);
            } catch (ValidationException v) {
                log.error("Created invalid response: Error:\n" + v.getMessage() +
                          "\nGenerated Response:\n" + j.toString(4) + "\n", v);
                throw new WebApplicationException(v, Status.INTERNAL_SERVER_ERROR);
            }
            j.write(writer);
            writer.flush();
        } catch (JSONException ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isCompatible(Set<MediaType> types, MediaType mediaType) {
        for (MediaType t : types) {
            if (t.isCompatible(mediaType)) {
                return true;
            }
        }
        return false;
    }

    public abstract T read(JSONObject j, MediaType mediaType) throws JSONException;
    public abstract JSONObject write(T t, MediaType mediaType) throws JSONException;
}
