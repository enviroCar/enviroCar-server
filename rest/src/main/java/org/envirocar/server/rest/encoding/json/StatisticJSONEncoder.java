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
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;

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
public class StatisticJSONEncoder extends AbstractJSONEntityEncoder<Statistic> {
    private final JSONEntityEncoder<Phenomenon> phenomenonEncoder;

    @Inject
    public StatisticJSONEncoder(JSONEntityEncoder<Phenomenon> phenomenonEncoder) {
        super(Statistic.class);
        this.phenomenonEncoder = phenomenonEncoder;
    }

    @Override
    public ObjectNode encodeJSON(Statistic entity, MediaType mediaType) {
        ObjectNode statistic = getJsonFactory().objectNode();
        statistic.put(JSONConstants.MAX_KEY, entity.getMax());
        statistic.put(JSONConstants.AVG_KEY, entity.getMean());
        statistic.put(JSONConstants.MIN_KEY, entity.getMin());
        statistic.put(JSONConstants.MEASUREMENTS_KEY, entity.getMeasurements());
        statistic.put(JSONConstants.TRACKS_KEY, entity.getTracks());
        statistic.put(JSONConstants.USERS_KEY, entity.getUsers());
        statistic.put(JSONConstants.SENSORS_KEY, entity.getSensors());
        statistic.set(JSONConstants.PHENOMENON_KEY, phenomenonEncoder.encodeJSON(entity.getPhenomenon(), mediaType));
        return statistic;
    }
}
