/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import dev.morphia.Key;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Transient;
import org.envirocar.server.core.entities.Gender;
import org.envirocar.server.core.entities.User;
import org.joda.time.DateTime;
import org.locationtech.jts.geom.Geometry;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity(MongoUser.COLLECTION)
public class MongoUser extends MongoEntityBase implements User {
    public static final String TOKEN = "token";
    public static final String IS_ADMIN = "isAdmin";
    public static final String NAME = "_id";
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
    public static final String CONFIRMATION_CODE = "confirmationCode";
    public static final String EXPIRE_AT = "expireAt";

    /**
     * @deprecated use {@link #TERMS_OF_USE_VERSION} instead. kept for backwards compatibility
     */
    @Deprecated
    public static final String ACCEPTED_TERMS_OF_USE = "acceptedTermsOfUseVersion";
    public static final String TERMS_OF_USE_VERSION = "touVersion";
    public static final String TERMS_OF_USE_HISTORY = "touHistory";
    public static final String PRIVACY_STATEMENT_VERSION = "privacyStatementVersion";
    public static final String PRIVACY_STATEMENT_HISTORY = "privacyStatementHistory";
    public static final String COLLECTION = "users";

    @Property(TOKEN)
    private String token;
    @Property(IS_ADMIN)
    private boolean isAdmin;
    @Id
    private String name;
    @Indexed
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
    @Embedded(TERMS_OF_USE_HISTORY)
    private List<TermsHistoryItem> termsOfUseHistory;
    @Property(PRIVACY_STATEMENT_VERSION)
    private String privacyStatementVersion;
    @Embedded(PRIVACY_STATEMENT_HISTORY)
    private List<TermsHistoryItem> privacyStatementHistory;

    @Indexed(
            options = @IndexOptions(
                    unique = true,
                    partialFilter = "{confirmationCode:{$type: \"string\"}}"
            )
    )
    @Property(CONFIRMATION_CODE)
    private String confirmationCode;
    @Indexed(options = @IndexOptions(expireAfterSeconds = 0))
    @Property(EXPIRE_AT)
    private DateTime expireAt;

    @Transient
    private boolean hasAcceptedTermsOfUse = true;
    @Transient
    private boolean hasAcceptedPrivacyStatement = true;

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
        return this.mail;
    }

    @Override
    public void setMail(String mail) {
        this.mail = mail;
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
        return this.isAdmin;
    }

    @Override
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Set<Key<MongoUser>> getFriends() {
        return this.friends == null ? null : Collections.unmodifiableSet(this.friends);
    }

    @Override
    public String toString() {
        return toStringHelper()
                       .add(NAME, this.name)
                       .add(MAIL, this.mail)
                       .add(IS_ADMIN, this.isAdmin)
                       .add(FRIENDS, this.friends).toString();
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
        MongoUser other = (MongoUser) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getCountry() {
        return this.country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public Geometry getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Geometry location) {
        this.location = location;
    }

    @Override
    public String getAboutMe() {
        return this.aboutMe;
    }

    @Override
    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String getDayOfBirth() {
        return this.dayOfBirth;
    }

    @Override
    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    @Override
    public Gender getGender() {
        return this.gender;
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String getLanguage() {
        return this.language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public Set<String> getBadges() {
        return this.badges == null ? null : Collections.unmodifiableSet(this.badges);
    }

    @Override
    public String getTermsOfUseVersion() {
        //acceptedTermsOfUseVersion kept for backwards compatibility with older users
        return this.termsOfUseVersion != null ? this.termsOfUseVersion : this.acceptedTermsOfUseVersion;
    }

    @Override
    public void setTermsOfUseVersion(String tou) {
        this.termsOfUseVersion = tou;
    }

    @Override
    public String getConfirmationCode() {
        return this.confirmationCode;
    }

    @Override
    public void setConfirmationCode(String code) {
        this.confirmationCode = code;
    }

    @Override
    public void setExpirationDate(DateTime expireAt) {
        this.expireAt = expireAt;
    }

    @Override
    public DateTime getExpirationDate() {
        return this.expireAt;
    }

    @Override
    public boolean hasAcceptedTermsOfUse() {
        return this.hasAcceptedTermsOfUse;
    }

    @Override
    public boolean hasAcceptedPrivacyStatement() {
        return this.hasAcceptedPrivacyStatement;
    }

    @Override
    public void setAcceptedTermsOfUse(boolean hasAcceptedTermsOfUse) {
        this.hasAcceptedTermsOfUse = hasAcceptedTermsOfUse;
    }

    @Override
    public void setAcceptedPrivacyStatement(boolean hasAcceptedPrivacyStatement) {
        this.hasAcceptedPrivacyStatement = hasAcceptedPrivacyStatement;
    }

    @Override
    public void setPrivacyStatementVersion(String privacyStatementVersion) {
        this.privacyStatementVersion = privacyStatementVersion;
    }

    @Override
    public String getPrivacyStatementVersion() {
        return this.privacyStatementVersion;
    }

    @Override
    public List<TermsHistoryItem> getTermsOfUseHistory() {
        return Optional.ofNullable(this.termsOfUseHistory).orElseGet(Collections::emptyList);
    }

    @Override
    public void setTermsOfUseHistory(List<TermsHistoryItem> history) {
        this.termsOfUseHistory = Objects.requireNonNull(history);
    }

    @Override
    public void addTermsOfUseHistoryItem(TermsHistoryItem item) {
        if (this.termsOfUseHistory == null) {
            this.termsOfUseHistory = new LinkedList<>();
        }
        this.termsOfUseHistory.add(Objects.requireNonNull(item));
    }

    @Override
    public List<TermsHistoryItem> getPrivacyStatementHistory() {
        return Optional.ofNullable(this.privacyStatementHistory).orElseGet(Collections::emptyList);
    }

    @Override
    public void setPrivacyStatementHistory(List<TermsHistoryItem> history) {
        this.privacyStatementHistory = Objects.requireNonNull(history);
    }

    @Override
    public void addPrivacyStatementHistoryItem(TermsHistoryItem item) {
        if (this.privacyStatementHistory == null) {
            this.privacyStatementHistory = new LinkedList<>();
        }
        this.privacyStatementHistory.add(Objects.requireNonNull(item));
    }
}

