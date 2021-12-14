/*
 * Copyright (C) 2013-2021 The enviroCar project
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
package org.envirocar.server.rest.schema;

import com.google.inject.Inject;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JsonSchemaResourceFilterFactory implements ResourceFilterFactory {
    private final JsonSchemaRequestValidationFilter requestValidationFilter;

    @Inject
    public JsonSchemaResourceFilterFactory(JsonSchemaRequestValidationFilter requestValidationFilter) {
        this.requestValidationFilter = Objects.requireNonNull(requestValidationFilter);
    }

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        URI request = getRequestSchema(am);

        List<ResourceFilter> filters = new ArrayList<>(3);
        if (request != null) {
            filters.add(this.requestValidationFilter);
        }
        return filters;
    }

    private URI getRequestSchema(AbstractMethod am) {
        return Optional.ofNullable(am.getAnnotation(Schema.class)).map(Schema::request)
                       .filter(value -> !value.isEmpty()).map(URI::create).orElse(null);
    }

}
