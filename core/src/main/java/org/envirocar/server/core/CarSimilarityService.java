package org.envirocar.server.core;

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.exception.ResourceNotFoundException;

/**
 *
 */
public interface CarSimilarityService {

    /**
     * resolve an existing sensor that matches
     * the properties of the provided sensor
     * 
     * @param s the sensor
     * @return the sensor that is the equivalent
     * @throws org.envirocar.server.core.exception.SensorNotFoundException if
     * no equivalent sensor could be found
     */
    Sensor resolveEquivalent(Sensor s) throws ResourceNotFoundException;
    
}
