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
package org.envirocar.server.core.validation;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;

import org.envirocar.server.core.exception.ValidationException;

/**
 * TODO JavaDoc
 *
 * @author Arne de Wall <a.dewall@52north.org>
 */
public class MeasurementValidator extends AbstractValidator<Measurement> {
    @Override
    public void validateCreate(Measurement t) throws ValidationException {
        isNotNull("location", t.getGeometry());
        isNotNull("user", t.getUser());
        isNotNull("sensor", t.getSensor());
        isNull("created", t.getCreationTime());
        isNull("modified", t.getModificationTime());
        for (MeasurementValue mv : t.getValues()) {
            isNotNull("phenomenon", mv.getPhenomenon());
            isNotNull("value", mv.getValue());
        }
    }

    @Override
    public void validateUpdate(Measurement t) throws ValidationException {
        isNull("created", t.getCreationTime());
        isNull("modified", t.getModificationTime());
        isNull("location", t.getGeometry());
        isNull("user", t.getUser());
        isNull("sensor", t.getSensor());
        for (MeasurementValue mv : t.getValues()) {
            isNotNull("phenomenon", mv.getPhenomenon());
            isNotNull("value", mv.getValue());
        }
    }
}
