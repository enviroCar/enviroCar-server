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
package org.envirocar.server.rest.encoding.rdf;

import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.core.statistics.Statistics;
import org.envirocar.server.rest.resources.StatisticsResource;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@javax.ws.rs.ext.Provider
public class StatisticsRDFEncoder extends AbstractCollectionRDFEntityEncoder<Statistic, Statistics> {
    private final Provider<UriInfo> uriInfo;

    @Inject
    public StatisticsRDFEncoder(Set<RDFLinker<Statistic>> linkers,
                                Provider<UriInfo> uriInfo) {
        super(Statistics.class, linkers);
        this.uriInfo = uriInfo;
    }

    @Override
    protected String getURI(Statistic t,
                            com.google.inject.Provider<UriBuilder> builder) {
        Object resource = uriInfo.get().getMatchedResources().get(0);

        User user = null;
        Track track = null;
        Sensor sensor = null;

        if (resource instanceof StatisticsResource) {
            StatisticsResource sr = (StatisticsResource) resource;
            user = sr.getUser();
            track = sr.getTrack();
            sensor = sr.getSensor();
        } else {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
        return StatisticRDFEncoder
                .build(track, user, sensor, t.getPhenomenon(), builder);
    }
}
