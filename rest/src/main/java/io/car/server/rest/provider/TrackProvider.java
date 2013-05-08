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
package io.car.server.rest.provider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.format.DateTimeFormatter;

import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;

import io.car.server.core.EntityFactory;
import io.car.server.core.db.SensorDao;
import io.car.server.core.entities.Track;
import io.car.server.rest.MediaTypes;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrackProvider extends AbstractJsonEntityProvider<Track> {
    @Inject
    private DateTimeFormatter formatter;
	@Inject
    private EntityFactory factory;
    @Inject
    private SensorProvider sensorProvider;
    @Inject
    private SensorDao sensorDao;

	public TrackProvider() {
		super(Track.class, MediaTypes.TRACK_TYPE, MediaTypes.TRACK_CREATE_TYPE,
				MediaTypes.TRACK_MODIFY_TYPE);
	}

	@Override
	public Track read(JSONObject j, MediaType mediaType) throws JSONException {
		JSONArray bbox = j.getJSONArray(JSONConstants.BBOX_KEY);
		return factory
				.createTrack()
				.setBbox(bbox.getDouble(0), bbox.getDouble(1),
						bbox.getDouble(2), bbox.getDouble(3))
                .setSensor(sensorDao.getByName(j.optString(JSONConstants.SENSOR_KEY)));
	}

	@Override
	public JSONObject write(Track t, MediaType mediaType) throws JSONException {
		Coordinate[] coords = t.getBbox().getCoordinates();
        JSONArray bbox = new JSONArray()
                .put(coords[0].x).put(coords[0].y)
                .put(coords[1].x).put(coords[1].y);
        //TODO include bounding box
        return new JSONObject()
                .put(JSONConstants.CREATED_KEY, formatter.print(t.getCreationDate()))
                .put(JSONConstants.MODIFIED_KEY, formatter.print(t.getLastModificationDate()))
                .put(JSONConstants.SENSOR_KEY, sensorProvider.write(t.getSensor(), mediaType))
                .put(JSONConstants.BBOX_KEY, bbox);
	}
}