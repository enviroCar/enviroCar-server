package io.car.server.rest.provider;

import io.car.server.core.EntityFactory;
import io.car.server.core.Measurement;
import io.car.server.core.MeasurementValue;
import io.car.server.rest.MediaTypes;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * 
 * @author @author Arne de Wall <a.dewall@52north.org>
 * 
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MeasurementProvider extends
		AbstractJsonEntityProvider<Measurement> {

	static final GeometryFactory geometry = new GeometryFactory();

	private final DateTimeFormatter formatter = ISODateTimeFormat
			.dateTimeNoMillis();
	@Inject
	private EntityFactory factory;

	public MeasurementProvider() {
		super(Measurement.class, MediaTypes.MEASUREMENT_TYPE,
				MediaTypes.MEASUREMENT_CREATE_TYPE,
				MediaTypes.MEASUREMENT_MODIFY_TYPE);
	}

	@Override
	public Measurement read(JSONObject j, MediaType mediaType)
			throws JSONException {
		// XXX check which direction of lon lat =) ?
		Measurement measurement = factory.createMeasurement();
		JSONArray location = j.getJSONArray(JSONConstants.LOCATION_KEY);
		measurement.setLocation(geometry.createPoint(new Coordinate(location
				.getDouble(0), location.getDouble(1))));

		JSONArray array = j.getJSONArray(JSONConstants.PHENOMENONS_KEY);
		for (int i = 0; i < array.length(); i++) {
			// measurement.addPhenomenon(array.get(i), value);
			// measurement. XXX TODO
		}
		return null;
	}

	@Override
	public JSONObject write(Measurement t, MediaType mediaType)
			throws JSONException {
		JSONObject object = new JSONObject();
		// object.put(JSONConstants.lo, value)
		return null;
	}

}
