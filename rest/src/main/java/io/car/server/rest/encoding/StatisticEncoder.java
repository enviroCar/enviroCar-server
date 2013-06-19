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
package io.car.server.rest.encoding;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.hp.hpl.jena.rdf.model.Model;

import io.car.server.core.entities.Phenomenon;
import io.car.server.core.statistics.Statistic;
import io.car.server.rest.JSONConstants;
import io.car.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class StatisticEncoder extends AbstractEntityEncoder<Statistic> {
    private final EntityEncoder<Phenomenon> phenomenonEncoder;

    @Inject
    public StatisticEncoder(EntityEncoder<Phenomenon> phenomenonEncoder) {
        super(Statistic.class);
        this.phenomenonEncoder = phenomenonEncoder;
    }

    @Override
    public ObjectNode encodeJSON(Statistic t, AccessRights rights, MediaType mt) {
        ObjectNode statistic = getJsonFactory().objectNode();
        statistic.put(JSONConstants.MAX_KEY, t.getMax());
        statistic.put(JSONConstants.AVG_KEY, t.getMean());
        statistic.put(JSONConstants.MIN_KEY, t.getMin());
        statistic.put(JSONConstants.MEASUREMENTS_KEY, t.getMeasurements());
        statistic.put(JSONConstants.TRACKS_KEY, t.getTracks());
        statistic.put(JSONConstants.USERS_KEY, t.getUsers());
        statistic.put(JSONConstants.PHENOMENON_KEY,
                      phenomenonEncoder
                .encodeJSON(t.getPhenomenon(), rights, mt));
        return statistic;
    }

    @Override
    public Model encodeRDF(Statistic t, AccessRights rights, MediaType mt) {
        /* TODO implement io.car.server.rest.encoding.StatisticEncoder.encodeRDF() */
        throw new UnsupportedOperationException("io.car.server.rest.encoding.StatisticEncoder.encodeRDF() not yet implemented");
    }
}
