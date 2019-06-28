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
package org.envirocar.server.core.entities;

import com.vividsolutions.jts.geom.Geometry;
import org.joda.time.DateTime;

import java.net.URL;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface User extends BaseEntity {
    Set<String> getBadges();

    default boolean hasBadges() {
        Set<String> badges = getBadges();
        return badges != null && !badges.isEmpty();
    }

    String getName();

    void setName(String name);

    default boolean hasName() {
        return getName() != null && !getName().isEmpty();
    }

    String getMail();

    void setMail(String mail);

    default boolean hasMail() {
        return getMail() != null && !getMail().isEmpty();
    }

    String getToken();

    void setToken(String token);

    default boolean hasToken() {
        return getToken() != null && !getToken().isEmpty();
    }

    boolean isAdmin();

    default boolean isConfirmed() {
        return getConfirmationCode()== null || getConfirmationCode().isEmpty();
    }

    String getConfirmationCode();

    void setConfirmationCode(String code);

    DateTime getExpirationDate();

    void setExpirationDate(DateTime time);

    void setAdmin(boolean isAdmin);

    String getCountry();

    void setCountry(String country);

    default boolean hasCountry() {
        return getCountry() != null && !getCountry().isEmpty();
    }

    String getDayOfBirth();

    void setDayOfBirth(String dayOfBirth);

    default boolean hasDayOfBirth() {
        return getDayOfBirth() != null && !getDayOfBirth().isEmpty();
    }

    String getFirstName();

    void setFirstName(String firstName);

    default boolean hasFirstName() {
        return getFirstName() != null && !getFirstName().isEmpty();
    }

    Gender getGender();

    void setGender(Gender gender);

    default boolean hasGender() {
        return getGender() != null;
    }

    String getLanguage();

    void setLanguage(String language);

    default boolean hasLanguage() {
        return getLanguage() != null && !getLanguage().isEmpty();
    }

    String getLastName();

    void setLastName(String lastName);

    default boolean hasLastName() {
        return getLastName() != null && !getLastName().isEmpty();
    }

    Geometry getLocation();

    void setLocation(Geometry location);

    default boolean hasLocation() {
        return getLocation() != null && !getLocation().isEmpty();
    }

    URL getUrl();

    void setUrl(URL url);

    default boolean hasUrl() {
        return getUrl() != null;
    }

    String getAboutMe();

    void setAboutMe(String aboutMe);

    default boolean hasAboutMe() {
        return getAboutMe() != null && !getAboutMe().isEmpty();
    }

    void setTermsOfUseVersion(String tou);

    String getTermsOfUseVersion();

    default boolean hasAcceptedTermsOfUseVersion() {
        return getTermsOfUseVersion() != null && !getTermsOfUseVersion().isEmpty();
    }

    boolean hasAcceptedTermsOfUse();

    void setAcceptedTermsOfUse(boolean hasAcceptedTermsOfUse);

    boolean hasAcceptedPrivacyStatement();

    void setAcceptedPrivacyStatement(boolean hasAcceptedPrivacyStatement);

    void setPrivacyStatementVersion(String privacyStatementVersion);

    String getPrivacyStatementVersion();

    default boolean hasPrivacyStatementVersion() {
        String version = getPrivacyStatementVersion();
        return version != null && !version.isEmpty();
    }
}
