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

import com.sun.jersey.core.spi.factory.ResponseImpl;
import org.envirocar.server.rest.util.CustomStatus;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
@Singleton
public class WebApplicationExceptionMapper extends AbstractExceptionMapper<WebApplicationException> {

    private static final String UNKNOWN_REASON_PHRASE = "Unknown";


    @Override
    protected StatusType getStatus(WebApplicationException exception) {
        Response response = exception.getResponse();
        if (response instanceof ResponseImpl) {
            return ((ResponseImpl) response).getStatusType();
        }
        int statusCode = response.getStatus();
        for (StatusType status : Status.values()) {
            if (status.getStatusCode() == statusCode) {
                return status;
            }
        }

        return new CustomStatus(statusCode, UNKNOWN_REASON_PHRASE);
    }
}
