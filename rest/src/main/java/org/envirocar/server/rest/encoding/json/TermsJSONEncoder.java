package org.envirocar.server.rest.encoding.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.envirocar.server.core.entities.Terms;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.rights.AccessRights;

import javax.ws.rs.core.MediaType;

public abstract class TermsJSONEncoder<T extends Terms> extends AbstractJSONEntityEncoder<T> {
    private final String schema;

    public TermsJSONEncoder(Class<T> clazz, String schema) {
        super(clazz);
        this.schema = schema;
    }

    @Override
    public ObjectNode encodeJSON(T entity, AccessRights rights, MediaType mediaType) {
        ObjectNode terms = getJsonFactory().objectNode();
        if (entity.getIdentifier() != null) {
            terms.put(JSONConstants.IDENTIFIER_KEY, entity.getIdentifier());
        }
        if (entity.getIssuedDate() != null) {
            terms.put(JSONConstants.ISSUED_DATE, entity.getIssuedDate());
        }
        if (getSchemaUriConfiguration().isSchema(mediaType, schema)) {
            if (entity.hasCreationTime()) {
                terms.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(entity.getCreationTime()));
            }
            if (entity.hasModificationTime()) {
                terms.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(entity.getModificationTime()));
            }
            if (entity.getContents() != null) {
                terms.put(JSONConstants.CONTENTS, entity.getContents());
            }
        }
        return terms;
    }
}
