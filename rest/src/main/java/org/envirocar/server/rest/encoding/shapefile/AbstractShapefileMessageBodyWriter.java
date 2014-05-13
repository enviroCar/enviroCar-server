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
package org.envirocar.server.rest.encoding.shapefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.apache.commons.io.IOUtils;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.encoding.ShapefileTrackEncoder;

@Produces(MediaTypes.APPLICATION_ZIPPED_SHP)
public abstract class AbstractShapefileMessageBodyWriter<T> implements
		MessageBodyWriter<T>, ShapefileTrackEncoder<T> {

    private final Class<T> classType;

    public AbstractShapefileMessageBodyWriter(Class<T> classType) {
        this.classType = classType;
    }
    
	public abstract File encodeShapefile(T t, MediaType mt);

	@Override
	public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaTyp) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType) {
        return this.classType.isAssignableFrom(type) &&
                mediaType.isCompatible(MediaTypes.APPLICATION_ZIPPED_SHP_TYPE);
	}

	@Override
	public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> h,
			OutputStream out) throws IOException, WebApplicationException {
		
		File shapeFile = encodeShapefile(t, mediaType);
		
		FileInputStream fileInputStream = new FileInputStream(shapeFile);
		
		IOUtils.copy(fileInputStream, out);
		
	}

}
