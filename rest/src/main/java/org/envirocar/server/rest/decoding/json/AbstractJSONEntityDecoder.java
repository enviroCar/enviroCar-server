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
package org.envirocar.server.rest.decoding.json;

import org.envirocar.server.core.entities.EntityFactory;
import org.joda.time.format.DateTimeFormatter;

import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractJSONEntityDecoder<T>
        extends AbstractJSONMessageBodyReader<T>
        implements JSONEntityDecoder<T> {
    private DateTimeFormatter dateTimeFormat;
    private EntityFactory entityFactory;

    public AbstractJSONEntityDecoder(Class<T> type) {
        super(type);
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    @Inject
    public void setEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    public DateTimeFormatter getDateTimeFormat() {
        return dateTimeFormat;
    }

    @Inject
    public void setDateTimeFormat(DateTimeFormatter dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }
}
