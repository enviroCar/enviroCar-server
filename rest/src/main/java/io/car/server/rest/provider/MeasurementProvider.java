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
package io.car.server.rest.provider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.hp.hpl.jena.rdf.model.Model;

import io.car.server.core.entities.Measurement;
import io.car.server.rest.MediaTypes;

/**
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Produces({ MediaType.APPLICATION_JSON, MediaTypes.XML_RDF, MediaTypes.TURTLE,
            MediaTypes.TURTLE_ALT })
@Consumes(MediaType.APPLICATION_JSON)
public class MeasurementProvider extends AbstractEntityProvider<Measurement> {
    public MeasurementProvider() {
        super(Measurement.class);
    }

    @Override
    public Measurement read(JsonNode j, MediaType mediaType) {
        return getCodingFactory().createMeasurementDecoder()
                .decode(j, mediaType);
    }

    @Override
    public JsonNode writeJSON(Measurement t, MediaType mediaType) {
        return getCodingFactory().createMeasurementEncoder()
                .encodeJSON(t, mediaType);
    }

    @Override
    public Model writeRDF(Measurement t, MediaType mediaType) {
        return getCodingFactory().createMeasurementEncoder()
                .encodeRDF(t, mediaType);
    }
}
