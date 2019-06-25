package org.envirocar.server.rest.resources;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.validation.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

public class PrivacyStatementResource extends AbstractResource {
    private final PrivacyStatement privacyStatement;

    @Inject
    public PrivacyStatementResource(@Assisted PrivacyStatement privacyStatement) {
        this.privacyStatement = privacyStatement;
    }

    @GET
    @Schema(response = Schemas.PRIVACY_STATEMENT)
    @Produces({MediaTypes.PRIVAVY_STATEMENT})
    public PrivacyStatement get() {
        return privacyStatement;
    }

}
