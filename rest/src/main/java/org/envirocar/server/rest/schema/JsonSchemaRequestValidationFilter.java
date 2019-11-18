/*
 * Copyright (C) 2013-2019 The enviroCar project
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.rest.MediaTypes;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JsonSchemaRequestValidationFilter extends AbstractJsonSchemaValidationFilter
        implements ContainerRequestFilter {

    @Inject
    public JsonSchemaRequestValidationFilter(JsonSchemaFactory schemaFactory,
                                             JsonNodeCreator factory,
                                             ObjectReader reader) {
        super(schemaFactory, factory, reader);
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        if (!MediaTypes.hasSchemaAttribute(request.getMediaType())) {
            return request;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = request.getEntityInputStream();
        try {
            ReaderWriter.writeTo(in, out);
            byte[] requestEntity = out.toByteArray();
            request.setEntityInputStream(new ByteArrayInputStream(requestEntity));
            try {
                validate(requestEntity, request.getMediaType());
            } catch (JsonParseException e) {
                throw new BadRequestException(e);
            }
        } catch (IOException ex) {
            throw new ContainerException(ex);
        }
        return request;
    }
}
