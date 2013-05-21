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

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;

import io.car.server.core.statistics.Statistics;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class StatisticsProvider extends AbstractJsonEntityProvider<Statistics> {

    public StatisticsProvider() {
        super(Statistics.class);
    }

    @Override
    public Statistics read(JsonNode j, MediaType mediaType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNode write(Statistics t, MediaType mediaType) {
        return getCodingFactory().createStatisticsEncoder().encode(t, mediaType);
    }
}
