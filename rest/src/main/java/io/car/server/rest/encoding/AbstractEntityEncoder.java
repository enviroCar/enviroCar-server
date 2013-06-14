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

import javax.ws.rs.core.UriInfo;

import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.inject.Inject;

import io.car.server.core.Service;
import io.car.server.rest.rights.AccessRights;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractEntityEncoder<T> implements EntityEncoder<T> {
    private UriInfo uriInfo;
    private JsonNodeFactory jsonFactory;
    private DateTimeFormatter dateTimeFormat;
    private Service service;
    private AccessRights rights;

    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @Inject
    public void setUriInfo(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public JsonNodeFactory getJsonFactory() {
        return jsonFactory;
    }

    @Inject
    public void setJsonFactory(JsonNodeFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    public DateTimeFormatter getDateTimeFormat() {
        return dateTimeFormat;
    }

    @Inject
    public void setDateTimeFormat(DateTimeFormatter dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public Service getService() {
        return service;
    }

    @Inject
    public void setService(Service service) {
        this.service = service;
    }

    public AccessRights getRights() {
        return rights;
    }

    @Inject
    public void setRights(AccessRights rights) {
        this.rights = rights;
    }
}
