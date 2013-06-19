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

import javax.ws.rs.core.MediaType;

import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;

import io.car.server.rest.provider.AbstractMessageBodyWriter;
import io.car.server.rest.rights.AccessRights;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractEntityEncoder<T>
        extends AbstractMessageBodyWriter<T>
        implements EntityEncoder<T> {
    private JsonNodeFactory jsonFactory;
    private DateTimeFormatter dateTimeFormat;
    private Provider<AccessRights> rights;

    public AbstractEntityEncoder(Class<T> classType) {
        super(classType);
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

    @Inject
    public void setRights(Provider<AccessRights> rights) {
        this.rights = rights;
    }

    @Override
    public final ObjectNode encodeJSON(T t, MediaType mt) {
        return encodeJSON(t, rights.get(), mt);
    }

    @Override
    public final Model encodeRDF(T t, MediaType mt) {
        return encodeRDF(t, rights.get(), mt);
    }
}
