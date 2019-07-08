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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.filter.StatisticsFilter;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.schema.Schema;

import javax.annotation.Nullable;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class StatisticResource extends AbstractResource {
    private final Track track;
    private final Sensor sensor;
    private final Phenomenon phenomenon;

    @Inject
    public StatisticResource(@Nullable @Assisted Track track,
                             @Nullable @Assisted Sensor sensor,
                             @Assisted Phenomenon phenomenon) {
        this.track = track;
        this.sensor = sensor;
        this.phenomenon = phenomenon;
    }

    @GET
    @Schema(response = Schemas.STATISTIC)
    @Produces({MediaTypes.JSON, MediaTypes.XML_RDF, MediaTypes.TURTLE, MediaTypes.TURTLE_ALT})
    public Statistic get() {
        return getStatisticsService().getStatistic(new StatisticsFilter(track, sensor), phenomenon);
    }

    public Track getTrack() {
        return track;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Phenomenon getPhenomenon() {
        return phenomenon;
    }
}
