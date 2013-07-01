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
package org.envirocar.server.rest.resources;

import java.net.URI;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.MediaTypes;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class AvatarResource extends AbstractResource {
    private static final String GRAVATAR_BASE_URL =
            "https://secure.gravatar.com/avatar/";
    public static final String SIZE = "size";
    public static final String DEFAULT_SIZE = "80";
    private final User user;

    @Inject
    public AvatarResource(@Assisted User user) {
        this.user = user;
    }

    @GET
    @Produces({ MediaTypes.IMAGE_JPEG })
    public Response get(@QueryParam(SIZE) @DefaultValue(DEFAULT_SIZE) int size) {
        return Response.temporaryRedirect(getURI(user, size)).build();
    }

    protected URI getURI(User user, int size) {
        String mail = user.hasMail() ? user.getMail() : "";
        String hash = DigestUtils.md5Hex(mail.trim().toLowerCase());
        return UriBuilder.fromUri(GRAVATAR_BASE_URL)
                .path(hash).queryParam(SIZE, size).build();
    }
}
