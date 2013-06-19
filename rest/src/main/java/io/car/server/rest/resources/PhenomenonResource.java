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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.StatisticsService;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.exception.PhenomenonNotFoundException;
import io.car.server.core.statistics.Statistic;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.Schemas;
import io.car.server.rest.validation.Schema;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class PhenomenonResource extends AbstractResource {
    public static final String STATISTIC = "statistic";
    private final Phenomenon phenomenon;
    private final StatisticsService statisticsService;

    @Inject
    public PhenomenonResource(@Assisted Phenomenon phenomenon,
                              StatisticsService statisticsService) {
        this.phenomenon = phenomenon;
        this.statisticsService = statisticsService;
    }

    @GET
    @Schema(response = Schemas.PHENOMENON)
    @Produces({ MediaTypes.PHENOMENON, MediaTypes.XML_RDF, MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
    public Phenomenon getPhenomenon() throws PhenomenonNotFoundException {
        return phenomenon;
    }

    @GET
    @Path(STATISTIC)
    @Schema(response = Schemas.STATISTIC)
    @Produces({ MediaTypes.STATISTIC, MediaTypes.XML_RDF, MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
    public Statistic getStatistics() {
        checkRights(getRights().canSeeStatisticsOf(phenomenon));
        return this.statisticsService.getStatistics(this.phenomenon);
    }
}
