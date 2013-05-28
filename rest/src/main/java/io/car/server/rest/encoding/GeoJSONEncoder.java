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
package io.car.server.rest.encoding;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.Model;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.exception.GeometryConverterException;
import io.car.server.rest.util.GeoJSON;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class GeoJSONEncoder extends AbstractEntityEncoder<Geometry> {
    private final GeoJSON geoJSON;

    @Inject
    public GeoJSONEncoder(GeoJSON geoJSON) {
        this.geoJSON = geoJSON;
    }

    @Override
    public ObjectNode encodeJSON(Geometry t, MediaType mt) {
        try {
            return geoJSON.encode(t);
        } catch (GeometryConverterException ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Model encodeRDF(Geometry t, MediaType mt) {
        /* TODO implement io.car.server.rest.encoding.GeoJSONEncoder.encodeRDF() */
        throw new UnsupportedOperationException("io.car.server.rest.encoding.GeoJSONEncoder.encodeRDF() not yet implemented");
    }

}
