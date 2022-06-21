/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.ResourceNotFoundException;
import org.envirocar.server.rest.auth.Anonymous;
import org.envirocar.server.rest.resources.AbstractResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ConfirmResource extends AbstractResource {
    public static final String CODE = "{code}";
    private static final URI WEBSITE = URI.create("https://envirocar.org");
    private static final URI APP = WEBSITE.resolve("/app");

    @GET
    @Anonymous
    @Path(CODE)
    public Response confirm(@PathParam("code") String confirmationCode)
            throws BadRequestException, ResourceNotFoundException {
        User confirmed = getUserService().confirmUser(confirmationCode);

        if (confirmed == null) {
            throw new ResourceNotFoundException(String.format("confirmation code %s not found", confirmationCode));
        }
        try {
            URI uri = UriBuilder.fromUri(APP)
                    .fragment("#!/login?username=" + URLEncoder
                            .encode(confirmed.getName(), StandardCharsets.UTF_8.name()))
                    .build();
            return Response.seeOther(uri).build();
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

    }
}
