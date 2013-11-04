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
package org.envirocar.server.core.entities;

import java.net.URL;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface User extends BaseEntity {
    Set<String> getBadges();

    boolean hasBadges();

    String getName();

    void setName(String name);

    boolean hasName();

    String getMail();

    void setMail(String mail);

    boolean hasMail();

    String getToken();

    void setToken(String token);

    boolean hasToken();

    boolean isAdmin();

    void setAdmin(boolean isAdmin);

    String getCountry();

    void setCountry(String country);

    boolean hasCountry();

    String getDayOfBirth();

    void setDayOfBirth(String dayOfBirth);

    boolean hasDayOfBirth();

    String getFirstName();

    void setFirstName(String firstName);

    boolean hasFirstName();

    Gender getGender();

    void setGender(Gender gender);

    boolean hasGender();

    String getLanguage();

    void setLanguage(String language);

    boolean hasLanguage();

    String getLastName();

    void setLastName(String lastName);

    boolean hasLastName();

    Geometry getLocation();

    void setLocation(Geometry location);

    boolean hasLocation();

    URL getUrl();

    void setUrl(URL url);

    boolean hasUrl();

    String getAboutMe();

    void setAboutMe(String aboutMe);

    boolean hasAboutMe();

	void setTermsOfUseVersion(String tou);

	String getTermsOfUseVersion();

	boolean hasAcceptedTermsOfUseVersion();
}
