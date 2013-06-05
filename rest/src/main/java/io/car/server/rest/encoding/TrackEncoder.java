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

import java.net.URI;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.util.GeoJSONConstants;
import io.car.server.rest.JSONConstants;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.TrackResource;
import io.car.server.rest.resources.TracksResource;

public class TrackEncoder extends AbstractEntityEncoder<Track> {
    private final EntityEncoder<Sensor> sensorEncoder;
    private final EntityEncoder<Measurements> measurementsEncoder;
    private final EntityEncoder<User> userEncoder;

    @Inject
    public TrackEncoder(EntityEncoder<Sensor> sensorEncoder,
                        EntityEncoder<Measurements> measurementsEncoder,
                        EntityEncoder<User> userEncoder) {
        this.sensorEncoder = sensorEncoder;
        this.userEncoder = userEncoder;
        this.measurementsEncoder = measurementsEncoder;
    }

    @Override
    public ObjectNode encode(Track t, MediaType mediaType) {
        ObjectNode track = getJsonFactory().objectNode();

        if (mediaType.equals(MediaTypes.TRACK_TYPE)) {
            track.put(GeoJSONConstants.TYPE_KEY,
                      GeoJSONConstants.FEATURE_COLLECTION_TYPE);
            ObjectNode properties = track
                    .putObject(GeoJSONConstants.PROPERTIES_KEY);
            if (t.hasIdentifier()) {
                properties.put(JSONConstants.IDENTIFIER_KEY, t.getIdentifier());
            }
            if (t.hasName()) {
                properties.put(JSONConstants.NAME_KEY, t.getName());
            }
            if (t.hasDescription()) {
                properties
                        .put(JSONConstants.DESCRIPTION_KEY, t.getDescription());
            }
            if (t.hasCreationTime()) {
                properties.put(JSONConstants.CREATED_KEY,
                               getDateTimeFormat().print(t.getCreationTime()));
            }
            if (t.hasModificationTime()) {
                properties.put(JSONConstants.MODIFIED_KEY,
                               getDateTimeFormat()
                        .print(t.getModificationTime()));
            }
            if (t.hasSensor()) {
                properties.put(JSONConstants.SENSOR_KEY,
                               sensorEncoder.encode(t.getSensor(), mediaType));
            }
            if (t.hasUser()) {
                properties.put(JSONConstants.USER_KEY,
                               userEncoder.encode(t.getUser(), mediaType));
            }

            URI measurements = getUriInfo().getAbsolutePathBuilder()
                    .path(TrackResource.MEASUREMENTS).build();
            properties.put(JSONConstants.MEASUREMENTS_KEY,
                           measurements.toString());

            URI statistics = getUriInfo().getAbsolutePathBuilder()
                    .path(TrackResource.STATISTICS).build();
            properties.put(JSONConstants.STATISTICS_KEY,
                           statistics.toString());

            Measurements values = getService().getMeasurementsByTrack(t, null);
            JsonNode features = measurementsEncoder
                    .encode(values, mediaType)
                    .path(GeoJSONConstants.FEATURES_KEY);
            track.put(GeoJSONConstants.FEATURES_KEY, features);
        } else {
            if (t.hasIdentifier()) {
                track.put(JSONConstants.IDENTIFIER_KEY, t.getIdentifier());
            }
            if (t.hasModificationTime()) {
                track.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat()
                        .print(t.getModificationTime()));
            }
            if (t.hasName()) {
                track.put(JSONConstants.NAME_KEY, t.getName());
            }
            URI uri = getUriInfo().getBaseUriBuilder().path(RootResource.class)
                    .path(RootResource.TRACKS).path(TracksResource.TRACK)
                    .build(t.getIdentifier());
            track.put(JSONConstants.HREF_KEY, uri.toString());
        }
        return track;
    }
}