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
package org.envirocar.server.rest.encoding;

import org.envirocar.server.core.DataService;
import org.envirocar.server.core.UserService;
import org.envirocar.server.core.entities.EntityFactory;
import org.envirocar.server.core.guice.UpdaterModule;
import org.envirocar.server.core.guice.ValidatorModule;
import org.envirocar.server.mongo.guice.MongoConnectionModule;
import org.envirocar.server.mongo.guice.MongoConverterModule;
import org.envirocar.server.mongo.guice.MongoMappedClassesModule;
import org.envirocar.server.rest.encoding.csv.TrackCSVEncoder;
import org.envirocar.server.rest.encoding.shapefile.TrackShapefileEncoder;
import org.envirocar.server.rest.guice.JerseyCodingModule;
import org.envirocar.server.rest.schema.GuiceRunner;
import org.envirocar.server.rest.schema.Modules;
import org.junit.runner.RunWith;

import com.github.jmkgreen.morphia.logging.MorphiaLoggerFactory;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.GeometryFactory;

@Modules({MongoConverterModule.class, JerseyCodingModule.class, MongoMappedClassesModule.class,
	MongoConnectionModule.class, EncodingTestModule.class,
	UpdaterModule.class, ValidatorModule.class })
@RunWith(GuiceRunner.class)
public abstract class AbstractEncodingTest {

	@Inject
	protected DataService dataService;

	@Inject
	protected UserService userService;

	@Inject
	protected TrackCSVEncoder trackCSVEncoder;

	@Inject
	protected TrackShapefileEncoder trackShapefileEncoder;

	@Inject
	protected EntityFactory entityFactory;

	@Inject
	protected GeometryFactory geometryFactory;
	
	public AbstractEncodingTest(){
		MorphiaLoggerFactory.reset();
	}

}
