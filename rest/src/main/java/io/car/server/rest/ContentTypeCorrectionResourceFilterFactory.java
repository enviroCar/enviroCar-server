/**
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.rest;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class ContentTypeCorrectionResourceFilterFactory implements ResourceFilterFactory {
    private static final Logger log = LoggerFactory.getLogger(ContentTypeCorrectionResourceFilterFactory.class);

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        MediaType type = getRequestType(am);
        if (type != null) {
            return Collections.<ResourceFilter>singletonList(new ContentTypeCorrectionResourceFilter(type));
        }
        return Collections.emptyList();
    }

    protected MediaType getRequestType(AbstractMethod am) throws IllegalArgumentException {
        Consumes consumes = am.getAnnotation(Consumes.class);
        if (consumes != null) {
            String[] mediatypes = consumes.value();
            if (mediatypes.length > 1) {
                log.warn("Request validation for {} is disabled! Only one consumable media type is supported", am);
            } else {
                for (String mediaType : mediatypes) {
                    MediaType mt = MediaType.valueOf(mediaType);
                    if (mt.getType().equals(MediaType.APPLICATION_JSON_TYPE.getType()) &&
                        mt.getSubtype().equals(MediaType.APPLICATION_JSON_TYPE.getSubtype())) {
                        return mt;
                    }
                }
            }
        }
        return null;
    }

    private class ContentTypeCorrectionResourceFilter implements ResourceFilter {
        private MediaType type;

        ContentTypeCorrectionResourceFilter(MediaType type) {
            this.type = type;
        }

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return new ContentTypeCorrectionRequestFilter(type);
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return null;
        }
    }

    private class ContentTypeCorrectionRequestFilter implements ContainerRequestFilter {
        private MediaType type;

        ContentTypeCorrectionRequestFilter(MediaType type) {
            this.type = type;
        }

        @Override
        public ContainerRequest filter(ContainerRequest request) {
            // container request caches the header......
            request.getRequestHeaders().remove(HttpHeaders.CONTENT_TYPE);
            request.getMediaType();
            request.getRequestHeaders().putSingle(HttpHeaders.CONTENT_TYPE, type.toString());
            return request;
        }
    }
}
