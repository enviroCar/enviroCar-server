/*
 * Copyright (C) 2013-2021 The enviroCar project
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.envirocar.server.core.entities.Announcement;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.util.Map.Entry;

@Singleton
@Provider
public class AnnouncementJSONEncoder extends AbstractJSONEntityEncoder<Announcement> {

    public AnnouncementJSONEncoder() {
        super(Announcement.class);
    }

    @Override
    public ObjectNode encodeJSON(Announcement entity, AccessRights rights, MediaType mediaType) {
        ObjectNode node = getJsonFactory().objectNode();
        if (entity.hasCreationTime()) {
            node.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(entity.getCreationTime()));
        }
        if (entity.hasModificationTime()) {
            node.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(entity.getModificationTime()));
        }
        if (entity.getIdentifier() != null) {
            node.put(JSONConstants.IDENTIFIER_KEY, entity.getIdentifier());
        }
        if (entity.getVersions() != null) {
            node.put(JSONConstants.VERSIONS, entity.getVersions());
        }
        if (entity.getCategory() != null) {
            node.put(JSONConstants.CATEGORY, entity.getCategory());
        }
        if (entity.getPriority() != null) {
            node.put(JSONConstants.PRIORITY, entity.getPriority());
        }
        if (entity.getContents() != null) {
            ObjectNode values = node.putObject(JSONConstants.CONTENT);
            for (Entry<String, String> entry : entity.getContents().entrySet()) {
                values.put(entry.getKey(), entry.getValue());
            }
        }
        return node;
    }

}
