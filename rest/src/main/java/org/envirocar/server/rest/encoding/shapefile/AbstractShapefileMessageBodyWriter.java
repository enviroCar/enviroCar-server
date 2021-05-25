/*
 * Copyright (C) 2013-2020 The enviroCar project
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

import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.encoding.ShapefileTrackEncoder;

import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * TODO: Javadoc
 *
 * @author Benjamin Pross
 */
@Produces(MediaTypes.APPLICATION_ZIPPED_SHP)
public abstract class AbstractShapefileMessageBodyWriter<T> implements MessageBodyWriter<T>, ShapefileTrackEncoder<T> {
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    private final Class<T> classType;

    public AbstractShapefileMessageBodyWriter(Class<T> classType) {
        this.classType = classType;
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaTyp) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
                               MediaType mediaType) {
        return this.classType.isAssignableFrom(type) && (
                mediaType.isCompatible(MediaTypes.APPLICATION_ZIPPED_SHP_TYPE) ||
                mediaType.isCompatible(MediaTypes.APPLICATION_X_SHAPEFILE_TYPE));
    }

    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> h,
                        OutputStream out) throws IOException {
        Path shapeFile = null;
        try {
            shapeFile = encodeShapefile(t, mediaType);
            Files.copy(shapeFile, out);
        } finally {
            if (shapeFile != null) {
                delete(shapeFile);
            }
        }
    }

    protected void delete(Path path) throws IOException {
        if (path != null && Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path directory, IOException exception) throws IOException {
                    if (exception != null) {
                        throw exception;
                    }
                    Files.delete(directory);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    protected abstract Path encodeShapefile(T t, MediaType mt) throws IOException;

}
