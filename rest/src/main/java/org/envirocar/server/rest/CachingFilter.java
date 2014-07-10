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

import java.util.Date;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.envirocar.server.core.entities.BaseEntity;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CachingFilter implements ContainerResponseFilter {

    @Override
    public ContainerResponse filter(ContainerRequest request,
                                    ContainerResponse response) {
        Object entity = response.getEntity();
        if (entity instanceof BaseEntity) {
            Date lastModified = getLastModificationTime((BaseEntity) entity);
            if (lastModified != null) {

                ResponseBuilder b = request.evaluatePreconditions(lastModified);
                if (b != null) {
                    response.setResponse(b.build());
                } else {
                    MultivaluedMap<String, Object> headers = response.getHttpHeaders();
                    headers.putSingle(HttpHeaders.LAST_MODIFIED, lastModified);
                }
            }
        }
        return response;
    }

    protected void addLastModifiedHeader(Object entity,
                                         ContainerResponse response) {

    }

    protected Date getLastModificationTime(BaseEntity entity) {
        if (entity.hasModificationTime()) {
            return entity.getModificationTime().toDate();
        } else if (entity.hasCreationTime()) {
            return entity.getCreationTime().toDate();
        } else {
            return null;
        }
    }

}
