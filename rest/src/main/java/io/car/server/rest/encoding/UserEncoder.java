/*
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.rest.encoding;

import static io.car.server.core.entities.Gender.FEMALE;
import static io.car.server.core.entities.Gender.MALE;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.entities.User;
import io.car.server.rest.JSONConstants;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.UserResource;
import io.car.server.rest.resources.UsersResource;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UserEncoder extends AbstractEntityEncoder<User> {
    private final EntityEncoder<Geometry> geometryEncoder;

    @Inject
    public UserEncoder(EntityEncoder<Geometry> geometryEncoder) {
        this.geometryEncoder = geometryEncoder;
    }

    @Override
    public ObjectNode encode(User t, MediaType mediaType) {
        ObjectNode j = getJsonFactory().objectNode();
        if (t.hasName()) {
            j.put(JSONConstants.NAME_KEY, t.getName());
        }
        if (mediaType.equals(MediaTypes.USER_TYPE)) {
            if (t.hasMail()) {
                j.put(JSONConstants.MAIL_KEY, t.getMail());
            }
            if (t.hasCreationTime()) {
                j.put(JSONConstants.CREATED_KEY,
                      getDateTimeFormat().print(t.getCreationTime()));
            }
            if (t.hasModificationTime()) {
                j.put(JSONConstants.MODIFIED_KEY,
                      getDateTimeFormat().print(t.getModificationTime()));
            }
            if (t.hasFirstName()) {
                j.put(JSONConstants.FIRST_NAME_KEY, t.getFirstName());
            }
            if (t.hasLastName()) {
                j.put(JSONConstants.LAST_NAME_KEY, t.getLastName());
            }
            if (t.hasGender()) {
                switch (t.getGender()) {
                    case MALE:
                        j.put(JSONConstants.GENDER_KEY, JSONConstants.MALE);
                        break;
                    case FEMALE:
                        j.put(JSONConstants.GENDER_KEY, JSONConstants.FEMALE);
                        break;
                }
            }
            if (t.hasDayOfBirth()) {
                j.put(JSONConstants.DAY_OF_BIRTH_KEY, t.getDayOfBirth());
            }
            if (t.hasAboutMe()) {
                j.put(JSONConstants.ABOUT_ME_KEY, t.getAboutMe());
            }
            if (t.hasCountry()) {
                j.put(JSONConstants.COUNTRY_KEY, t.getCountry());
            }
            if (t.hasLocation()) {
                j.put(JSONConstants.LOCATION_KEY,
                      geometryEncoder.encode(t.getLocation(), mediaType));
            }
            if (t.hasLanguage()) {
                j.put(JSONConstants.LANGUAGE_KEY, t.getLanguage());
            }

            encodeLinks(j);
        } else {
            URI uri = getUriInfo().getBaseUriBuilder()
                    .path(RootResource.class)
                    .path(RootResource.USERS)
                    .path(UsersResource.USER).build(t.getName());
            return j.put(JSONConstants.HREF_KEY, uri.toString());
        }
        return j;
    }

    protected void encodeLinks(ObjectNode j) {
        j.put(JSONConstants.MEASUREMENTS_KEY,
              getUriInfo().getAbsolutePathBuilder()
                .path(UserResource.MEASUREMENTS)
                .build().toString());
        j.put(JSONConstants.GROUPS_KEY,
              getUriInfo().getAbsolutePathBuilder()
                .path(UserResource.GROUPS)
                .build().toString());
        j.put(JSONConstants.FRIENDS_KEY,
              getUriInfo().getAbsolutePathBuilder()
                .path(UserResource.FRIENDS)
                .build().toString());
        j.put(JSONConstants.TRACKS_KEY,
              getUriInfo().getAbsolutePathBuilder()
                .path(UserResource.TRACKS)
                .build().toString());
        j.put(JSONConstants.STATISTICS_KEY,
              getUriInfo().getAbsolutePathBuilder()
                .path(UserResource.STATISTICS)
                .build().toString());
        j.put(JSONConstants.ACTIVITIES_KEY,
              getUriInfo().getAbsolutePathBuilder()
                .path(UserResource.ACTIVITIES)
                .build().toString());
        j.put(JSONConstants.FRIEND_ACTIVITIES,
              getUriInfo().getAbsolutePathBuilder()
                .path(UserResource.FRIEND_ACTIVITIES)
                .build().toString());
        j.put(JSONConstants.AVATAR_KEY,
              getUriInfo().getAbsolutePathBuilder()
                .path(UserResource.AVATAR)
                .build().toString());
    }
}
