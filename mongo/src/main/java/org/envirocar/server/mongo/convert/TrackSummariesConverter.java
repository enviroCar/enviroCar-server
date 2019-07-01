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
package org.envirocar.server.mongo.convert;

import java.util.List;

import com.mongodb.DBObject;
import org.bson.types.BasicBSONList;
import org.envirocar.server.core.entities.TrackSummaries;
import org.envirocar.server.core.entities.TrackSummary;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.MappingException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * MongoDB type converter for {@link TrackSummaries}.
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
public class TrackSummariesConverter extends TypeConverter implements SimpleValueConverter {
    private static final String START_POSITION_LAT = "startPositionLat";
    private static final String START_POSITION_LNG = "startPositionLng";
    private static final String END_POSITION_LAT = "endPositionLat";
    private static final String END_POSITION_LNG = "endPositionLng";
    private static final String ID = "id";

    public TrackSummariesConverter() {
        super(TrackSummaries.class);
    }

    /**
     * FIX ME: encode & decode as defined in the Schemas: via geometry objects instead off double lat,lng, ...
     *
     * @param trackSummaries
     * @param optionalExtraInfo
     *
     * @return
     */
    @Override
    public Object encode(Object trackSummaries, MappedField optionalExtraInfo) {
        if (trackSummaries == null) {
            return null;
        }
        TrackSummaries dn = (TrackSummaries) trackSummaries;
        List<TrackSummary> trackSummaryList = dn.getTrackSummaryList();
        BasicDBList bdbl = new BasicDBList();
        trackSummaryList.forEach(ts -> {
            double startLng = ts.getStartPosition().getCoordinate().y;
            double startLat = ts.getStartPosition().getCoordinate().x;
            double endLng = ts.getEndPosition().getCoordinate().y;
            double endLat = ts.getEndPosition().getCoordinate().x;
            DBObject bdbo = BasicDBObjectBuilder.start()
                    .add(ID, ts.getIdentifier())
                    .add(START_POSITION_LAT, startLat)
                    .add(START_POSITION_LNG, startLng)
                    .add(END_POSITION_LAT, endLat)
                    .add(END_POSITION_LNG, endLng).get();
            bdbl.add(bdbo);
        });
        return bdbl;
    }

    /**
     *  * FIX ME: encode & decode as defined in the Schemas: via geometry objects instead off double lat,lng, ...
     *
     * @param c
     * @param o
     * @param i
     *
     * @return
     *
     * @throws MappingException
     */
    @Override
    public TrackSummaries decode(Class<?> c, Object o, MappedField i) throws
            MappingException {

        if (o == null) {
            return null;
        }

        TrackSummaries tsm = new TrackSummaries();
        if (!(o instanceof BasicBSONList)) {
            return tsm;
        }
        GeometryFactory geomFac = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);

        BasicBSONList list = (BasicBSONList) o;

        for (Object value : list) {
            BasicDBObject trackSummary = (BasicDBObject) value;

            Object id = trackSummary.get(ID);
            Object startLat = trackSummary.get(START_POSITION_LAT);
            Object startLong = trackSummary.get(START_POSITION_LNG);
            Object endLat = trackSummary.get(END_POSITION_LAT);
            Object endLong = trackSummary.get(END_POSITION_LNG);

            if (!(id instanceof String) ||
                    !(startLat instanceof Number) ||
                    !(startLong instanceof Number) ||
                    !(endLat instanceof Number) ||
                    !(endLong instanceof Number)) {
                throw new MappingException("Can not decode " + id);
            }

            TrackSummary ts = new TrackSummary();
            ts.setIdentifier((String) id);

            Coordinate start = new Coordinate(((Number) startLat).doubleValue(),
                    ((Number) startLong).doubleValue());
            Coordinate end = new Coordinate(((Number) endLat).doubleValue(),
                    ((Number) endLong).doubleValue());
            ts.setStartPosition(geomFac.createPoint(start));
            ts.setEndPosition(geomFac.createPoint(end));

            tsm.addTrackSummary(ts);
        }
        return tsm;
    }
}
