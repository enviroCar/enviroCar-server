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
package org.envirocar.server.rest.filter;

import com.sun.jersey.core.header.LinkHeader;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.resources.RootResource;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ApiDocsLinkFilter implements ContainerResponseFilter {

    private static final String LINK_HEADER = "Link";
    private static final String LINK_REL = "service-desc";
    private final Provider<UriInfo> uriInfo;

    @Inject
    public ApiDocsLinkFilter(Provider<UriInfo> uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        URI uri = this.uriInfo.get().getBaseUriBuilder().path(RootResource.API_DOCS).build();
        response.getHttpHeaders()
                .add(LINK_HEADER, LinkHeader.uri(uri).type(MediaTypes.YAML_TYPE).rel(LINK_REL).build().toString());
        response.getHttpHeaders()
                .add(LINK_HEADER, LinkHeader.uri(uri).type(MediaTypes.JSON_TYPE).rel(LINK_REL).build().toString());
        return response;
    }
}
