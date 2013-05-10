/**
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.rest.resources;

import io.car.server.core.entities.Measurement;
import io.car.server.core.exception.MeasurementNotFoundException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.rest.AbstractResource;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.auth.Authenticated;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * 
 * @author Arne de Wall <a.dewall@52north.org>
 * 
 */
public class MeasurementResource extends AbstractResource {

	protected final Measurement measurement;

	@Inject
	public MeasurementResource(@Assisted Measurement measurement) {
		this.measurement = measurement;
	}

	@PUT
	@Consumes(MediaTypes.MEASUREMENT_MODIFY)
	@Authenticated
	public Response modify(Measurement changes)
			throws MeasurementNotFoundException, UserNotFoundException {

		if (!canModifyUser(getCurrentUser())) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		getService().modifyMeasurement(measurement, changes);
		return null;
	}
}
