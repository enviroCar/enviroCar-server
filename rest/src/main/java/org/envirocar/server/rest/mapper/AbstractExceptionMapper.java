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
package org.envirocar.server.rest.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.util.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Collections;
import java.util.Map;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractExceptionMapper.class);


    @Override
    public Response toResponse(T exception) {
        log.info("Mapping exception", exception);
        String message = getMessage(exception);
        Response.StatusType status = getStatus(exception);
        JsonNode details = getDetails(exception);
        return Response.status(status)
                .type(MediaTypes.EXCEPTION_TYPE)
                .entity(new ErrorMessage(status, message, details, exception))
                .build();
    }

    protected abstract Response.StatusType getStatus(T exception);

    protected JsonNode getDetails(T exception) {
        return null;
    }

    protected Map<String, String> getAdditionalHeaders() {
        return Collections.emptyMap();
    }

    private String getMessage(T exception) {
        String message = null;
        for (Throwable t = exception; t != null && (message == null || message.isEmpty()); t = t.getCause()) {
            message = t.getMessage();
        }
        return message;
    }

}
