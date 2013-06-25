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
package io.car.server.rest.resources;

import javax.annotation.Nullable;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import io.car.server.core.StatisticsService;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.exception.PhenomenonNotFoundException;
import io.car.server.core.filter.StatisticsFilter;
import io.car.server.core.statistics.Statistic;
import io.car.server.core.statistics.Statistics;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.Schemas;
import io.car.server.rest.validation.Schema;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class StatisticsResource extends AbstractResource {
    private static final String PHENOMENON = "{phenomenon}";
    private final Track track;
    private final User user;
    private final Sensor sensor;
    private final StatisticsService statisticService;

    @AssistedInject
    public StatisticsResource(@Assisted User user,
                              StatisticsService statisticService) {
        this(null, user, null, statisticService);
    }

    @AssistedInject
    public StatisticsResource(@Assisted @Nullable Track track,
                              StatisticsService statisticService) {
        this(track, null, null, statisticService);
    }

    @AssistedInject
    public StatisticsResource(@Assisted Sensor sensor,
                              StatisticsService statisticService) {
        this(null, null, sensor, statisticService);
    }

    @AssistedInject
    public StatisticsResource(StatisticsService statisticService) {
        this(null, null, null, statisticService);
    }

    public StatisticsResource(Track track, User user, Sensor sensor,
                              StatisticsService statisticService) {
        this.track = track;
        this.user = user;
        this.statisticService = statisticService;
        this.sensor = sensor;
    }

    @GET
    @Schema(response = Schemas.STATISTICS)
    @Produces({ MediaTypes.STATISTICS,
                MediaTypes.XML_RDF,
                MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
    public Statistics statistics() {
        return this.statisticService
                .getStatistics(new StatisticsFilter(user, track, sensor));
    }

    @GET
    @Path(PHENOMENON)
    @Schema(response = Schemas.STATISTIC)
    @Produces({ MediaTypes.STATISTIC,
                MediaTypes.XML_RDF,
                MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
    public Statistic statistics(@PathParam("phenomenon") String phenomenon)
            throws PhenomenonNotFoundException {
        Phenomenon p = getDataService().getPhenomenonByName(phenomenon);
        return this.statisticService
                .getStatistic(new StatisticsFilter(user, track, sensor), p);
    }
}
