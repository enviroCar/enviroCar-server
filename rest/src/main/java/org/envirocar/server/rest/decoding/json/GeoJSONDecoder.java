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
package org.envirocar.server.rest.decoding.json;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

import org.envirocar.server.core.exception.GeometryConverterException;
import org.envirocar.server.rest.util.GeoJSON;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class GeoJSONDecoder extends AbstractJSONEntityDecoder<Geometry> {
    private final GeoJSON geoJSON;

    @Inject
    public GeoJSONDecoder(GeoJSON geoJSON) {
        super(Geometry.class);
        this.geoJSON = geoJSON;
    }

    @Override
    public Geometry decode(JsonNode j, MediaType mt) {
        try {
            return geoJSON.decode(j);
        } catch (GeometryConverterException ex) {
            throw new WebApplicationException(ex, Status.BAD_REQUEST);
        }
    }
}
