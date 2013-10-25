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
package org.envirocar.server.mongo.entity;

import java.net.URL;
import java.util.Collections;
import java.util.Set;

import org.envirocar.server.core.entities.Gender;
import org.envirocar.server.core.entities.User;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.google.common.base.Objects;
import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("users")
public class MongoUser extends MongoEntityBase implements User {
    public static final String TOKEN = "token";
    public static final String IS_ADMIN = "isAdmin";
    public static final String NAME = Mapper.ID_KEY;
    public static final String MAIL = "mail";
    public static final String FRIENDS = "friends";
    public static final String LAST_NAME = "lastName";
    public static final String FIRST_NAME = "firstName";
    public static final String COUNTRY = "country";
    public static final String LOCATION = "location";
    public static final String ABOUT_ME = "aboutMe";
    public static final String URL = "url";
    public static final String DAY_OF_BIRTH = "dayOfBirth";
    public static final String GENDER = "gender";
    public static final String LANGUAGE = "lang";
    public static final String BADGES = "badges";
    /**
     * @deprecated use {@link #TERMS_OF_USE_VERSION} instead. kept for backwards compatibility
     */
    @Deprecated
    public static final String ACCEPTED_TERMS_OF_USE = "acceptedTermsOfUseVersion";
    public static final String TERMS_OF_USE_VERSION = "touVersion";

    @Property(TOKEN)
    private String token;
    @Property(IS_ADMIN)
    private boolean isAdmin = false;
    @Id
    private String name;
    @Indexed(unique = true)
    @Property(MAIL)
    private String mail;
    @Property(FRIENDS)
    private Set<Key<MongoUser>> friends;
    @Property(FIRST_NAME)
    private String firstName;
    @Property(LAST_NAME)
    private String lastName;
    @Property(COUNTRY)
    private String country;
    @Property(LOCATION)
    private Geometry location;
    @Property(ABOUT_ME)
    private String aboutMe;
    @Property(URL)
    private URL url;
    @Property(DAY_OF_BIRTH)
    private String dayOfBirth;
    @Property(GENDER)
    private Gender gender;
    @Property(LANGUAGE)
    private String language;
    @Property(BADGES)
    private Set<String> badges;
    /**
     * @deprecated use {@link #termsOfUseVersion} instead. kept for backwards compatibility
     */
    @Deprecated
    @Property(ACCEPTED_TERMS_OF_USE)
    private String acceptedTermsOfUseVersion;
    @Property(TERMS_OF_USE_VERSION)
    private String termsOfUseVersion;
    
    public MongoUser(String name) {
        this.name = name;
    }

    public MongoUser() {
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean hasName() {
        return getName() != null && !getName().isEmpty();
    }

    @Override
    public boolean hasMail() {
        return getMail() != null && !getMail().isEmpty();
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean hasToken() {
        return getToken() != null && !getToken().isEmpty();
    }

    public Set<Key<MongoUser>> getFriends() {
        return friends == null ? null : Collections.unmodifiableSet(friends);
    }

    @Override
    public String toString() {
        return toStringHelper()
                .add(NAME, name)
                .add(MAIL, mail)
                .add(IS_ADMIN, isAdmin)
                .add(FRIENDS, friends).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MongoUser other = (MongoUser) obj;
        return Objects.equal(this.name, other.name);
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public Geometry getLocation() {
        return location;
    }

    @Override
    public void setLocation(Geometry location) {
        this.location = location;
    }

    @Override
    public String getAboutMe() {
        return aboutMe;
    }

    @Override
    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String getDayOfBirth() {
        return dayOfBirth;
    }

    @Override
    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean hasCountry() {
        return getCountry() != null && !getCountry().isEmpty();
    }

    @Override
    public boolean hasDayOfBirth() {
        return getDayOfBirth() != null && !getDayOfBirth().isEmpty();
    }

    @Override
    public boolean hasFirstName() {
        return getFirstName() != null && !getFirstName().isEmpty();
    }

    @Override
    public boolean hasGender() {
        return getGender() != null;
    }

    @Override
    public boolean hasLanguage() {
        return getLanguage() != null && !getLanguage().isEmpty();
    }

    @Override
    public boolean hasLastName() {
        return getLastName() != null && !getLastName().isEmpty();
    }

    @Override
    public boolean hasLocation() {
        return getLocation() != null && !getLocation().isEmpty();
    }

    @Override
    public boolean hasUrl() {
        return getUrl() != null;
    }

    @Override
    public boolean hasAboutMe() {
        return getAboutMe() != null && !getAboutMe().isEmpty();
    }

    @Override
    public Set<String> getBadges() {
        return this.badges == null ? null : Collections.unmodifiableSet(this.badges);
    }

    @Override
    public boolean hasBadges() {
        return this.badges != null && !this.badges.isEmpty();
    }

    @Override
	public String getTermsOfUseVersion() {
    	//acceptedTermsOfUseVersion kept for backwards compatibility with older users
		return termsOfUseVersion != null ? termsOfUseVersion : acceptedTermsOfUseVersion;
	}

    @Override
	public void setTermsOfUseVersion(String tou) {
		this.termsOfUseVersion = tou;
	}

	@Override
	public boolean hasAcceptedTermsOfUseVersion() {
		return getTermsOfUseVersion() != null && !getTermsOfUseVersion().isEmpty();
	}
    
}
