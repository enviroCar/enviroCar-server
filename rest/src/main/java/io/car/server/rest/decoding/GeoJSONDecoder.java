/*
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
package io.car.server.rest.decoding;

import io.car.server.rest.util.GeoJSON;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.exception.GeometryConverterException;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GeoJSONDecoder extends AbstractEntityDecoder<Geometry> {
    private final GeoJSON geoJSON;

    @Inject
    public GeoJSONDecoder(GeoJSON geoJSON) {
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
