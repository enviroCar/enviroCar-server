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
package org.envirocar.server.rest.encoding.rdf;

import java.util.Set;

import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.statistics.Statistic;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.envirocar.server.rest.InternalServerError;
import org.envirocar.server.rest.resources.*;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@javax.ws.rs.ext.Provider
@Singleton
public class StatisticRDFEncoder extends AbstractLinkerRDFEntityEncoder<Statistic> {
    private final Provider<UriInfo> uriInfo;

    @Inject
    public StatisticRDFEncoder(Set<RDFLinker<Statistic>> linkers,
                               Provider<UriInfo> uriInfo) {
        super(Statistic.class, linkers);
        this.uriInfo = uriInfo;
    }

    @Override
    protected String getURI(Statistic t, Provider<UriBuilder> builder) {
        Object resource = uriInfo.get().getMatchedResources().get(0);

        User user;
        Track track;
        Sensor sensor;

        if (resource instanceof StatisticResource) {
            StatisticResource sr = (StatisticResource) resource;
            user = sr.getUser();
            track = sr.getTrack();
            sensor = sr.getSensor();
        } else if (resource instanceof StatisticsResource) {
            StatisticsResource sr = (StatisticsResource) resource;
            user = sr.getUser();
            track = sr.getTrack();
            sensor = sr.getSensor();
        } else {
            throw new InternalServerError();
        }
        return build(track, user, sensor, t.getPhenomenon(), builder);
    }

    protected static String build(Track t, User u, Sensor s, Phenomenon p,
                                  Provider<UriBuilder> builder) {
        if (t != null) {
            return builder.get()
                    .path(RootResource.class)
                    .path(RootResource.TRACKS)
                    .path(TracksResource.TRACK)
                    .path(TrackResource.STATISTICS)
                    .path(StatisticsResource.PHENOMENON)
                    .build(t.getIdentifier(),
                           p.getName())
                    .toASCIIString();
        } else if (u != null) {
            return builder.get()
                    .path(RootResource.class)
                    .path(RootResource.USERS)
                    .path(UsersResource.USER)
                    .path(UserResource.STATISTICS)
                    .path(StatisticsResource.PHENOMENON)
                    .build(u.getName(),
                           p.getName())
                    .toASCIIString();
        } else if (s != null) {
            return builder.get()
                    .path(RootResource.class)
                    .path(RootResource.SENSORS)
                    .path(SensorsResource.SENSOR)
                    .path(SensorResource.STATISTICS)
                    .path(StatisticsResource.PHENOMENON)
                    .build(s.getIdentifier(),
                           p.getName())
                    .toASCIIString();
        } else {
            return builder.get()
                    .path(RootResource.class)
                    .path(RootResource.STATISTICS)
                    .path(StatisticsResource.PHENOMENON)
                    .build(p.getName())
                    .toASCIIString();
        }
    }
}
