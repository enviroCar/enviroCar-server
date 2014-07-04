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
package org.envirocar.server.rest.encoding.shapefile;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.ResourceAlreadyExistException;
import org.envirocar.server.core.exception.TrackTooLongException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.mongo.entity.MongoSensor;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.encoding.AbstractEncodingTest;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * TODO JavaDoc
 *
 * @author Benjamin Pross
 */
public class ShapefileEncodingTest extends AbstractEncodingTest{

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private String dateTime = "2014-05-20T08:42:06Z";

	private Track testTrack1;
	private Track testTrack2;

	private User user;
	private Sensor sensor;
	private List<Phenomenon> phenomenons;

	private String testUserName = "TestUser";
	
	private int measurementThreshold = 500;//temp value
	
	@Before
	public void setup() {
		
		try {

			measurementThreshold = TrackShapefileEncoder.shapeFileExportThreshold;
			
//			testTrack1 = getTestTrack(trackObjectId1);
//			testTrack2 = getTestTrack(trackObjectId2);

			if (testTrack1 == null) {

				testTrack1 = createTrack(getUser(), getSensor());

				createTrackWithMeasurementsLessThanThreshold(
						testTrack1, getPhenomenons(), getUser(), getSensor());

			}
			if(testTrack2 == null){

				testTrack2 = createTrack(getUser(), getSensor());
				
				createTrackWithMeasurementsMoreThanThreshold(
						testTrack2, getPhenomenons(), getUser(), getSensor());
			}

		} catch (ValidationException e) {
			e.printStackTrace();
		}

	}
	
	@After
	public void removeTracks() {
		if (testTrack1 != null) {
			dataService.deleteTrack(testTrack1);
		}
		
		if (testTrack2 != null) {
			dataService.deleteTrack(testTrack2);
		}
	}

	@Test
	public void testShapefileEncoding()
			throws IOException {

		File shapeFile;
		try {
			shapeFile = trackShapefileEncoder.encodeShapefile(testTrack1,
							MediaTypes.APPLICATION_ZIPPED_SHP_TYPE);
			assertTrue(shapeFile.exists());
		} catch (TrackTooLongException e) {
			e.printStackTrace();
			fail();
		} 

	}
	
	@Test
	public void testShapefileEncodingMoreMeasurementsThanAllowed()
			throws IOException {
		exception.expect(TrackTooLongException.class);
		trackShapefileEncoder.encodeShapefile(testTrack2,
							MediaTypes.APPLICATION_ZIPPED_SHP_TYPE);	
	}
	
	private User getUser(){
		if(user == null){
			user =  createUser(testUserName);
		}
		return user;
	}
	
	private Sensor getSensor(){
		
		if(sensor == null){
			sensor = createSensor();
		}
		return sensor;
	}
	
	private List<Phenomenon> getPhenomenons(){
		
		if(phenomenons == null){
			phenomenons = createPhenomenoms();
		}		
		return phenomenons;		
	}


	private void createTrackWithMeasurementsLessThanThreshold(
			Track track, List<Phenomenon> phenomenons, User user, Sensor sensor) {

		for (int i = 0; i < measurementThreshold - 1; i++) {			
			createMeasurement(track, new ObjectId().toString(), phenomenons, user,
					sensor, i);
		}

		dataService.createTrack(track);

	}

	private void createTrackWithMeasurementsMoreThanThreshold(			
			Track track, List<Phenomenon> phenomenons, User user, Sensor sensor) {

		for (int i = 0; i <= measurementThreshold + 1; i++) {			
			createMeasurement(track, new ObjectId().toString(), phenomenons, user,
					sensor, i);
		}

		dataService.createTrack(track);
	}

	private Measurement createMeasurement(Track testTrack, String objectId,
			List<Phenomenon> phenomenons, User user, Sensor sensor,
			int basenumber) {

		Measurement measurement = entityFactory.createMeasurement();

		measurement.setGeometry(geometryFactory.createPoint(new Coordinate(
				51.9, 7)));

		measurement.setSensor(sensor);

		measurement.setUser(user);

		measurement.setIdentifier(objectId);

		int value = basenumber;

		for (Phenomenon phenomenon : phenomenons) {

			MeasurementValue measurementValue = entityFactory
					.createMeasurementValue();

			measurementValue.setPhenomenon(phenomenon);

			measurementValue.setValue(value);

			measurement.addValue(measurementValue);

			value++;
		}

		measurement.setTime(DateTime.now());
		
		measurement.setTrack(testTrack);

		dataService.createMeasurement(measurement);

		return measurement;

	}

	private Track createTrack(User user, Sensor sensor) {

		Track result = entityFactory.createTrack();

//		result.setIdentifier(objectId);

		result.setUser(user);

		result.setSensor(sensor);

		return result;
	}

	private Sensor createSensor() {
		Sensor s = entityFactory.createSensor();

		s.setIdentifier("51bc53ab5064ba7f336ef920");

		s.setType("Car");
		
		MongoSensor ms = (MongoSensor) s;

		ms.setCreationTime(DateTime.parse(dateTime));
		ms.setModificationTime(DateTime.parse(dateTime));

		dataService.createSensor(ms);
		return s;
	}

	private List<Phenomenon> createPhenomenoms() {

		List<Phenomenon> result = new ArrayList<Phenomenon>();

		result.add(createPhenomenom("RPM", "u/min"));
		result.add(createPhenomenom("Intake Temperature", "C"));
		result.add(createPhenomenom("Speed", "km/h"));
		result.add(createPhenomenom("MAF", "l/s"));

		return result;
	}

	private Phenomenon createPhenomenom(String name, String unit) {

		Phenomenon f = entityFactory.createPhenomenon();

		f.setName(name);
		f.setUnit(unit);

		dataService.createPhenomenon(f);

		return f;
	}

	private User createUser(String testUserName) {

		User user = entityFactory.createUser();

		user.setName(testUserName);
		user.setMail("info@52north.org");
		user.setToken("pwd123");

		try {
			if (userService.getUser(testUserName) == null) {
				userService.createUser(user);
			}
		} catch (UserNotFoundException e) {
			/*
			 * ignore this one and try to create user
			 */
			try {
				userService.createUser(user);
			} catch (ValidationException e1) {
				e1.printStackTrace();
			} catch (ResourceAlreadyExistException e1) {
				e1.printStackTrace();
			}
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (ResourceAlreadyExistException e) {
			e.printStackTrace();
		}
		return user;

	}

}
