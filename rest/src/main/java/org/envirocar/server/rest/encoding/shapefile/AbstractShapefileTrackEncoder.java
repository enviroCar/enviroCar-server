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
package org.envirocar.server.rest.encoding.shapefile;

import java.io.File;

import javax.ws.rs.core.MediaType;

import org.envirocar.server.core.exception.TrackTooLongException;
import org.envirocar.server.rest.encoding.ShapefileTrackEncoder;
import org.envirocar.server.rest.rights.AccessRights;
import org.joda.time.format.DateTimeFormatter;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * TODO JavaDoc
 *
 * @author Benjamin Pross
 */
public abstract class AbstractShapefileTrackEncoder<T>
        extends AbstractShapefileMessageBodyWriter<T>
        implements ShapefileTrackEncoder<T> {
    private DateTimeFormatter dateTimeFormat;
    private Provider<AccessRights> rights;

    public AbstractShapefileTrackEncoder(Class<T> classType) {
        super(classType);
    }

    public DateTimeFormatter getDateTimeFormat() {
        return dateTimeFormat;
    }

    @Inject
    public void setDateTimeFormat(DateTimeFormatter dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    @Inject
    public void setRights(Provider<AccessRights> rights) {
        this.rights = rights;
    }

    @Override
    public File encodeShapefile(T t, MediaType mt) throws TrackTooLongException{
        return encodeShapefile(t, rights.get(), mt);
    }
}
