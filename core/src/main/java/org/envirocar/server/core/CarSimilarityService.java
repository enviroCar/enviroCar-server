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

import java.util.Set;
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
    
    Sensor resolveMappedSensor(String id) throws ResourceNotFoundException;
    
    Set<String> getMappedSensorIds();
    
}
