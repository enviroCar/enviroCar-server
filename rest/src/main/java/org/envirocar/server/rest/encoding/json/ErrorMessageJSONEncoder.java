package org.envirocar.server.rest.encoding.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.mapper.ErrorMessage;
import org.envirocar.server.rest.rights.AccessRights;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

@Provider
public class ErrorMessageJSONEncoder extends AbstractJSONEntityEncoder<ErrorMessage> {

    public ErrorMessageJSONEncoder() {
        super(ErrorMessage.class);
    }

    @Override
    public ObjectNode encodeJSON(ErrorMessage errorMessage, AccessRights rights, MediaType mt) {
        return getJsonFactory().objectNode()
                .put(JSONConstants.STATUS_CODE, errorMessage.getStatus().getStatusCode())
                .put(JSONConstants.REASON_PHRASE, errorMessage.getStatus().getReasonPhrase())
                .put(JSONConstants.MESSAGE, errorMessage.getMessage())
                .put(JSONConstants.DETAILS, errorMessage.getDetails());
    }
}
