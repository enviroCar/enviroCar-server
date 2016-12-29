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
package org.envirocar.server.core;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValues;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.core.filter.SensorFilter;
import org.envirocar.server.core.util.GeodesicGeometryOperations;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 *
 */
public class UserStatisticsTest {

    private GeodesicGeometryOperations ggo = new GeodesicGeometryOperations();
    private GeometryFactory gf = new GeometryFactory();

    @Mock
    private User user1;
    @Mock
    private Track track1;
    @Mock
    private Measurement m1;
    @Mock
    private Measurement m2;
    @Mock
    private Measurement m3;

    @Before
    public void initialize() {
        
        MockitoAnnotations.initMocks(this);
        // create some users
        Mockito.when(user1.getName()).thenReturn("Bert");
        Mockito.when(user1.getToken()).thenReturn("password");

        // create some tracks
        Mockito.when(track1.getIdentifier()).thenReturn("58178eb3e4b01fb1c09u73c8");
        Mockito.when(track1.getUser()).thenReturn(user1);

        // create measurements
        DateTime first = new DateTime();
        first.minusSeconds(60);
        DateTime second = new DateTime();
        second.minusSeconds(45);
        DateTime third = new DateTime();
        third.minusSeconds(30);
        DateTime fourth = new DateTime();
        fourth.minusSeconds(20);
        Mockito.when(m1.getUser()).thenReturn(user1);
        Mockito.when(m1.getTrack()).thenReturn(track1);
        Mockito.when(m1.getGeometry()).thenReturn(gf.createPoint(new Coordinate(0.0, 0.1)));
        Mockito.when(m1.getTime()).thenReturn(first);
        
        /**
        MeasurementValues mv1 = null;
        
        Mockito.when(m1.getValues()).thenReturn(mv1);
        * */
    }

}
