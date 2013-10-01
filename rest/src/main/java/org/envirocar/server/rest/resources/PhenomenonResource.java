/*
 * Copyright (C) 2013 The enviroCar project
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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.exception.PhenomenonNotFoundException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.validation.Schema;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class PhenomenonResource extends AbstractResource {
    public static final String STATISTIC = "statistic";
    private final Phenomenon phenomenon;

    @Inject
    public PhenomenonResource(@Assisted Phenomenon phenomenon) {
        this.phenomenon = phenomenon;
    }

    @GET
    @Schema(response = Schemas.PHENOMENON)
    @Produces({ MediaTypes.PHENOMENON,
                MediaTypes.XML_RDF,
                MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
    public Phenomenon getPhenomenon() throws PhenomenonNotFoundException {
        return phenomenon;
    }

    @Path(STATISTIC)
    public StatisticResource getStatistic() {
        checkRights(getRights().canSeeStatisticsOf(phenomenon));
        return getResourceFactory()
                .createStatisticResource(phenomenon, null, null, null);
    }
}
