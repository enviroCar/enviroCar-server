package org.envirocar.server.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.envirocar.server.core.dao.SensorDao;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.core.filter.SensorFilter;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

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
        serviceMock.loadSimilarityDefinition("/car-similarity-test.json");
        
        Sensor equi = serviceMock.resolveEquivalent(newSensor);
        Assert.assertThat(equi, Matchers.is(oldSensor));
        
        props.put("manufacturer", "vito mobile  ");
        
        Sensor similar = serviceMock.resolveEquivalent(newSensor);
        Assert.assertThat(similar, Matchers.is(oldSensor));
        
        Mockito.verify(serviceMock, Mockito.times(1)).isManufacturerSimilar(Mockito.any(String.class), Mockito.any(String.class));
    }
    
    private SensorDao createDao() {
        this.oldSensor = Mockito.mock(Sensor.class);
        Map<String, Object> props = new HashMap<>();
        props.put("fuelType", "diesel");
        props.put("constructionYear", 2015);
        props.put("engineDisplacement", 1234);
        props.put("model", "DiTO ");
        props.put("manufacturer", "vITO ");
        Mockito.when(oldSensor.getProperties()).thenReturn(props);
        
        SensorDao dao = Mockito.mock(SensorDao.class);
        Mockito.when(dao.get(Mockito.any(SensorFilter.class))).thenReturn(
                Sensors.from(Arrays.asList(new Sensor[] {oldSensor})).build());
        
        return dao;
    }

}
