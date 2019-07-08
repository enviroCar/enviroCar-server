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
package org.envirocar.server.rest.encoding.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.core.util.GeoJSONConstants;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.schema.JsonSchemaUriConfiguration;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Singleton
public class TrackJSONEncoder extends AbstractJSONEntityEncoder<Track> {
    private final JSONEntityEncoder<Sensor> sensorEncoder;
    private final JSONEntityEncoder<Measurements> measurementsEncoder;
    private final DataService dataService;

    @Inject
    public TrackJSONEncoder(JSONEntityEncoder<Sensor> sensorEncoder,
                            JSONEntityEncoder<Measurements> measurementsEncoder,
                            DataService dataService) {
        super(Track.class);
        this.sensorEncoder = sensorEncoder;
        this.measurementsEncoder = measurementsEncoder;
        this.dataService = dataService;
    }

    @Override
    public ObjectNode encodeJSON(Track entity, MediaType mediaType) {
        ObjectNode track = getJsonFactory().objectNode();
        if (getSchemaUriConfiguration().isSchema(mediaType, Schemas.TRACK)) {
            track.put(GeoJSONConstants.TYPE_KEY, GeoJSONConstants.FEATURE_COLLECTION_TYPE);
            ObjectNode properties = track.putObject(GeoJSONConstants.PROPERTIES_KEY);
            if (entity.hasIdentifier()) {
                properties.put(JSONConstants.IDENTIFIER_KEY, entity.getIdentifier());
            }
            if (entity.hasSensor()) {
                properties.set(JSONConstants.SENSOR_KEY, sensorEncoder.encodeJSON(entity.getSensor(), mediaType));
            }
            if (entity.hasLength()) {
                properties.put(JSONConstants.LENGTH_KEY, entity.getLength());
            }

            if (entity.hasAppVersion()) {
                properties.put(JSONConstants.APP_VERSION_KEY, entity.getAppVersion());
            }
            if (entity.hasTouVersion()) {
                properties.put(JSONConstants.TOU_VERSION_KEY, entity.getTouVersion());
            }
            Measurements values = dataService.getMeasurements(new MeasurementFilter(entity));
            JsonNode features = measurementsEncoder.encodeJSON(values, mediaType).path(GeoJSONConstants.FEATURES_KEY);
            track.set(GeoJSONConstants.FEATURES_KEY, features);
        } else {
            if (entity.hasIdentifier()) {
                track.put(JSONConstants.IDENTIFIER_KEY, entity.getIdentifier());
            }
            //additional props
            if (entity.hasLength()) {
                track.put(JSONConstants.LENGTH_KEY, entity.getLength());
            }
            if (entity.hasBegin()) {
                track.put(JSONConstants.BEGIN_KEY, getDateTimeFormat().print(entity.getBegin()));
            }
            if (entity.hasBegin()) {
                track.put(JSONConstants.END_KEY, getDateTimeFormat().print(entity.getEnd()));
            }
            if (entity.hasSensor()) {
                track.set(JSONConstants.SENSOR_KEY, this.sensorEncoder.encodeJSON(entity.getSensor(), mediaType));
            }

        }
        return track;
    }
}
