/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import org.locationtech.jts.geom.Geometry;
import org.envirocar.server.core.entities.TrackSummary;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
@Provider
@Singleton
public class UserStatisticJSONEncoder extends AbstractJSONEntityEncoder<UserStatistic> {

    private final JSONEntityEncoder<Geometry> geometryEncoder;

    @Inject
    public UserStatisticJSONEncoder(JSONEntityEncoder<Geometry> geometryEncoder) {
        super(UserStatistic.class);
        this.geometryEncoder = geometryEncoder;
    }

    @Override
    public ObjectNode encodeJSON(UserStatistic entity, AccessRights rights, MediaType mediaType) {
        ObjectNode userStatistics = getJsonFactory().objectNode()
                                                    .put(JSONConstants.DISTANCE_KEY, entity.getDistance())
                                                    .put(JSONConstants.DURATION_KEY, entity.getDuration());

        ObjectNode statistics = userStatistics.putObject(JSONConstants.USERSTATISTIC_KEY);

        statistics.putObject(JSONConstants.BELOW60KMH_KEY)
                  .put(JSONConstants.DISTANCE_KEY, entity.getDistanceBelow60kmh())
                  .put(JSONConstants.DURATION_KEY, entity.getDurationBelow60kmh());

        statistics.putObject(JSONConstants.ABOVE130KMH_KEY)
                  .put(JSONConstants.DISTANCE_KEY, entity.getDistanceAbove130kmh())
                  .put(JSONConstants.DURATION_KEY, entity.getDurationAbove130kmh());

        statistics.putObject(JSONConstants.NAN_KEY)
                  .put(JSONConstants.DISTANCE_KEY, entity.getDistanceNaN())
                  .put(JSONConstants.DURATION_KEY, entity.getDurationNaN());

        if (entity.hasTrackSummaries()) {
            for (TrackSummary trackSummary : entity.getTrackSummaries()) {
                userStatistics.withArray(JSONConstants.TRACKSUMMARIES_KEY)
                              .add(encodeTrackSummary(trackSummary, rights, mediaType));
            }
        }

        userStatistics.put(JSONConstants.TRACK_COUNT, entity.getNumTracks());

        return userStatistics;
    }

    private ObjectNode encodeTrackSummary(TrackSummary trackSummary, AccessRights rights, MediaType mediaType) {
        ObjectNode node = getJsonFactory().objectNode();
        if (trackSummary.hasIdentifier()) {
            node.put(JSONConstants.IDENTIFIER_KEY, trackSummary.getIdentifier());
        }
        if (trackSummary.hasStartPosition()) {
            ObjectNode startPosition = node.putObject(JSONConstants.STARTPOSITION_KEY);
            startPosition.set(JSONConstants.GEOMETRY_KEY,
                              this.geometryEncoder.encodeJSON(trackSummary.getStartPosition(), rights, mediaType));
        }
        if (trackSummary.hasEndPosition()) {
            ObjectNode endPosition = node.putObject(JSONConstants.ENDPOSITION_KEY);
            endPosition.set(JSONConstants.GEOMETRY_KEY,
                            this.geometryEncoder.encodeJSON(trackSummary.getEndPosition(), rights, mediaType));
        }
        return node;
    }
}
