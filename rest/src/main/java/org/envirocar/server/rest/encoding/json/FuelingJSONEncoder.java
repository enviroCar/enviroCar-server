/*
 * Copyright (C) 2013-2021 The enviroCar project
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
import org.envirocar.server.core.entities.DimensionedNumber;
import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * JSON encoder for {@link Fueling}s.
 *
 * @author Christian Autermann
 */
@Provider
@Singleton
public class FuelingJSONEncoder extends AbstractJSONEntityEncoder<Fueling> {
    private final JSONEntityEncoder<User> userEncoder;
    private final JSONEntityEncoder<Sensor> sensorEncoder;

    /**
     * Creates a new {@code FuelingJSONEncoder} using the specified delegates.
     *
     * @param userEncoder   the encoder to encode {@link User}s
     * @param sensorEncoder the encoder to encode {@link Sensor}s
     */
    @Inject
    public FuelingJSONEncoder(JSONEntityEncoder<User> userEncoder, JSONEntityEncoder<Sensor> sensorEncoder) {
        super(Fueling.class);
        this.userEncoder = checkNotNull(userEncoder);
        this.sensorEncoder = checkNotNull(sensorEncoder);
    }

    @Override
    public ObjectNode encodeJSON(Fueling entity, AccessRights rights, MediaType mediaType) {
        ObjectNode fueling = getJsonFactory().objectNode();
        if (entity.hasFuelType()) {
            fueling.put(JSONConstants.FUEL_TYPE, entity.getFuelType());
        }
        if (entity.hasCost()) {
            fueling.set(JSONConstants.COST, encodeJSON(entity.getCost()));
        }
        if (entity.hasMileage()) {
            fueling.set(JSONConstants.MILEAGE, encodeJSON(entity.getMileage()));
        }
        if (entity.hasVolume()) {
            fueling.set(JSONConstants.VOLUME, encodeJSON(entity.getVolume()));
        }
        if (entity.hasTime()) {
            fueling.put(JSONConstants.TIME_KEY, getDateTimeFormat().print(entity.getTime()));
        }
        if (entity.hasCar()) {
            fueling.set(JSONConstants.CAR_KEY, sensorEncoder.encodeJSON(entity.getCar(), rights, mediaType));
        }
        if (entity.hasUser()) {
            fueling.set(JSONConstants.USER_KEY, userEncoder.encodeJSON(entity.getUser(), rights, mediaType));
        }
        if (entity.hasCreationTime()) {
            fueling.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(entity.getCreationTime()));
        }
        if (entity.hasModificationTime()) {
            fueling.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(entity.getModificationTime()));
        }
        if (entity.hasComment()) {
            fueling.put(JSONConstants.COMMENT, entity.getComment());
        }
        if (entity.hasIdentifier()) {
            fueling.put(JSONConstants.IDENTIFIER_KEY, entity.getIdentifier());
        }
        fueling.put(JSONConstants.MISSED_FUEL_STOP, entity.isMissedFuelStop());
        fueling.put(JSONConstants.PARTIAL_FUELING, entity.isPartialFueling());
        return fueling;
    }

    private ObjectNode encodeJSON(DimensionedNumber dm) {
        ObjectNode node = getJsonFactory().objectNode();
        node.put(JSONConstants.VALUE_KEY, dm.value());
        node.put(JSONConstants.UNIT_KEY, dm.unit());
        return node;
    }
}
