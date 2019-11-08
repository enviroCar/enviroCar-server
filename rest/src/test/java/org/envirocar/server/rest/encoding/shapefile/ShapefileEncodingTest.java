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
package org.envirocar.server.rest.encoding.shapefile;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.entities.*;
import org.envirocar.server.core.exception.TrackTooLongException;
import org.envirocar.server.mongo.entity.*;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.rights.NonRestrictiveRights;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertTrue;

/**
 * TODO JavaDoc
 *
 * @author Benjamin Pross
 */
public class ShapefileEncodingTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();
    private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
    private final Track track = createTrack();
    private final Sensor sensor = createSensor();
    private final List<Phenomenon> phenomenons = createPhenomenoms();

    @Mock
    private DataService dataService;

    private TrackShapefileEncoder trackShapefileEncoder;

    @Before
    public void setup() {
        this.trackShapefileEncoder = new TrackShapefileEncoder(dataService);
        this.trackShapefileEncoder.setDateTimeFormat(ISODateTimeFormat.dateTimeNoMillis());
        this.trackShapefileEncoder.setRights(NonRestrictiveRights::new);
    }

    @Test
    public void testShapefileEncoding() {
        Measurements measurements = createTrackWithMeasurementsLessThanThreshold();
        Mockito.when(dataService.getMeasurements(Mockito.anyObject())).thenReturn(measurements);
        File shapeFile = trackShapefileEncoder.encodeShapefile(track, MediaTypes.APPLICATION_ZIPPED_SHP_TYPE);
        assertTrue(shapeFile.exists());
    }

    @Test
    public void testShapefileEncodingMoreMeasurementsThanAllowed() {
        exception.expect(TrackTooLongException.class);
        Measurements measurements = createTrackWithMeasurementsMoreThanThreshold();
        Mockito.when(dataService.getMeasurements(Mockito.anyObject())).thenReturn(measurements);
        trackShapefileEncoder.encodeShapefile(track, MediaTypes.APPLICATION_ZIPPED_SHP_TYPE);
    }

    private Measurements createTrackWithMeasurementsLessThanThreshold() {
        return Measurements.from(IntStream.range(0, TrackShapefileEncoder.shapeFileExportThreshold - 1)
                .mapToObj(this::createMeasurement)
                .collect(toList())).build();
    }

    private Measurements createTrackWithMeasurementsMoreThanThreshold() {
        return Measurements.from(IntStream.range(0, TrackShapefileEncoder.shapeFileExportThreshold)
                .mapToObj(this::createMeasurement)
                .collect(toList())).build();
    }

    private Measurement createMeasurement(int basenumber) {

        Measurement measurement = new MongoMeasurement();
        measurement.setGeometry(geometryFactory.createPoint(new Coordinate(51.9, 7)));
        measurement.setSensor(sensor);
        int value = basenumber;
        for (Phenomenon phenomenon : phenomenons) {
            MeasurementValue measurementValue = new MongoMeasurementValue();
            measurementValue.setPhenomenon(phenomenon);
            measurementValue.setValue(value);
            measurement.addValue(measurementValue);
            value++;
        }
        measurement.setTime(DateTime.now());
        dataService.createMeasurement(measurement);
        return measurement;

    }

    private Track createTrack() {
        MongoTrack result = new MongoTrack();
        result.setSensor(sensor);
        result.setCreationTime(DateTime.now());
        result.setModificationTime(DateTime.now());
        return result;
    }


    private List<Phenomenon> createPhenomenoms() {
        List<Phenomenon> phenomena = new ArrayList<>();
        phenomena.add(createPhenomenom("RPM", "u/min"));
        phenomena.add(createPhenomenom("Intake Temperature", "C"));
        phenomena.add(createPhenomenom("Speed", "km/h"));
        phenomena.add(createPhenomenom("MAF", "l/s"));
        return phenomena;
    }

    private Sensor createSensor() {
        MongoSensor sensor = new MongoSensor();
        sensor.setType("Car");
        sensor.setCreationTime(DateTime.now());
        sensor.setModificationTime(DateTime.now());
        return sensor;
    }

    private Phenomenon createPhenomenom(String name, String unit) {
        MongoPhenomenon phenomenon = new MongoPhenomenon();
        phenomenon.setName(name);
        phenomenon.setUnit(unit);
        phenomenon.setCreationTime(DateTime.now());
        phenomenon.setModificationTime(DateTime.now());
        return phenomenon;
    }
}
