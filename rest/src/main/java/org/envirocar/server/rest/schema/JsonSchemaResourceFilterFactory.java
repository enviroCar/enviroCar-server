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
package org.envirocar.server.rest.schema;

import com.google.inject.Inject;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

import javax.inject.Provider;
import java.net.URI;
import java.util.*;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JsonSchemaResourceFilterFactory implements ResourceFilterFactory {
    private final JsonSchemaValidationFilter schemaValidationFilter;
    private final Provider<JsonSchemaUriConfiguration> schemaUriConfiguration;

    @Inject
    public JsonSchemaResourceFilterFactory(JsonSchemaValidationFilter schemaValidationFilter,
                                           Provider<JsonSchemaUriConfiguration> schemaUriConfiguration) {
        this.schemaValidationFilter = Objects.requireNonNull(schemaValidationFilter);
        this.schemaUriConfiguration = Objects.requireNonNull(schemaUriConfiguration);
    }

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        return Optional.ofNullable(am.getAnnotation(Schema.class))
                .map(this::create).orElseGet(Collections::emptyList);
    }

    public List<ResourceFilter> create(Schema annotation) {
        URI request = Optional.of(annotation.request()).filter(this::isNotEmpty).map(URI::create).orElse(null);
        URI response = Optional.of(annotation.response()).filter(this::isNotEmpty).map(URI::create).orElse(null);
        if (request == null && response == null) return null;
        ResourceFilter filter = new JsonSchemaMediaTypeResourceFilter(request, response, schemaUriConfiguration);
        return Arrays.asList(filter, schemaValidationFilter);
    }

    private boolean isNotEmpty(String schema) {
        return !schema.isEmpty();
    }
}
