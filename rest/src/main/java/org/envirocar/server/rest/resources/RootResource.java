/*
 * Copyright (C) 2013-2018 The enviroCar project
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
package org.envirocar.server.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.validation.Schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Path("/")
public class RootResource extends AbstractResource {
    public static final String TRACKS = "tracks";
    public static final String PHENOMENONS = "phenomenons";
    public static final String SENSORS = "sensors";
    public static final String MEASUREMENTS = "measurements";
    public static final String STATISTICS = "statistics";
    public static final String TERMS_OF_USE = "termsOfUse";
    public static final String SCHEMA = "schema";

    @Inject
    private JsonNodeFactory factory;

    @GET
    @Schema(response = Schemas.ROOT)
    @Produces({MediaTypes.ROOT})
    public JsonNode get() {
        ObjectNode root = factory.objectNode();
        root.put(JSONConstants.TRACKS_KEY, getUriInfo()
                .getAbsolutePathBuilder()
                .path(TRACKS).build().toString());
        root.put(JSONConstants.SENSORS_KEY, getUriInfo()
                .getAbsolutePathBuilder().path(SENSORS)
                .build().toString());
        root.put(JSONConstants.PHENOMENONS_KEY, getUriInfo()
                .getAbsolutePathBuilder()
                .path(PHENOMENONS).build().toString());
        root.put(JSONConstants.MEASUREMENTS_KEY, getUriInfo()
                .getAbsolutePathBuilder()
                .path(MEASUREMENTS).build().toString());
        root.put(JSONConstants.STATISTICS_KEY, getUriInfo()
                .getAbsolutePathBuilder()
                .path(STATISTICS).build().toString());
        root.put(JSONConstants.TERMS_OF_USE_KEY, getUriInfo()
                .getAbsolutePathBuilder()
                .path(TERMS_OF_USE).build().toString());
        root.put(JSONConstants.SCHEMA, getUriInfo()
                .getAbsolutePathBuilder()
                .path(SCHEMA).build().toString());
        return root;
    }

    @Path(TRACKS)
    public TracksResource tracks() {
        return getResourceFactory().createTracksResource();
    }

    @Path(PHENOMENONS)
    public PhenomenonsResource phenomenons() {
        return getResourceFactory().createPhenomenonsResource();
    }

    @Path(SENSORS)
    public SensorsResource sensors() {
        return getResourceFactory().createSensorsResource();
    }

    @Path(MEASUREMENTS)
    public MeasurementsResource measurements() {
        return getResourceFactory().createMeasurementsResource(null);
    }

    @Path(STATISTICS)
    public StatisticsResource statistics() {
        return getResourceFactory().createStatisticsResource();
    }

    @Path(TERMS_OF_USE)
    public TermsOfUseResource termsOfUse() {
        return getResourceFactory().createTermsOfUseResource();
    }

    @Path(SCHEMA)
    public JSONSchemaResource schemas() {
        return getResourceFactory().createSchemaResource();
    }

    @Path("rest")
    public RootResource redirect() {
        return this;
    }
}
