/*
 * Copyright (C) 2013-2019 The enviroCar project
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
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Singleton
public class UserJSONEncoder extends AbstractJSONEntityEncoder<User> {
    private final JSONEntityEncoder<Geometry> geometryEncoder;

    @Inject
    public UserJSONEncoder(JSONEntityEncoder<Geometry> geometryEncoder) {
        super(User.class);
        this.geometryEncoder = geometryEncoder;
    }

    @Override
    public ObjectNode encodeJSON(User entity, AccessRights rights, MediaType mediaType) {
        ObjectNode j = getJsonFactory().objectNode();
        if (entity.hasName() && rights.canSeeNameOf(entity)) {
            j.put(JSONConstants.NAME_KEY, entity.getName());
        }
        if (entity.hasMail() && rights.canSeeMailOf(entity)) {
            j.put(JSONConstants.MAIL_KEY, entity.getMail());
        }
        if (entity.hasCreationTime() && rights.canSeeCreationTimeOf(entity)) {
            j.put(JSONConstants.CREATED_KEY, getDateTimeFormat().print(entity.getCreationTime()));
        }
        if (entity.hasModificationTime() && rights.canSeeModificationTimeOf(entity)) {
            j.put(JSONConstants.MODIFIED_KEY, getDateTimeFormat().print(entity.getModificationTime()));
        }
        if (entity.hasFirstName() && rights.canSeeFirstNameOf(entity)) {
            j.put(JSONConstants.FIRST_NAME_KEY, entity.getFirstName());
        }
        if (entity.hasLastName() && rights.canSeeLastNameOf(entity)) {
            j.put(JSONConstants.LAST_NAME_KEY, entity.getLastName());
        }
        if (entity.hasGender() && rights.canSeeGenderOf(entity)) {
            switch (entity.getGender()) {
                case MALE:
                    j.put(JSONConstants.GENDER_KEY, JSONConstants.MALE);
                    break;
                case FEMALE:
                    j.put(JSONConstants.GENDER_KEY, JSONConstants.FEMALE);
                    break;
            }
        }
        if (entity.hasDayOfBirth() && rights.canSeeDayOfBirthOf(entity)) {
            j.put(JSONConstants.DAY_OF_BIRTH_KEY, entity.getDayOfBirth());
        }
        if (entity.hasAboutMe() && rights.canSeeAboutMeOf(entity)) {
            j.put(JSONConstants.ABOUT_ME_KEY, entity.getAboutMe());
        }
        if (entity.hasCountry() && rights.canSeeCountryOf(entity)) {
            j.put(JSONConstants.COUNTRY_KEY, entity.getCountry());
        }
        if (entity.hasLocation() && rights.canSeeLocationOf(entity)) {
            j.set(JSONConstants.LOCATION_KEY, geometryEncoder.encodeJSON(entity.getLocation(), rights, mediaType));
        }
        if (entity.hasLanguage() && rights.canSeeLanguageOf(entity)) {
            j.put(JSONConstants.LANGUAGE_KEY, entity.getLanguage());
        }
        if (entity.hasBadges() && rights.canSeeBadgesOf(entity)) {
            entity.getBadges().forEach(j.putArray(JSONConstants.BADGES)::add);
        }
        if (entity.hasTermsOfUseVersion() && rights.canSeeAcceptedTermsOfUseVersionOf(entity)) {
            j.put(JSONConstants.ACCEPTED_TERMS_OF_USE_VERSION_KEY, entity.getTermsOfUseVersion());
        }
        if (entity.hasPrivacyStatementVersion() && rights.canSeeAcceptedPrivacyStatementVersionOf(entity)) {
            j.put(JSONConstants.ACCEPTED_PRIVACY_STATEMENT_VERSION, entity.getPrivacyStatementVersion());
        }
        return j;
    }
}
