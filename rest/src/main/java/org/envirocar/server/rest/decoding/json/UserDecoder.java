/*
 * Copyright (C) 2013-2018 The enviroCar project
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
package org.envirocar.server.rest.decoding.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;
import org.envirocar.server.core.entities.Gender;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.rest.JSONConstants;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Singleton
public class UserDecoder extends AbstractJSONEntityDecoder<User> {
    private final JSONEntityDecoder<Geometry> geometryDecoder;

    @Inject
    public UserDecoder(JSONEntityDecoder<Geometry> geometryDecoder) {
        super(User.class);
        this.geometryDecoder = geometryDecoder;
    }

    @Override
    public User decode(JsonNode node, MediaType mediaType) {
        User user = getEntityFactory().createUser();
        user.setName(node.path(JSONConstants.NAME_KEY).textValue());
        user.setMail(node.path(JSONConstants.MAIL_KEY).textValue());
        user.setToken(node.path(JSONConstants.TOKEN_KEY).textValue());

        user.setAboutMe(node.path(JSONConstants.ABOUT_ME_KEY).textValue());
        user.setCountry(node.path(JSONConstants.COUNTRY_KEY).textValue());
        user.setDayOfBirth(node.path(JSONConstants.DAY_OF_BIRTH_KEY).textValue());
        user.setFirstName(node.path(JSONConstants.FIRST_NAME_KEY).textValue());
        user.setLastName(node.path(JSONConstants.LAST_NAME_KEY).textValue());
        user.setLanguage(node.path(JSONConstants.LANGUAGE_KEY).textValue());

        user.setAcceptedTermsOfUse(node.path(JSONConstants.ACCEPTED_TERMS_OF_USE).booleanValue());
        user.setAcceptedPrivacyStatement(node.path(JSONConstants.ACCEPTED_PRIVACY_STATEMENT).booleanValue());

        user.setTermsOfUseVersion(node.path(JSONConstants.ACCEPTED_TERMS_OF_USE_VERSION_KEY).textValue());
        user.setPrivacyStatementVersion(node.path(JSONConstants.ACCEPTED_PRIVACY_STATEMENT_VERSION).textValue());

        JsonNode location = node.path(JSONConstants.LOCATION_KEY);
        if (!location.isMissingNode() && !location.isNull()) {
            user.setLocation(geometryDecoder.decode(location, mediaType));
        }
        String g = node.path(JSONConstants.GENDER_KEY).textValue();
        if (g != null) {
            if (g.equalsIgnoreCase(JSONConstants.MALE)) {
                user.setGender(Gender.MALE);
            } else if (g.equalsIgnoreCase(JSONConstants.FEMALE)) {
                user.setGender(Gender.FEMALE);
            } else {
                throw new BadRequestException();
            }
        }
        String u = node.path(JSONConstants.URL_KEY).textValue();
        if (u != null) {
            try {
                user.setUrl(new URL(u));
            } catch (MalformedURLException ex) {
                throw new BadRequestException(ex);
            }
        }
        return user;
    }
}
