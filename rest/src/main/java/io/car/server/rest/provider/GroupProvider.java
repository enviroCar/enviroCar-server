/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package io.car.server.rest.provider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.inject.Inject;

import io.car.server.core.EntityFactory;
import io.car.server.core.Group;
import io.car.server.rest.MediaTypes;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupProvider extends AbstractJsonEntityProvider<Group> {
    private final DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis();
    @Inject
    private EntityFactory factory;
    @Inject
    private UserProvider userProvider;

    public GroupProvider() {
        super(Group.class, MediaTypes.GROUP_TYPE, MediaTypes.GROUP_CREATE_TYPE, MediaTypes.GROUP_MODIFY_TYPE);
    }

    @Override
    public Group read(JSONObject j, MediaType mediaType) throws JSONException {
        return factory.createGroup()
                .setName(j.optString(JSONConstants.NAME_KEY, null))
                .setDescription(j.optString(JSONConstants.DESCRIPTION_KEY, null));
    }

    @Override
    public JSONObject write(Group t, MediaType mediaType) throws JSONException {

        return new JSONObject()
                .put(JSONConstants.NAME_KEY, t.getName())
                .put(JSONConstants.DESCRIPTION_KEY, t.getDescription())
                .put(JSONConstants.CREATED_KEY, formatter.print(t.getCreationDate()))
                .put(JSONConstants.MODIFIED_KEY, formatter.print(t.getLastModificationDate()))
                .put(JSONConstants.OWNER_KEY, userProvider.write(t.getOwner(), mediaType));
    }
}
