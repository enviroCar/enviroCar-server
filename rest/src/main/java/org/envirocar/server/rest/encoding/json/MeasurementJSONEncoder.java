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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.util.GeoJSONConstants;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Singleton
public class MeasurementJSONEncoder extends AbstractJSONEntityEncoder<Measurement> {
    private final JSONEntityEncoder<Geometry> geometryEncoder;
    private final JSONEntityEncoder<User> userProvider;
    private final JSONEntityEncoder<Sensor> sensorProvider;

    @Inject
    public MeasurementJSONEncoder(JSONEntityEncoder<Geometry> geometryEncoder,
                                  JSONEntityEncoder<User> userProvider,
                                  JSONEntityEncoder<Sensor> sensorProvider) {
        super(Measurement.class);
        this.geometryEncoder = geometryEncoder;
        this.userProvider = userProvider;
        this.sensorProvider = sensorProvider;
    }

    @Override
    public ObjectNode encodeJSON(Measurement entity, AccessRights rights, MediaType mediaType) {
        ObjectNode measurement = getJsonFactory().objectNode();
        measurement.put(GeoJSONConstants.TYPE_KEY, GeoJSONConstants.FEATURE_TYPE);
        if (entity.hasGeometry() && rights.canSeeGeometryOf(entity)) {
            measurement.set(JSONConstants.GEOMETRY_KEY, geometryEncoder.encodeJSON(entity.getGeometry(), rights, mediaType));
        }

        ObjectNode properties = measurement.putObject(GeoJSONConstants.PROPERTIES_KEY);
        if (entity.hasIdentifier()) {
            properties.put(JSONConstants.IDENTIFIER_KEY, entity.getIdentifier());
        }
        if (entity.hasTime() && rights.canSeeTimeOf(entity)) {
            properties.put(JSONConstants.TIME_KEY, getDateTimeFormat().print(entity.getTime()));
        }

        if (!getSchemaUriConfiguration().isSchema(mediaType, Schemas.TRACK)) {
            if (entity.hasSensor() && rights.canSeeSensorOf(entity)){
                properties.set(JSONConstants.SENSOR_KEY, sensorProvider.encodeJSON(entity.getSensor(), rights, mediaType));
            }
            if (entity.hasUser() && rights.canSeeUserOf(entity)) {
                properties.set(JSONConstants.USER_KEY, userProvider.encodeJSON(entity.getUser(), rights, mediaType));
            }
            if (entity.hasModificationTime() && rights.canSeeModificationTimeOf(entity)) {
                properties.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(entity.getModificationTime()));
            }
            if (entity.hasCreationTime() && rights.canSeeCreationTimeOf(entity)) {
                properties.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(entity.getCreationTime()));
            }
            if (entity.hasTrack() && rights.canSeeTracks()) {
                properties.put(JSONConstants.TRACK_KEY, entity.getTrack().getIdentifier());
            }
        }
        if (getSchemaUriConfiguration().isSchema(mediaType, Schemas.MEASUREMENT)
                || getSchemaUriConfiguration().isSchema(mediaType, Schemas.MEASUREMENTS)
                || getSchemaUriConfiguration().isSchema(mediaType, Schemas.TRACK)) {
            if (rights.canSeeValuesOf(entity)) {
                ObjectNode values = properties.putObject(JSONConstants.PHENOMENONS_KEY);

                for (MeasurementValue measurementValue : entity.getValues()) {
                    if (measurementValue.hasPhenomenon() && measurementValue.hasValue()) {
                        ObjectNode phenomenon = values.objectNode();
                        phenomenon.putPOJO(JSONConstants.VALUE_KEY, measurementValue.getValue());
                        values.set(measurementValue.getPhenomenon().getName(), phenomenon);
                        if (measurementValue.getPhenomenon().hasUnit()) {
                            phenomenon.put(JSONConstants.UNIT_KEY, measurementValue.getPhenomenon().getUnit());
                        }
                    }
                }
            }
        }
        return measurement;
    }
}
