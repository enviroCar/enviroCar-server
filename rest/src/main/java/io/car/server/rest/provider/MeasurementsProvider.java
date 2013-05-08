package io.car.server.rest.provider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.rest.MediaTypes;

/**
 *
 * @author Christian Autermann <c.autermann@52north.org>
 * @author Arne de Wall <a.dewall@52north.org>
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MeasurementsProvider extends AbstractJsonEntityProvider<Measurements> {

	@Context
	private UriInfo uriInfo;

	public MeasurementsProvider(Class<Measurements> classType, MediaType get) {
		super(Measurements.class, MediaTypes.MEASUREMENTS_TYPE);
	}

	@Override
    public Measurements read(JSONObject j, MediaType mediaType) throws JSONException {
        throw new UnsupportedOperationException();
	}

	@Override
    public JSONObject write(Measurements t, MediaType mediaType) throws JSONException {
        JSONArray measurements = new JSONArray();
        for (Measurement m : t) {
            measurements.put(new JSONObject()
                    .put(JSONConstants.IDENTIFIER_KEY, m.getIdentifier())
                    .put(JSONConstants.HREF_KEY, uriInfo.getRequestUriBuilder().path(m.getIdentifier())));
		}
        return new JSONObject().put(JSONConstants.MEASUREMENTS_KEY, measurements);
	}
}
