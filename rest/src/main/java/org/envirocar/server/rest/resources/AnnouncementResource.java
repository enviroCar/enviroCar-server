package org.envirocar.server.rest.resources;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.envirocar.server.core.entities.Announcement;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.validation.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

public class AnnouncementResource extends AbstractResource {
    private final Announcement announcement;

    @Inject
    public AnnouncementResource(@Assisted Announcement announcement) {
        this.announcement = announcement;
    }

    @GET
    @Schema(response = Schemas.TERMS_OF_USE_INSTANCE)
    @Produces({MediaTypes.TERMS_OF_USE_INSTANCE})
    public Announcement get() {
        return announcement;
    }

}
