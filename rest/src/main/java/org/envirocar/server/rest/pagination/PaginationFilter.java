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
package org.envirocar.server.rest.pagination;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.core.header.LinkHeader;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import org.envirocar.server.core.util.pagination.Paginated;
import org.envirocar.server.core.util.pagination.Pagination;

import com.google.common.base.Optional;

import org.envirocar.server.rest.RESTConstants;

public class PaginationFilter implements ContainerResponseFilter {
    public static final String REL_FIRST = "first";
    public static final String REL_LAST = "last";
    public static final String REL_PREV = "prev";
    public static final String REL_NEXT = "next";
    public static final String LINK_HEADER = "Link";
    public static final String CONTENT_RANGE_HEADER = "Content-Range";

    @Override
    public ContainerResponse filter(ContainerRequest req,
                                    ContainerResponse res) {
        Object entity = res.getEntity();
        if (entity instanceof Paginated) {
            Paginated<?> p = (Paginated) entity;
            if (p.isPaginated()) {
                insertLinks(p, req, res);
                insertRangeHeader(p, res);
            }
        }
        return res;
    }

    private void insertRangeHeader(Paginated<?> p,
                                   ContainerResponse res) {
        StringBuilder sb = new StringBuilder("items ");
        sb.append(p.getCurrent().get().getBegin());
        sb.append("-");
        sb.append(p.getCurrent().get().getEnd());
        if (p.getTotalCount()>=0) {
            sb.append("/").append(p.getTotalCount());
        }
        res.getHttpHeaders().add(CONTENT_RANGE_HEADER, sb.toString());
    }


    private void insertLinks(Paginated<?> p,
                             ContainerRequest req,
                             ContainerResponse res) {
        Optional<Pagination> first = p.getFirst();
        if (first.isPresent()) {
            addLink(REL_FIRST, first.get(), req, res);
        }
        Optional<Pagination> last = p.getLast();
        if (last.isPresent()) {
            addLink(REL_LAST, last.get(), req, res);
        }
        Optional<Pagination> previous = p.getPrevious();
        if (previous.isPresent()) {
            addLink(REL_PREV, previous.get(), req, res);
        }
        Optional<Pagination> next = p.getNext();
        if (next.isPresent()) {
            addLink(REL_NEXT, next.get(), req, res);
        }
    }

    protected void addLink(String rel, Pagination p, ContainerRequest req,
                           ContainerResponse res) {
        if (p.getPage()>= 0 && p.getLimit() >= 0) {
            URI uri = req.getRequestUriBuilder()
                    .replaceQueryParam(RESTConstants.LIMIT, p.getLimit())
                    .replaceQueryParam(RESTConstants.PAGE, p.getPage())
                    .build();
            MediaType type = res.getMediaType();
            LinkHeader header = LinkHeader.uri(uri).type(type).rel(rel).build();
            res.getHttpHeaders().add(LINK_HEADER, header.toString());
        }
    }
}
