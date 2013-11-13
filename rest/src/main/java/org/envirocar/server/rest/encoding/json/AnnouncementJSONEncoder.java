/*
 * Copyright (C) 2013 The enviroCar project
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

import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.entities.Announcement;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.rights.AccessRights;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Provider
public class AnnouncementJSONEncoder extends AbstractJSONEntityEncoder<Announcement> {

    public AnnouncementJSONEncoder() {
        super(Announcement.class);
    }

    @Override
    public ObjectNode encodeJSON(Announcement a, AccessRights rights,
                                 MediaType mediaType) {
        ObjectNode anno = getJsonFactory().objectNode();
        if (a.hasCreationTime()) {
            anno.put(JSONConstants.CREATED_KEY, getDateTimeFormat()
                    .print(a.getCreationTime()));
        }
        if (a.hasModificationTime()) {
            anno.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat()
                    .print(a.getModificationTime()));
        }
        if (a.getIdentifier() != null) {
            anno.put(JSONConstants.IDENTIFIER_KEY, a.getIdentifier());
        }
        if (a.getVersions() != null) {
            anno.put(JSONConstants.VERSIONS, a.getVersions());
        }
        if (a.getCategory() != null) {
            anno.put(JSONConstants.CATEGORY, a.getCategory());
        }
        if (a.getPriority() != null) {
            anno.put(JSONConstants.PRIORITY, a.getPriority());
        }
        if (a.getContents() != null) {
            ObjectNode values = anno.putObject(JSONConstants.CONTENT);
            for (Entry<String, String> e : a.getContents().entrySet()) {
                values.put(e.getKey(), e.getValue());
            }
        }
        return anno;
    }

}
