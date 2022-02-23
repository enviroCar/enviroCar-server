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
package org.envirocar.server.rest.encoding.shapefile;

import org.envirocar.server.core.DataService;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.mongo.entity.MongoMeasurement;
import org.envirocar.server.mongo.entity.MongoMeasurementValue;
import org.envirocar.server.mongo.entity.MongoPhenomenon;
import org.envirocar.server.mongo.entity.MongoSensor;
import org.envirocar.server.mongo.entity.MongoTrack;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.rights.NonRestrictiveRights;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public final MockitoRule mockitoRule = MockitoJUnit.rule();
    private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
    private final Track track = createTrack();
    private final Sensor sensor = createSensor();
    private final List<Phenomenon> phenomenons = createPhenomenons();

    @Mock
    private DataService dataService;

    private TrackShapefileEncoder trackShapefileEncoder;

    @Before
    public void setup() {
        this.trackShapefileEncoder = new TrackShapefileEncoder(dataService, ISODateTimeFormat.dateTimeNoMillis());
        this.trackShapefileEncoder.setRights(NonRestrictiveRights::new);
    }

    @Test
    public void testShapefileEncoding() throws IOException {
        Measurements measurements = createTrackWithMeasurementsLessThanThreshold();
        Mockito.when(dataService.getMeasurements(Mockito.anyObject())).thenReturn(measurements);
        Path shapeFile = trackShapefileEncoder.encodeShapefile(track, MediaTypes.APPLICATION_ZIPPED_SHP_TYPE);
        assertTrue(Files.exists(shapeFile));
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

    private Measurement createMeasurement(int baseNumber) {

        Measurement measurement = new MongoMeasurement();
        measurement.setGeometry(geometryFactory.createPoint(new Coordinate(51.9, 7)));
        measurement.setSensor(sensor);
        int value = baseNumber;
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

    private List<Phenomenon> createPhenomenons() {
        List<Phenomenon> phenomena = new ArrayList<>();
        phenomena.add(createPhenomenon("RPM", "u/min"));
        phenomena.add(createPhenomenon("Intake Temperature", "C"));
        phenomena.add(createPhenomenon("Speed", "km/h"));
        phenomena.add(createPhenomenon("MAF", "l/s"));
        return phenomena;
    }

    private Sensor createSensor() {
        MongoSensor sensor = new MongoSensor();
        sensor.setType("Car");
        sensor.setCreationTime(DateTime.now());
        sensor.setModificationTime(DateTime.now());
        return sensor;
    }

    private Phenomenon createPhenomenon(String name, String unit) {
        MongoPhenomenon phenomenon = new MongoPhenomenon();
        phenomenon.setName(name);
        phenomenon.setUnit(unit);
        phenomenon.setCreationTime(DateTime.now());
        phenomenon.setModificationTime(DateTime.now());
        return phenomenon;
    }
}
