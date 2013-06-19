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

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.Model;

import io.car.server.core.DataService;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.filter.MeasurementFilter;
import io.car.server.core.util.GeoJSONConstants;
import io.car.server.rest.JSONConstants;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class TrackEncoder extends AbstractEntityEncoder<Track> {
    private final EntityEncoder<Sensor> sensorEncoder;
    private final EntityEncoder<Measurements> measurementsEncoder;
    private final EntityEncoder<User> userEncoder;
    private final DataService dataService;

    @Inject
    public TrackEncoder(EntityEncoder<Sensor> sensorEncoder,
                        EntityEncoder<Measurements> measurementsEncoder,
                        EntityEncoder<User> userEncoder,
                        DataService dataService) {
        super(Track.class);
        this.sensorEncoder = sensorEncoder;
        this.userEncoder = userEncoder;
        this.measurementsEncoder = measurementsEncoder;
        this.dataService = dataService;
    }

    @Override
    public ObjectNode encodeJSON(Track t, AccessRights rights,
                                 MediaType mediaType) {
        ObjectNode track = getJsonFactory().objectNode();

        if (mediaType.equals(MediaTypes.TRACK_TYPE)) {
            track.put(GeoJSONConstants.TYPE_KEY,
                      GeoJSONConstants.FEATURE_COLLECTION_TYPE);
            ObjectNode properties = track
                    .putObject(GeoJSONConstants.PROPERTIES_KEY);
            if (t.hasIdentifier()) {
                properties.put(JSONConstants.IDENTIFIER_KEY, t.getIdentifier());
            }
            if (t.hasName() && rights.canSeeNameOf(t)) {
                properties.put(JSONConstants.NAME_KEY, t.getName());
            }
            if (t.hasDescription() && rights.canSeeDescriptionOf(t)) {
                properties
                        .put(JSONConstants.DESCRIPTION_KEY, t.getDescription());
            }
            if (t.hasCreationTime() && rights.canSeeCreationTimeOf(t)) {
                properties.put(JSONConstants.CREATED_KEY,
                               getDateTimeFormat().print(t.getCreationTime()));
            }
            if (t.hasModificationTime() && rights
                    .canSeeModificationTimeOf(t)) {
                properties.put(JSONConstants.MODIFIED_KEY,
                               getDateTimeFormat()
                        .print(t.getModificationTime()));
            }
            if (t.hasSensor() && rights.canSeeSensorOf(t)) {
                properties.put(JSONConstants.SENSOR_KEY, sensorEncoder
                        .encodeJSON(t.getSensor(), rights, mediaType));
            }
            if (t.hasUser() && rights.canSeeUserOf(t)) {
                properties.put(JSONConstants.USER_KEY, userEncoder
                        .encodeJSON(t.getUser(), rights, mediaType));
            }
            JsonNode features;
            if (rights.canSeeMeasurementsOf(t)) {
                Measurements values = dataService
                        .getMeasurements(new MeasurementFilter(t));
                features = measurementsEncoder
                        .encodeJSON(values, rights, mediaType)
                        .path(GeoJSONConstants.FEATURES_KEY);
            } else {
                features = track.arrayNode();
            }
            track.put(GeoJSONConstants.FEATURES_KEY, features);
        } else {
            if (t.hasIdentifier()) {
                track.put(JSONConstants.IDENTIFIER_KEY, t.getIdentifier());
            }
            if (t.hasModificationTime() && rights
                    .canSeeModificationTimeOf(t)) {
                track.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat()
                        .print(t.getModificationTime()));
            }
            if (t.hasName() && rights.canSeeNameOf(t)) {
                track.put(JSONConstants.NAME_KEY, t.getName());
            }
        }
        return track;
    }

    @Override
    public Model encodeRDF(Track t, AccessRights rights, MediaType mt) {
        /* TODO implement io.car.server.rest.encoding.TrackEncoder.encodeRDF() */
        throw new UnsupportedOperationException("io.car.server.rest.encoding.TrackEncoder.encodeRDF() not yet implemented");
    }
}
