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

import io.car.server.core.entities.Track;
import io.car.server.rest.MediaTypes;

@Provider
@Produces({ MediaType.APPLICATION_JSON, MediaTypes.XML_RDF, MediaTypes.TURTLE,
            MediaTypes.TURTLE_ALT })
@Consumes(MediaType.APPLICATION_JSON)
public class TrackProvider extends AbstractEntityProvider<Track> {
    public TrackProvider() {
        super(Track.class);
    }

    @Override
    public Track read(JsonNode j, MediaType mediaType) {
        return getCodingFactory().createTrackDecoder().decode(j, mediaType);
    }

    @Override
    public JsonNode writeJSON(Track t, MediaType mediaType) {
        return getCodingFactory().createTrackEncoder().encodeJSON(t, mediaType);
    }

    @Override
    public Model writeRDF(Track t, MediaType mediaType) {
        return getCodingFactory().createTrackEncoder().encodeRDF(t, mediaType);
    }
}