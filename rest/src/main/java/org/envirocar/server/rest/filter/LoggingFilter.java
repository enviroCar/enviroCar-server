/*
 * Copyright (C) 2013-2020 The enviroCar project
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

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.UriInfo;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String ID_PROPERTY = LoggingFilter.class.getName() + "-request-id";
    private static final String TIME_PROPERTY = LoggingFilter.class.getName() + "-request-time";
    private final Provider<HttpContext> httpContext;
    private final Provider<UriInfo> uriInfo;
    private final AtomicLong reqId = new AtomicLong();

    @Inject
    public LoggingFilter(Provider<HttpContext> httpContext,
                         Provider<UriInfo> uriInfo) {
        this.httpContext = httpContext;
        this.uriInfo = uriInfo;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        setStartTime();
        log(String.format("[%d]> %s %s", setId(), request.getMethod(), uriInfo.get().getRequestUri()));
        return request;
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        getId().map(id -> String.format("[%d]< %s %s: %d (%s)", id,
                                        request.getMethod(),
                                        uriInfo.get().getRequestUri(),
                                        response.getStatus(),
                                        getDuration().orElse(null)))
               .ifPresent(this::log);
        return response;
    }

    private Optional<Duration> getDuration() {
        return getStartTime().map(start -> Duration.between(start, OffsetDateTime.now()));
    }

    private long setId() {
        long id = reqId.getAndIncrement();
        getContext().put(ID_PROPERTY, id);
        return id;
    }

    private Optional<Long> getId() {
        return Optional.ofNullable(getContext().get(ID_PROPERTY)).map(Long.class::cast);
    }

    private void setStartTime() {
        getContext().put(TIME_PROPERTY, OffsetDateTime.now());
    }

    private Optional<OffsetDateTime> getStartTime() {
        return Optional.ofNullable(getContext().get(TIME_PROPERTY)).map(OffsetDateTime.class::cast);
    }

    private Map<String, Object> getContext() {
        return this.httpContext.get().getProperties();
    }

    public void log(String message) {
        LOG.info(message);
    }

}
