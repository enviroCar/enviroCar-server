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
package org.envirocar.server.rest.decoding.json;

import java.math.BigDecimal;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.entities.DimensionedNumber;
import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.rest.JSONConstants;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

/**
 * JSON decoder for {@link Fueling}s.
 *
 * @author Christian Autermann
 */
@Provider
@Singleton
public class FuelingDecoder extends AbstractJSONEntityDecoder<Fueling> {
    private static final boolean MISSED_FUEL_STOP_DEFAULT_VALUE = true;
    private static final boolean PARTIAL_FUELING_DEFAULT_VALUE = false;
    private final SensorDao sensorDao;

    /**
     * Creates a new {@code FuelingDecoder}.
     *
     * @param sensorDao the sensor DAO to fetch the sensor
     */
    @Inject
    public FuelingDecoder(SensorDao sensorDao) {
        super(Fueling.class);
        this.sensorDao = sensorDao;
    }

    @Override
    public Fueling decode(JsonNode node, MediaType mediaType) {
        Fueling fueling = getEntityFactory().createFueling();
        fueling.setFuelType(node.path(JSONConstants.FUEL_TYPE).textValue());
        fueling.setComment(node.path(JSONConstants.COMMENT).textValue());
        fueling.setCost(decodeDimensionedNumber(node.path(JSONConstants.COST)));
        fueling.setMileage(decodeDimensionedNumber(node.path(JSONConstants.MILEAGE)));
        fueling.setMissedFuelStop(node.path(JSONConstants.MISSED_FUEL_STOP).asBoolean(MISSED_FUEL_STOP_DEFAULT_VALUE));
        fueling.setPartialFueling(node.path(JSONConstants.PARTIAL_FUELING).asBoolean(PARTIAL_FUELING_DEFAULT_VALUE));
        fueling.setTime(getDateTimeFormat().parseDateTime(node.path(JSONConstants.TIME_KEY).textValue()));
        fueling.setVolume(decodeDimensionedNumber(node.path(JSONConstants.VOLUME)));
        fueling.setCar(sensorDao.getByIdentifier(node.path(JSONConstants.CAR_KEY).textValue()));
        return fueling;
    }

    private DimensionedNumber decodeDimensionedNumber(JsonNode node) {
        BigDecimal value = node.path(JSONConstants.VALUE_KEY).decimalValue();
        String unit = node.path(JSONConstants.UNIT_KEY).textValue();
        return new DimensionedNumber(value, unit);
    }

}
