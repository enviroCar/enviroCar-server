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
package org.envirocar.server.rest.util;

import javax.ws.rs.core.Response;
import java.util.Optional;

public class CustomStatus implements Response.StatusType {
    private final int statusCode;
    private final String reasonPhrase;

    public CustomStatus(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = Optional.ofNullable(reasonPhrase)
                .filter(x -> !x.isEmpty()).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public Response.Status.Family getFamily() {
        switch (this.statusCode / 100) {
            case 1:
                return Response.Status.Family.INFORMATIONAL;
            case 2:
                return Response.Status.Family.SUCCESSFUL;
            case 3:
                return Response.Status.Family.REDIRECTION;
            case 4:
                return Response.Status.Family.CLIENT_ERROR;
            case 5:
                return Response.Status.Family.SERVER_ERROR;
            default:
                return Response.Status.Family.OTHER;
        }
    }

    @Override
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
}
