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

import io.car.server.core.entities.Sensors;
import io.car.server.rest.MediaTypes;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Produces({ MediaType.APPLICATION_JSON, MediaTypes.XML_RDF, MediaTypes.TURTLE,
            MediaTypes.TURTLE_ALT })
@Consumes(MediaType.APPLICATION_JSON)
public class SensorsProvider extends AbstractEntityProvider<Sensors> {
    public SensorsProvider() {
        super(Sensors.class);
    }

    @Override
    public Sensors read(JsonNode j, MediaType mediaType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode writeJSON(Sensors t, MediaType mediaType) {
        return getCodingFactory().createSensorsEncoder()
                .encodeJSON(t, mediaType);
    }

    @Override
    public Model writeRDF(Sensors t, MediaType mediaType) {
        return getCodingFactory().createSensorsEncoder().encodeRDF(t, mediaType);
    }
}
