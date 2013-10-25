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
package org.envirocar.server.core.update;

import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.util.PasswordEncoder;

import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UserUpdater implements EntityUpdater<User> {
    private final PasswordEncoder encoder;

    @Inject
    public UserUpdater(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void update(User changes, User original) {
        if (changes.getMail() != null) {
            original.setMail(changes.getMail());
        }
        if (changes.getToken() != null) {
            original.setToken(encoder.encode(changes.getToken()));
        }
        if (changes.getAboutMe() != null) {
            if (changes.getAboutMe().isEmpty()) {
                original.setAboutMe(null);
            } else {
                original.setAboutMe(changes.getAboutMe());
            }
        }
        if (changes.getCountry() != null) {
            if (changes.getCountry().isEmpty()) {
                original.setCountry(null);
            } else {
                original.setCountry(changes.getCountry());
            }
        }
        if (changes.getDayOfBirth() != null) {
            if (changes.getDayOfBirth().isEmpty()) {
                original.setDayOfBirth(null);
            } else {
                original.setDayOfBirth(changes.getDayOfBirth());
            }
        }
        if (changes.getFirstName() != null) {
            if (changes.getFirstName().isEmpty()) {
                original.setFirstName(null);
            } else {
                original.setFirstName(changes.getFirstName());
            }
        }
        if (changes.getGender() != null) {
            original.setGender(changes.getGender());
        }
        if (changes.getLanguage() != null) {
            if (changes.getLanguage().isEmpty()) {
                original.setLanguage(null);
            } else {
                original.setLanguage(changes.getLanguage());
            }
        }
        if (changes.getLastName() != null) {
            if (changes.getLastName().isEmpty()) {
                original.setLastName(null);
            } else {
                original.setLastName(changes.getLastName());
            }
        }
        if (changes.getLocation() != null) {
            original.setLocation(changes.getLocation());
        }
        if (changes.getUrl() != null) {
            original.setUrl(changes.getUrl());
        }
        if (changes.getTermsOfUseVersion() != null) {
        	original.setTermsOfUseVersion(changes.getTermsOfUseVersion());
        }
    }
}
