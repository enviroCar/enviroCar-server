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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.entities.TrackSummaries;
import org.envirocar.server.core.entities.TrackSummary;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
@Provider
public class UserStatisticJSONEncoder extends AbstractJSONEntityEncoder<UserStatistic> {

    private final JSONEntityEncoder<Geometry> geometryEncoder;

    @Inject
    public UserStatisticJSONEncoder(
            JSONEntityEncoder<Geometry> geometryEncoder) {
        super(UserStatistic.class);
        this.geometryEncoder = geometryEncoder;
    }

    @Override
    public ObjectNode encodeJSON(UserStatistic t, AccessRights rights, MediaType mt) {
        ObjectNode userStatistics = getJsonFactory().objectNode();
        userStatistics.putPOJO(JSONConstants.DISTANCE_KEY, t.getDistance());
        userStatistics.putPOJO(JSONConstants.DURATION_KEY, t.getDuration());
        ObjectNode statistics = userStatistics.putObject(JSONConstants.USERSTATISTIC_KEY);
        ObjectNode below60kmh = statistics.putObject(JSONConstants.BELOW60KMH_KEY);
        below60kmh.putPOJO(JSONConstants.DISTANCE_KEY, t.getDistanceBelow60kmh());
        below60kmh.putPOJO(JSONConstants.DURATION_KEY, t.getDurationBelow60kmh());
        ObjectNode above130kmh = statistics.putObject(JSONConstants.ABOVE130KMH_KEY);
        above130kmh.putPOJO(JSONConstants.DISTANCE_KEY, t.getDistanceAbove130kmh());
        above130kmh.putPOJO(JSONConstants.DURATION_KEY, t.getDurationAbove130kmh());
        ObjectNode NaN = statistics.putObject(JSONConstants.NAN_KEY);
        NaN.putPOJO(JSONConstants.DISTANCE_KEY, t.getDistanceNaN());
        NaN.putPOJO(JSONConstants.DURATION_KEY, t.getDurationNaN());
        userStatistics.set(JSONConstants.TRACKSUMMARIES_KEY, getTrackSummaries(t.getTrackSummaries(), rights, mt));
        return userStatistics;
    }

    private JsonNode getTrackSummaries(TrackSummaries t, AccessRights rights, MediaType mediaType) {
        ArrayNode result = getJsonFactory().arrayNode();
        if (t.hasTrackSummaries()) {
            for (TrackSummary item : t.getTrackSummaryList()) {
                ObjectNode ts = result.addObject();
                if (item.hasIdentifier()) {
                    ts.putPOJO(JSONConstants.IDENTIFIER_KEY, item.getIdentifier());
                }
                if (item.hasStartPosition()) {
                    ObjectNode startPos = ts.putObject(JSONConstants.STARTPOSITION_KEY);
                    startPos.set(JSONConstants.GEOMETRY_KEY,
                                 geometryEncoder.encodeJSON(item.getStartPosition(), rights, mediaType));
                }
                if (item.hasEndPosition()) {
                    ObjectNode endPos = ts.putObject(JSONConstants.ENDPOSITION_KEY);
                    endPos.set(JSONConstants.GEOMETRY_KEY,
                               geometryEncoder.encodeJSON(item.getEndPosition(), rights, mediaType));
                }
            }
        }
        return result;
    }
}
