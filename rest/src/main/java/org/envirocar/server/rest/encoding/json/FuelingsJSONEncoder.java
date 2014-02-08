package org.envirocar.server.rest.encoding.json;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Fuelings;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

/**
 * JSON encoder for {@link Fuelings}.
 *
 * @author Christian Autermann
 */
@Provider
public class FuelingsJSONEncoder extends AbstractJSONEntityEncoder<Fuelings> {
    private final JSONEntityEncoder<Fueling> fuelingEncoder;

    /**
     * Creates a new {@code FuelingsJSONEncoder} using the supplied encoder as a
     * delegate.
     *
     * @param fuelingEncoder the encoder to use for {@link Fueling}s
     */
    @Inject
    public FuelingsJSONEncoder(JSONEntityEncoder<Fueling> fuelingEncoder) {
        super(Fuelings.class);
        this.fuelingEncoder = fuelingEncoder;
    }

    @Override
    public ObjectNode encodeJSON(Fuelings fuelings, AccessRights rights,
                                 MediaType mt) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode annos = root.putArray(JSONConstants.FUELINGS);

        for (Fueling b : fuelings) {
            annos.add(fuelingEncoder.encodeJSON(b, rights, mt));
        }
        return root;
    }

}
