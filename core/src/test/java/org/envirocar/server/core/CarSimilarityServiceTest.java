/*
 * Copyright (C) 2013-2021 The enviroCar project
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

import java.util.*;

import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.core.filter.SensorFilter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 */
public class CarSimilarityServiceTest {
    private Sensor oldSensor;
    
    @Test
    public void testEquivalent() throws ResourceNotFoundException {
        Sensor newSensor = Mockito.mock(Sensor.class);
        Map<String, Object> props = new HashMap<>();
        props.put("fuelType", "diesel");
        props.put("constructionYear", 2015);
        props.put("engineDisplacement", 1234);
        props.put("model", "Dito");
        props.put("manufacturer", "Vito");
        Mockito.when(newSensor.getProperties()).thenReturn(props);
        
        SensorDao dao = createDao();
        
        CarSimilarityServiceImpl service = new CarSimilarityServiceImpl(dao);
        CarSimilarityServiceImpl serviceMock = Mockito.spy(service);
        serviceMock.setSimilarityDefinition("/car-similarity-test.json");
        
        Sensor equi = serviceMock.resolveEquivalent(newSensor);
        assertThat(equi, Matchers.is(oldSensor));
        
        props.put("manufacturer", "vito mobile  ");
        
        Sensor similar = serviceMock.resolveEquivalent(newSensor);
        assertThat(similar, Matchers.is(oldSensor));
        
        Mockito.verify(serviceMock, Mockito.times(1)).isManufacturerSimilar(Mockito.any(String.class), Mockito.any(String.class));
    }
    
    @Test(expected = ResourceNotFoundException.class)
    public void testNoEquivalent() throws ResourceNotFoundException {
        Sensor newSensor = Mockito.mock(Sensor.class);
        Map<String, Object> props = new HashMap<>();
        props.put("fuelType", "diesel");
        props.put("constructionYear", 2015);
        props.put("engineDisplacement", 1234);
        props.put("model", "Wagon");
        props.put("manufacturer", "Odd");
        Mockito.when(newSensor.getProperties()).thenReturn(props);
        
        SensorDao dao = createDao();
        
        CarSimilarityServiceImpl service = new CarSimilarityServiceImpl(dao);
        CarSimilarityServiceImpl serviceMock = Mockito.spy(service);
        serviceMock.setSimilarityDefinition("/car-similarity-test.json");
        
        serviceMock.resolveEquivalent(newSensor);
    }
    
    private SensorDao createDao() {
        this.oldSensor = Mockito.mock(Sensor.class);
        Map<String, Object> props = new HashMap<>();
        props.put("fuelType", "diesel");
        props.put("constructionYear", 2015);
        props.put("engineDisplacement", 1234);
        props.put("model", "DiTO ");
        props.put("manufacturer", "vITO ");
        String id = "51ffab4fe4b058cd3d654006";
        
        Mockito.when(oldSensor.getIdentifier()).thenReturn(id);
        Mockito.when(oldSensor.getProperties()).thenReturn(props);
        
        SensorDao dao = Mockito.mock(SensorDao.class);
        Mockito.when(dao.get(Mockito.any(SensorFilter.class))).thenReturn(
                Sensors.from(Collections.singletonList(oldSensor)).build());
        Mockito.when(dao.getByIdentifier(id)).thenReturn(oldSensor);
        
        return dao;
    }
    
    @Test
    public void testStaticMapping() throws ResourceNotFoundException {
        SensorDao dao = createDao();
        
        CarSimilarityServiceImpl service = new CarSimilarityServiceImpl(dao);
        service.setSimilarityDefinition("/car-similarity-test.json");
        
        Sensor resolved = service.resolveMappedSensor("51ffab4fe4b058cd3d654007");
        assertThat(resolved, Matchers.is(oldSensor));
    }
    
    @Test(expected = ResourceNotFoundException.class)
    public void testNoMapping() throws ResourceNotFoundException {
        SensorDao dao = createDao();
        
        CarSimilarityServiceImpl service = new CarSimilarityServiceImpl(dao);
        service.setSimilarityDefinition("/car-similarity-test.json");
        
        service.resolveMappedSensor("i-am-no-id");
    }
    
    @Test
    public void testMappingIdsCollection() {
        String id = "51ffab4fe4b058cd3d654007";
        
        CarSimilarityServiceImpl service = new CarSimilarityServiceImpl(Mockito.mock(SensorDao.class));
        service.setSimilarityDefinition("/car-similarity-test.json");
        
        Collection<String> mapped = service.getMappedSensorIds();

        assertThat(mapped.size(), Matchers.is(1));
        assertThat(mapped.iterator().next(), Matchers.is(id));
    }

}
