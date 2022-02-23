/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.rest.encoding.ShapefileTrackEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.Path;

/**
 * TODO JavaDoc
 *
 * @author Benjamin Pross
 */
public abstract class AbstractShapefileTrackEncoder extends AbstractShapefileMessageBodyWriter<Track>
        implements ShapefileTrackEncoder<Track> {
    private Provider<AccessRights> rights;

    public AbstractShapefileTrackEncoder() {
        super(Track.class);
    }

    @Inject
    public void setRights(Provider<AccessRights> rights) {
        this.rights = rights;
    }

    @Override
    public void writeTo(Track track, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> h, OutputStream out) throws IOException {
        h.putSingle(CONTENT_DISPOSITION, String.format("attachment; filename=\"%s.zip\"", track.getIdentifier()));
        super.writeTo(track, type, genericType, annotations, mediaType, h, out);
    }

    @Override
    public Path encodeShapefile(Track t, MediaType mt) throws IOException {
        return encodeShapefile(t, rights.get(), mt);
    }
}
