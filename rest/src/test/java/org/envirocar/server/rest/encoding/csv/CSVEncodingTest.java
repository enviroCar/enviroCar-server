/*
 * Copyright (C) 2013-2019 The enviroCar project
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
package org.envirocar.server.rest.encoding.csv;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.entities.*;
import org.envirocar.server.mongo.entity.*;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.rights.NonRestrictiveRights;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * TODO JavaDoc
 *
 * @author Benjamin Pross
 */
public class CSVEncodingTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Rule
    public final ErrorCollector errors = new ErrorCollector();
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();
    private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
    @Mock
    private DataService dataService;
    private TrackCSVEncoder trackCSVEncoder;
    private final Track track = createTrack();
    private final Sensor sensor = createSensor();
    private final List<Phenomenon> phenomenons = createPhenomenons();


    @Before
    public void setup() {
        this.trackCSVEncoder = new TrackCSVEncoder(this.dataService);
        this.trackCSVEncoder.setDateTimeFormat(ISODateTimeFormat.dateTimeNoMillis());
        this.trackCSVEncoder.setRights(NonRestrictiveRights::new);
    }

    @Test
    public void testCSVEncodingAllMeasurementsHaveAllPhenomenons()
            throws IOException {

        Measurements measurements = createTrackWithMeasurements_AllMeasurementsHaveAllPhenomenons();
        Mockito.when(dataService.getMeasurements(Mockito.anyObject())).thenReturn(measurements);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(trackCSVEncoder.encodeCSV(track, MediaTypes.TEXT_CSV_TYPE)));

        String line;

        String[] propertyNames = new String[0];
        String[] propertyValues1 = new String[0];
        String[] propertyValues2 = new String[0];

        for (int i = 0; i < 3; i++) {
            line = bufferedReader.readLine();
            if (line != null) {
                switch (i) {
                    case 0:
                        propertyNames = line.split(";");
                        break;
                    case 1:
                        propertyValues1 = line.split(";");
                        break;
                    case 2:
                        propertyValues2 = line.split(";");
                        break;

                    default:
                        break;
                }
            }
        }

        Iterator<Measurement> iterator = measurements.iterator();

        Map<String, String> expectedValues = new HashMap<>();

        expectedValues.put("id", iterator.next().getIdentifier());
        expectedValues.put("RPM(u/min)", "1");
        expectedValues.put("Speed(km/h)", "3");
        expectedValues.put("Intake Temperature(C)", "2");
        expectedValues.put("MAF(l/s)", "4");
        expectedValues.put("longitude", "1.1");
        expectedValues.put("latitude", "1.2");

        checkMeasurementValues(propertyNames, propertyValues1, expectedValues);

        expectedValues = new HashMap<>();

        expectedValues.put("id", iterator.next().getIdentifier());
        expectedValues.put("RPM(u/min)", "2");
        expectedValues.put("Speed(km/h)", "4");
        expectedValues.put("Intake Temperature(C)", "3");
        expectedValues.put("MAF(l/s)", "5");
        expectedValues.put("longitude", "2.1");
        expectedValues.put("latitude", "2.2");

        checkMeasurementValues(propertyNames, propertyValues2, expectedValues);

    }

    @Test
    public void testCSVEncodingFirstMeasurementHasLessPhenomenons()
            throws IOException {

        Measurements measurements = createTrackWithMeasurements_FirstMeasurementHasLessPhenomenons();
        Mockito.when(dataService.getMeasurements(Mockito.anyObject())).thenReturn(measurements);

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(trackCSVEncoder.encodeCSV(track,
                        MediaTypes.TEXT_CSV_TYPE)));

        String line;

        String[] propertyNames = new String[0];
        String[] line1 = new String[0];
        String[] line2 = new String[0];

        for (int i = 0; i < 3; i++) {
            line = bufferedReader.readLine();
            if (line != null) {
                switch (i) {
                    case 0:
                        propertyNames = line.split(";");
                        break;
                    case 1:
                        line1 = line.split(";");
                        break;
                    case 2:
                        line2 = line.split(";");
                        break;

                    default:
                        break;
                }
            }

        }

        Iterator<Measurement> iterator = measurements.iterator();

        Map<String, String> expectedValues = new HashMap<>();

        expectedValues.put("id", iterator.next().getIdentifier());
        expectedValues.put("RPM(u/min)", "1");
        expectedValues.put("Speed(km/h)", "3");
        expectedValues.put("Intake Temperature(C)", "2");
        expectedValues.put("MAF(l/s)", "");
        expectedValues.put("longitude", "1.1");
        expectedValues.put("latitude", "1.2");

        checkMeasurementValues(propertyNames, line1, expectedValues);

        expectedValues = new HashMap<>();

        Measurement next = iterator.next();
        expectedValues.put("id", next.getIdentifier());
        expectedValues.put("RPM(u/min)", "2");
        expectedValues.put("Speed(km/h)", "4");
        expectedValues.put("Intake Temperature(C)", "3");
        expectedValues.put("MAF(l/s)", "5");
        expectedValues.put("longitude", "2.1");
        expectedValues.put("latitude", "2.2");

        checkMeasurementValues(propertyNames, line2, expectedValues);

    }

    @Test
    public void testCSVEncodingFirstMeasurementHasMorePhenomenons() throws IOException {
        Measurements measurements = createTrackWithMeasurements_FirstMeasurementHasMorePhenomenons();
        Mockito.when(dataService.getMeasurements(Mockito.anyObject())).thenReturn(measurements);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(trackCSVEncoder.encodeCSV(track, MediaTypes.TEXT_CSV_TYPE)));

        String line;

        String[] header = new String[0];
        String[] line1 = new String[0];
        String[] line2 = new String[0];

        for (int i = 0; i < 3; i++) {
            line = bufferedReader.readLine();
            if (line != null) {
                switch (i) {
                    case 0:
                        header = line.split(";");
                        break;
                    case 1:
                        line1 = line.split(";");
                        break;
                    case 2:
                        line2 = line.split(";");
                        break;

                    default:
                        break;
                }
            }

        }


        Iterator<Measurement> iterator = measurements.iterator();

        Map<String, String> expectedValues = new HashMap<>();

        expectedValues.put("id", iterator.next().getIdentifier());
        expectedValues.put("RPM(u/min)", "1");
        expectedValues.put("Speed(km/h)", "3");
        expectedValues.put("Intake Temperature(C)", "2");
        expectedValues.put("MAF(l/s)", "4");
        expectedValues.put("longitude", "1.1");
        expectedValues.put("latitude", "1.2");

        checkMeasurementValues(header, line1, expectedValues);

        expectedValues = new HashMap<>();

        expectedValues.put("id", iterator.next().getIdentifier());
        expectedValues.put("RPM(u/min)", "2");
        expectedValues.put("Speed(km/h)", "4");
        expectedValues.put("Intake Temperature(C)", "3");
        expectedValues.put("MAF(l/s)", "");
        expectedValues.put("longitude", "2.1");
        expectedValues.put("latitude", "2.2");

        checkMeasurementValues(header, line2, expectedValues);

    }

    private Measurements createTrackWithMeasurements_AllMeasurementsHaveAllPhenomenons() {
        return Measurements.from(Arrays.asList(
                createMeasurement(this.phenomenons, 1),
                createMeasurement(this.phenomenons, 2)
        )).build();

    }

    private Measurements createTrackWithMeasurements_FirstMeasurementHasLessPhenomenons() {
        List<Phenomenon> lessPhenomena = new ArrayList<>(this.phenomenons);
        lessPhenomena.remove(lessPhenomena.size() - 1);
        return Measurements.from(Arrays.asList(
                createMeasurement(lessPhenomena, 1),
                createMeasurement(this.phenomenons, 2)
        )).build();
    }

    private Measurements createTrackWithMeasurements_FirstMeasurementHasMorePhenomenons() {
        List<Phenomenon> lessPhenomena = new ArrayList<>(this.phenomenons);
        lessPhenomena.remove(lessPhenomena.size() - 1);
        return Measurements.from(Arrays.asList(
                createMeasurement(this.phenomenons, 1),
                createMeasurement(lessPhenomena, 2)
        )).build();
    }

    private Measurement createMeasurement(List<Phenomenon> phenomena, int basenumber) {
        MongoMeasurement measurement = new MongoMeasurement();
        measurement.setGeometry(geometryFactory.createPoint(new Coordinate(basenumber + 0.1, basenumber + 0.2)));
        measurement.setSensor(sensor);
        int value = basenumber;
        for (Phenomenon phenomenon : phenomena) {
            MeasurementValue measurementValue = new MongoMeasurementValue();
            measurementValue.setPhenomenon(phenomenon);
            measurementValue.setValue(value);
            measurement.addValue(measurementValue);
            value++;
        }
        measurement.setTime(DateTime.now());
        measurement.setCreationTime(DateTime.now());
        measurement.setModificationTime(DateTime.now());
        return measurement;

    }

    private Track createTrack() {
        MongoTrack track = new MongoTrack();
        track.setSensor(sensor);
        track.setCreationTime(DateTime.now());
        track.setModificationTime(DateTime.now());
        return track;
    }

    private Sensor createSensor() {
        MongoSensor sensor = new MongoSensor();
        sensor.setType("car");
        sensor.setCreationTime(DateTime.now());
        sensor.setModificationTime(DateTime.now());
        return sensor;
    }

    private List<Phenomenon> createPhenomenons() {
        List<Phenomenon> phenomena = new ArrayList<>();
        phenomena.add(createPhenomenon("RPM", "u/min"));
        phenomena.add(createPhenomenon("Intake Temperature", "C"));
        phenomena.add(createPhenomenon("Speed", "km/h"));
        phenomena.add(createPhenomenon("MAF", "l/s"));
        return phenomena;
    }

    private Phenomenon createPhenomenon(String name, String unit) {
        MongoPhenomenon phenomenon = new MongoPhenomenon();
        phenomenon.setName(name);
        phenomenon.setUnit(unit);
        phenomenon.setCreationTime(DateTime.now());
        phenomenon.setModificationTime(DateTime.now());
        return phenomenon;
    }

    private void checkMeasurementValues(String[] propertyNames, String[] propertyValues, Map<String, String> expectedValues) {
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = propertyNames[i].trim();
            if (!propertyName.equals("time")) {
                errors.checkThat(
                        String.format("Value for %s does not match expected.", propertyName),
                        propertyValues[i].trim(), is(equalTo(expectedValues.get(propertyName))));
            }

        }
    }

}
