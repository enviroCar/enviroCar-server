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
package org.envirocar.server.rest;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import org.envirocar.server.core.ConfirmationLinkFactory;
import org.envirocar.server.core.entities.User;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.envirocar.server.rest.resources.ConfirmResource;
import org.envirocar.server.rest.resources.RootResource;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@RequestScoped
public class ConfirmationLinkFactoryImpl implements ConfirmationLinkFactory {

    private final UriInfo uriInfo;

    @Inject
    public ConfirmationLinkFactoryImpl(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public URI getConfirmationLink(User user) {
        return uriInfo.getBaseUriBuilder()
                .path(RootResource.CONFIRM)
                .path(ConfirmResource.CODE)
                .build(user.getConfirmationCode());
    }

}
