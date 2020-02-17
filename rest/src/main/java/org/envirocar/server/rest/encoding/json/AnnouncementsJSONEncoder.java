/*
 * Copyright (C) 2013-2020 The enviroCar project
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
package org.envirocar.server.rest.encoding.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import org.envirocar.server.core.entities.Announcement;
import org.envirocar.server.core.entities.Announcements;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

@Singleton
@Provider
public class AnnouncementsJSONEncoder extends AbstractJSONEntityEncoder<Announcements> {
    private final JSONEntityEncoder<Announcement> announcementEncoder;

    @Inject
    public AnnouncementsJSONEncoder(
            JSONEntityEncoder<Announcement> enc) {
        super(Announcements.class);
        this.announcementEncoder = enc;
    }

    @Override
    public ObjectNode encodeJSON(Announcements entity, AccessRights rights, MediaType mediaType) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode announcements = root.putArray(JSONConstants.ANNOUNCEMENTS_KEY);

        for (Announcement u : entity) {
            announcements.add(announcementEncoder.encodeJSON(u, rights, mediaType));
        }
        return root;
    }
}
