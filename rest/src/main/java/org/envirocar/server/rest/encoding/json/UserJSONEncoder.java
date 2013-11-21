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

import static org.envirocar.server.core.entities.Gender.FEMALE;
import static org.envirocar.server.core.entities.Gender.MALE;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class UserJSONEncoder extends AbstractJSONEntityEncoder<User> {
    private final JSONEntityEncoder<Geometry> geometryEncoder;

    @Inject
    public UserJSONEncoder(JSONEntityEncoder<Geometry> geometryEncoder) {
        super(User.class);
        this.geometryEncoder = geometryEncoder;
    }

    @Override
    public ObjectNode encodeJSON(User t, AccessRights rights,
                                 MediaType mediaType) {
        ObjectNode j = getJsonFactory().objectNode();
        if (t.hasName() && rights.canSeeNameOf(t)) {
            j.put(JSONConstants.NAME_KEY, t.getName());
        }
        if (t.hasMail() && rights.canSeeMailOf(t)) {
            j.put(JSONConstants.MAIL_KEY, t.getMail());
        }
        if (t.hasCreationTime() && rights.canSeeCreationTimeOf(t)) {
            j.put(JSONConstants.CREATED_KEY,
                  getDateTimeFormat().print(t.getCreationTime()));
        }
        if (t.hasModificationTime() && rights.canSeeModificationTimeOf(t)) {
            j.put(JSONConstants.MODIFIED_KEY,
                  getDateTimeFormat().print(t.getModificationTime()));
        }
        if (t.hasFirstName() && rights.canSeeFirstNameOf(t)) {
            j.put(JSONConstants.FIRST_NAME_KEY, t.getFirstName());
        }
        if (t.hasLastName() && rights.canSeeLastNameOf(t)) {
            j.put(JSONConstants.LAST_NAME_KEY, t.getLastName());
        }
        if (t.hasGender() && rights.canSeeGenderOf(t)) {
            switch (t.getGender()) {
                case MALE:
                    j.put(JSONConstants.GENDER_KEY, JSONConstants.MALE);
                    break;
                case FEMALE:
                    j.put(JSONConstants.GENDER_KEY, JSONConstants.FEMALE);
                    break;
            }
        }
        if (t.hasDayOfBirth() && rights.canSeeDayOfBirthOf(t)) {
            j.put(JSONConstants.DAY_OF_BIRTH_KEY, t.getDayOfBirth());
        }
        if (t.hasAboutMe() && rights.canSeeAboutMeOf(t)) {
            j.put(JSONConstants.ABOUT_ME_KEY, t.getAboutMe());
        }
        if (t.hasCountry() && rights.canSeeCountryOf(t)) {
            j.put(JSONConstants.COUNTRY_KEY, t.getCountry());
        }
        if (t.hasLocation() && rights.canSeeLocationOf(t)) {
            j.put(JSONConstants.LOCATION_KEY,
                  geometryEncoder.encodeJSON(t.getLocation(), rights, mediaType));
        }
        if (t.hasLanguage() && rights.canSeeLanguageOf(t)) {
            j.put(JSONConstants.LANGUAGE_KEY, t.getLanguage());
        }
        if (t.hasBadges() && rights.canSeeBadgesOf(t)) {
            final ArrayNode badges = j.putArray(JSONConstants.BADGES);
            for (String badge : t.getBadges()) {
                badges.add(badge);
            }
        }
        if (t.getTermsOfUseVersion() != null) {
        	j.put(JSONConstants.TOU_VERSION_KEY, t.getTermsOfUseVersion());
        	
        	// kept for backwards compatibility
        	j.put(JSONConstants.ACCEPTED_TERMS_OF_USE_VERSION_KEY, t.getTermsOfUseVersion());
        }
        return j;
    }
}
