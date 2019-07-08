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
package org.envirocar.server.rest.guice;

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.*;
import org.envirocar.server.rest.CachingFilter;
import org.envirocar.server.rest.URIContentNegotiationFilter;
import org.envirocar.server.rest.auth.AuthenticationFilter;
import org.envirocar.server.rest.auth.AuthenticationResourceFilterFactory;
import org.envirocar.server.rest.pagination.PaginationFilter;
import org.envirocar.server.rest.rights.HasAcceptedLatestLegalPoliciesResourceFilterFactory;
import org.envirocar.server.rest.schema.JsonSchemaResourceFilterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.joining;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyModule extends JerseyServletModule {
    @Override
    protected void configureServlets() {
        serve("/*").with(GuiceContainer.class, getContainerFilterConfig());
    }

    private Map<String, String> getContainerFilterConfig() {
        return ImmutableMap.<String, String>builder()
                .put(ResourceConfig.FEATURE_DISABLE_WADL, "false")
                .put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, classList(requestFilters()))
                .put(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, classList(responseFilters()))
                .put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES, classList(filterFactories()))
                .build();
    }

    private String classList(Iterable<? extends Class<?>> classes) {
        return StreamSupport.stream(classes.spliterator(), false).map(Class::getName).collect(joining(","));
    }

    private List<Class<? extends ContainerResponseFilter>> responseFilters() {
        return Arrays.asList(
                LoggingFilter.class,
                CachingFilter.class,
                PaginationFilter.class,
                GZIPContentEncodingFilter.class);
    }

    private List<Class<? extends ContainerRequestFilter>> requestFilters() {
        return Arrays.asList(
                LoggingFilter.class,
                GZIPContentEncodingFilter.class,
                URIContentNegotiationFilter.class,
                AuthenticationFilter.class);
    }

    private List<Class<? extends ResourceFilterFactory>> filterFactories() {
        return Arrays.asList(
                AuthenticationResourceFilterFactory.class,
                HasAcceptedLatestLegalPoliciesResourceFilterFactory.class,
                JsonSchemaResourceFilterFactory.class);
    }

    private static class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
        private static final Logger LOG = LoggerFactory.getLogger(LoggingFilter.class);
        private final Provider<HttpContext> httpContext;
        private final AtomicLong reqId = new AtomicLong();

        @Inject
        private LoggingFilter(Provider<HttpContext> httpContext) {
            this.httpContext = httpContext;
        }

        @Override
        public ContainerRequest filter(ContainerRequest request) {
            long id = reqId.getAndIncrement();
            Map<String, Object> contextProps = this.httpContext.get().getProperties();
            contextProps.put("logging-filter-request-id", id);
            contextProps.put("logging-filter-request-time", OffsetDateTime.now());
            String message = String.format("[%d]> %s %s", id,
                    request.getMethod(),
                    request.getRequestUri());
            log(message);
            return request;
        }


        @Override
        public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
            Map<String, Object> contextProps = this.httpContext.get().getProperties();
            Long id = (Long) contextProps.get("logging-filter-request-id");
            if (id != null) {
                OffsetDateTime time = (OffsetDateTime) contextProps.get("logging-filter-request-time");
                String message = String.format("[%d]< %s %s: %d (took %s)", id,
                        request.getMethod(),
                        request.getRequestUri(),
                        response.getStatus(),
                        Duration.between(time, OffsetDateTime.now()));
                log(message);
            }
            return response;
        }

        public void log(String message) {
            LOG.info(message);
        }

    }

}
