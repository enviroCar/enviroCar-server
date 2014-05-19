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
package org.envirocar.server.rest.encoding.csv;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bson.types.ObjectId;
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.UserService;
import org.envirocar.server.core.entities.EntityFactory;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.ResourceAlreadyExistException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.core.guice.CoreModule;
import org.envirocar.server.core.guice.UpdaterModule;
import org.envirocar.server.core.guice.ValidatorModule;
import org.envirocar.server.mongo.entity.MongoSensor;
import org.envirocar.server.mongo.guice.MongoConnectionModule;
import org.envirocar.server.mongo.guice.MongoConverterModule;
import org.envirocar.server.mongo.guice.MongoMappedClassesModule;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.guice.JerseyCodingModule;
import org.envirocar.server.rest.schema.GuiceRunner;
import org.envirocar.server.rest.schema.Modules;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * TODO JavaDoc
 *
 * @author Benjamin Pross
 */
@RunWith(GuiceRunner.class)
@Modules({MongoConverterModule.class, CoreModule.class, JerseyCodingModule.class, MongoMappedClassesModule.class, MongoConnectionModule.class, CSVEncodingTestModule.class, UpdaterModule.class, ValidatorModule.class})
public class CSVEncodingTest {
	
	@Inject
	private DataService dataService;
	
	@Inject
	private UserService userService;
	
	@Inject
	private TrackCSVEncoder trackCSVEncoder;

	@Inject
	private EntityFactory entityFactory;
	
	@Inject
	private GeometryFactory geometryFacotry;
		
	@Test
	public void testCSVEncoding(){	
		try {
			
			Phenomenon f = entityFactory.createPhenomenon();
			
			f.setName("RPM");
			f.setUnit("u/min");
			
			dataService.createPhenomenon(f);
			
			f = entityFactory.createPhenomenon();
			
			f.setName("Intake Temperature");
			f.setUnit("C");
			
			dataService.createPhenomenon(f);
			
			f = entityFactory.createPhenomenon();
			
			f.setName("Speed");
			f.setUnit("km/h");
			
			dataService.createPhenomenon(f);
			
			f.setName("MAF");
			f.setUnit("l/s");
			
			dataService.createPhenomenon(f);
			
			f.setName("Engine Load");
			f.setUnit("%");
			
			dataService.createPhenomenon(f);
			
			f.setName("Throttle Position");
			f.setUnit("%");
			
			dataService.createPhenomenon(f);
			
			String testUserName = "TestUser";
			
			Sensor s = entityFactory.createSensor();
			
			s.setIdentifier("51bc53ab5064ba7f336ef920");
			
			MongoSensor ms = (MongoSensor) s;
			
			ms.setCreationTime(DateTime.now());
			ms.setModificationTime(DateTime.now());
			
			dataService.createSensor(ms);
			
			Measurement measurement = entityFactory.createMeasurement();
			
			measurement.setGeometry(geometryFacotry.createPoint(new Coordinate(1.1, 1.2)));
			
			measurement.setSensor(ms);
			
			User user = entityFactory.createUser();
			
			user.setName(testUserName);
			user.setMail("info@52north.org");
			user.setToken("pwd123");
			
			if(userService.getUser(testUserName) == null){			
				userService.createUser(user);			
			}
			
			measurement.setUser(user);
			
			String objectId = ObjectId.get().toString();
			
			measurement.setIdentifier(objectId);
			
			MeasurementValue measurementValue = entityFactory.createMeasurementValue();
			
			measurementValue.setPhenomenon(f);
			
			measurementValue.setValue(4.0);
			
			measurement.addValue(measurementValue);

			Track testTrack = entityFactory.createTrack();
			
			testTrack.setUser(user);
			
			measurement.setTrack(testTrack);
			
			dataService.createMeasurement(measurement);
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(trackCSVEncoder.encodeCSV(testTrack, MediaTypes.TEXT_CSV_TYPE)));
			
			String line = "";
			
			String content = "";
			
			while((line = bufferedReader.readLine()) != null){
				
				content = content.concat(line + "\n");
				
			}
			
			System.out.println(content);
			
			assertTrue(content.contains(objectId));
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (ResourceAlreadyExistException e) {
			e.printStackTrace();
		} 
	}
	
}
