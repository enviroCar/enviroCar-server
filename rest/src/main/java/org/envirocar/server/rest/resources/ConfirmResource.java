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
package org.envirocar.server.rest.resources;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.rest.RESTConstants;
import org.envirocar.server.rest.auth.Anonymous;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ConfirmResource extends AbstractResource {

    private static final URI WEBSITE = URI.create("https://envirocar.org");
    private static final URI APP = URI.create("https://envirocar.org/app");

    @GET
    @Anonymous
    public Response confirm(@QueryParam(RESTConstants.USER) String username,
                            @QueryParam(RESTConstants.CODE) String confirmationCode) throws BadRequestException {
        boolean confirmed = getUserService().confirmUser(username, confirmationCode);

        URI uri = WEBSITE;

        if (confirmed) {
            uri = UriBuilder.fromUri(APP)
                    .queryParam("username", username)
                    .fragment("#!/login")
                    .build();
        }

        return Response.seeOther(uri).build();
    }
}