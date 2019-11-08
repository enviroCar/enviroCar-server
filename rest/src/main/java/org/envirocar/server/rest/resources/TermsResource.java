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
package org.envirocar.server.rest.resources;

import org.envirocar.server.core.entities.Terms;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;
import java.util.Locale;

public class TermsResource extends AbstractResource {
    private Provider<HttpHeaders> httpHeaders;

    protected HttpHeaders getHttpHeaders() {
        return httpHeaders.get();
    }

    protected List<Locale> getAcceptableLanguages() {
        return getHttpHeaders().getAcceptableLanguages();
    }

    @Inject
    public void setHttpHeaders(Provider<HttpHeaders> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    protected <T extends Terms> T setContents(T entity) {
        entity.setContents(getAcceptableLanguages()
                                   .stream()
                                   .map(Locale::getLanguage)
                                   .filter(entity.getTranslations()::containsKey)
                                   .map(entity.getTranslations()::get)
                                   .findFirst()
                                   .orElse(entity.getContents()));
        return entity;
    }
}
