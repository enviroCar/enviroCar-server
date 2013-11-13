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

import org.envirocar.server.core.entities.Badge;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.rights.AccessRights;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Provider
public class BadgeJSONEncoder extends AbstractJSONEntityEncoder<Badge> {

    public BadgeJSONEncoder() {
        super(Badge.class);
    }

    @Override
    public ObjectNode encodeJSON(Badge badge, AccessRights rights,
                                 MediaType mediaType) {
        ObjectNode json = getJsonFactory().objectNode();

        if (badge.getName() != null) {
            json.put(JSONConstants.NAME_KEY, badge.getName());
        }
        
        if (badge.getDisplayName() != null) {
            ObjectNode values = json.putObject(JSONConstants.DISPLAY_NAME_KEY);
            for (Entry<String, String> e : badge.getDisplayName().entrySet()) {
                values.put(e.getKey(), e.getValue());
            }
        }
        
        if (badge.getDescription() != null) {
            ObjectNode values = json.putObject(JSONConstants.DESCRIPTION_KEY);
            for (Entry<String, String> e : badge.getDescription().entrySet()) {
                values.put(e.getKey(), e.getValue());
            }
        }
        return json;
    }
}