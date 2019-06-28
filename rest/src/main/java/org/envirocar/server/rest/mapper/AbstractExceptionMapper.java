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
package org.envirocar.server.rest.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class AbstractExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractExceptionMapper.class);
    private final Response.StatusType status;

    public AbstractExceptionMapper(Response.StatusType status) {
        this.status = status;
    }

    @Override
    public Response toResponse(T exception) {
        log.info("Mapping exception", exception);

        String stackTrace = getStackTrace(exception);
        String message = getMessage(exception);

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorMessage(status, message, stackTrace))
                .build();
    }

    private String getMessage(T exception) {
        String message = null;
        for (Throwable t = exception; t != null && (message == null || message.isEmpty()); t = t.getCause()) {
            message = t.getMessage();
        }
        return message;
    }

    private String getStackTrace(T exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }

}
