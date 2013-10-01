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
package org.envirocar.server.rest;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.core.header.LinkHeader;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import org.envirocar.server.core.util.PaginatedIterable;
import org.envirocar.server.core.util.Pagination;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class PaginationFilter implements ContainerResponseFilter {
    public static final String REL_FIRST = "first";
    public static final String REL_LAST = "last";
    public static final String REL_PREV = "prev";
    public static final String REL_NEXT = "next";
    public static final String LINK_HEADER = "Link";

    @Override
    public ContainerResponse filter(ContainerRequest req,
                                    ContainerResponse res) {
        Object entity = res.getEntity();
        if (entity instanceof PaginatedIterable) {
            PaginatedIterable<?> p = (PaginatedIterable) entity;
            if (p.isPaginated()) {
                insertLinks(p, req, res);
            }
        }
        return res;
    }

    private void insertLinks(PaginatedIterable<?> p,
                             ContainerRequest req,
                             ContainerResponse res) {
        if (p.hasFirst()) {
            addLink(REL_FIRST, p.getFirst(), req, res);
        }
        if (p.hasLast()) {
            addLink(REL_LAST, p.getLast(), req, res);
        }
        if (p.hasPrevious()) {
            addLink(REL_PREV, p.getPrevious(), req, res);
        }
        if (p.hasNext()) {
            addLink(REL_NEXT, p.getNext(), req, res);
        }
    }

    protected void addLink(String rel, Pagination p, ContainerRequest req,
                           ContainerResponse res) {
        URI uri = req.getRequestUriBuilder()
                .replaceQueryParam(RESTConstants.LIMIT, p.getLimit())
                .replaceQueryParam(RESTConstants.PAGE, p.getPage())
                .build();
        MediaType type = res.getMediaType();
        LinkHeader header = LinkHeader.uri(uri).type(type).rel(rel).build();
        res.getHttpHeaders().add(LINK_HEADER, header.toString());
    }
}
