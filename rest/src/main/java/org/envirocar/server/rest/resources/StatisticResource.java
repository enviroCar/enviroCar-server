/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.envirocar.server.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.filter.StatisticsFilter;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.validation.Schema;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class StatisticResource extends AbstractResource {
    private final Track track;
    private final User user;
    private final Sensor sensor;
    private final Phenomenon phenomenon;

    public StatisticResource(Track track, User user, Sensor sensor,
                             Phenomenon phenomenon) {
        this.track = track;
        this.user = user;
        this.sensor = sensor;
        this.phenomenon = phenomenon;
    }

    @GET
    @Schema(response = Schemas.STATISTIC)
    @Produces({ MediaTypes.STATISTIC,
                MediaTypes.XML_RDF,
                MediaTypes.TURTLE,
                MediaTypes.TURTLE_ALT })
    public Statistic get() {
        return getStatisticsService().getStatistic(
                new StatisticsFilter(getUser(), getTrack(), getSensor()),
                getPhenomenon());
    }

    public Track getTrack() {
        return track;
    }

    public User getUser() {
        return user;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Phenomenon getPhenomenon() {
        return phenomenon;
    }
}
