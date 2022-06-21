/*
 * Copyright (C) 2013-2022 The enviroCar project
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
package org.envirocar.server.rest.resources;

import org.envirocar.server.core.entities.Announcement;
import org.envirocar.server.core.entities.Announcements;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

public class AnnouncementsResource extends AbstractResource {

    public static final String ANNOUNCEMENT = "{id}";

    @GET
    @Schema(response = Schemas.ANNOUNCEMENTS)
    public Announcements announcements() throws BadRequestException {
        return getDataService().getAnnouncements(getPagination());
    }

    @Path(ANNOUNCEMENT)
    public AnnouncementResource announcement(@PathParam("id") String id) throws ResourceNotFoundException {
        Announcement announcement = getDataService().getAnnouncement(id);
        return getResourceFactory().createAnnouncementResource(announcement);
    }


}
