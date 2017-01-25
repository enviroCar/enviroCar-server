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

import org.envirocar.server.core.entities.DimensionedNumber;

import com.github.jmkgreen.morphia.converters.SimpleValueConverter;
import com.github.jmkgreen.morphia.converters.TypeConverter;
import com.github.jmkgreen.morphia.mapping.MappedField;
import com.github.jmkgreen.morphia.mapping.MappingException;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.List;
import org.envirocar.server.core.entities.TrackSummaries;
import org.envirocar.server.core.entities.TrackSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MongoDB type converter for {@link DimensionedNumber}s.
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
public class TrackSummariesConverter
        extends TypeConverter implements SimpleValueConverter {

    private static final Logger log = LoggerFactory.getLogger(TrackSummariesConverter.class);

    public TrackSummariesConverter() {
        super(TrackSummaries.class);
    }

    /**
     * FIX ME: encode & decode as defined in the Schemas: via geometry objects
     * instead off double lat,lng, ...
     * @param trackSummaries
     * @param optionalExtraInfo
     * @return 
     */
    @Override
    public Object encode(Object trackSummaries, MappedField optionalExtraInfo) {
        if (trackSummaries == null) {
            return null;
        }
        TrackSummaries dn = (TrackSummaries) trackSummaries;
        List<TrackSummary> trackSummaryList = dn.getTrackSummaryList();
        BasicDBList bdbl  = new BasicDBList();
        for (int i = 0; i < trackSummaryList.size(); i++){
            TrackSummary ts = trackSummaryList.get(i);
            double startLng = ts.getStartPosition().getCoordinate().x;
            double startLat = ts.getStartPosition().getCoordinate().y;
            double endLng = ts.getEndPosition().getCoordinate().x;
            double endLat = ts.getEndPosition().getCoordinate().y;
            
            Object bdbo = BasicDBObjectBuilder.start()
                    .add("id", ts.getIdentifier())
                    .add("startPositionLat", startLat)
                    .add("startPositionLng", startLng)
                    .add("endPositionLat", endLat)
                    .add("endPositionLng", endLng).get();
            bdbl.add(bdbo);
        }
        return bdbl;
    }

    /**
     *  * FIX ME: encode & decode as defined in the Schemas: via geometry objects
     * instead off double lat,lng, ...
     * @param c
     * @param o
     * @param i
     * @return
     * @throws MappingException 
     */
    @Override
    public TrackSummaries decode(Class c, Object o, MappedField i) throws
            MappingException {
        TrackSummaries tsm = new TrackSummaries();
        if (o == null) {
            return null;
        } else if (o instanceof DBObject) {
            DBObject dbObject = (DBObject) o;
            BasicDBList trackSummaryList = (BasicDBList) dbObject;
            for (int j = 0; j < trackSummaryList.size(); j++) {
                BasicDBObject trackSummary = (BasicDBObject) trackSummaryList.get(j);

                Object b = trackSummary.get("id");
                String id;
                if (b instanceof String) {
                    id = b.toString();
                } else {
                    throw new MappingException("Can not decode " + b);
                }
                Object sPlat
                        = trackSummary.get("startPositionLat");
                double value;
                if (sPlat instanceof Number) {
                    value = ((Number) sPlat).doubleValue();
                } else {
                    throw new MappingException("Can not decode " + b);
                }
                Object sPlng
                        = trackSummary.get("startPositionLng");
                double value2;
                if (sPlng instanceof Number) {
                    value2 = ((Number) sPlng).doubleValue();
                } else {
                    throw new MappingException("Can not decode " + b);
                }
                Object ePlat
                        = trackSummary.get("endPositionLat");
                double value3;
                if (ePlat instanceof Number) {
                    value3 = ((Number) ePlat).doubleValue();
                } else {
                    throw new MappingException(
                            "Can not decode " + b);
                }
                Object ePlng
                        = trackSummary.get("endPositionLng");
                double value4;
                if (ePlng instanceof Number) {
                    value4 = ((Number) ePlng).doubleValue();
                } else {
                    throw new MappingException(
                            "Can not decode " + b);
                }

                TrackSummary ts = new TrackSummary();
                ts.setIdentifier(id);

                GeometryFactory geomFac = new GeometryFactory();
                Coordinate startPt = new Coordinate(value, value2);
                Coordinate endPt = new Coordinate(value3, value4);
                Geometry startPos
                        = geomFac.createPoint(startPt);
                Geometry endPos
                        = geomFac.createPoint(endPt);
                ts.setStartPosition(startPos);
                ts.setEndPosition(endPos);

                tsm.addTrackSummary(ts);
            }

            return tsm;
        }// else {
           // throw new MappingException("Can not decode " + o);
        //}
        return tsm;
    }
}
