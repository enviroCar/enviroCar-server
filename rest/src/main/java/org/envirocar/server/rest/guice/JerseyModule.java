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
package org.envirocar.server.rest.guice;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.util.Modules;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.core.ExtendedUriInfo;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;
import com.sun.jersey.spi.container.WebApplication;
import org.envirocar.server.rest.PrefixedUriInfo;
import org.envirocar.server.rest.auth.AuthenticationFilter;
import org.envirocar.server.rest.auth.AuthenticationResourceFilterFactory;
import org.envirocar.server.rest.filter.ApiDocsLinkFilter;
import org.envirocar.server.rest.filter.CachingFilter;
import org.envirocar.server.rest.filter.LoggingFilter;
import org.envirocar.server.rest.filter.URIContentNegotiationFilter;
import org.envirocar.server.rest.pagination.PaginationFilter;
import org.envirocar.server.rest.rights.HasAcceptedLatestLegalPoliciesResourceFilterFactory;
import org.envirocar.server.rest.schema.JsonSchemaResourceFilterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.StreamSupport;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(JerseyModule.class);
    private static final String FILE_EXTENSIONS_PROPERTIES = "/file-extensions.properties";

    @Override
    protected void configure() {
        install(Modules.override(new JerseyModuleImpl()).with(new JerseyOverrideModule()));
    }

    @Provides
    public Map<String, MediaType> getMapping() {
        Properties properties = new Properties();
        InputStream stream = URIContentNegotiationFilter.class.getResourceAsStream(FILE_EXTENSIONS_PROPERTIES);
        if (stream == null) {
            LOG.warn("no file extensions found");
        } else {
            try (InputStream inputStream = stream;
                 InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                properties.load(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        return properties.stringPropertyNames().stream()
                         .collect(toMap(identity(), ex -> MediaType.valueOf(properties.getProperty(ex))));
    }

    private static class JerseyModuleImpl extends JerseyServletModule {
        @Override
        protected void configureServlets() {
            serve("/*").with(GuiceContainer.class, getContainerFilterConfig());
        }

        private Map<String, String> getContainerFilterConfig() {
            return ImmutableMap.<String, String>builder()
                               .put(ResourceConfig.FEATURE_DISABLE_WADL, "true")
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
                    ApiDocsLinkFilter.class,
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
    }

    private static class JerseyOverrideModule extends AbstractModule {
        @Provides
        @RequestScoped
        public UriInfo uriInfo(WebApplication wa) {
            HttpContext context = wa.getThreadLocalHttpContext();
            ExtendedUriInfo uriInfo = context.getUriInfo();
            HttpHeaders headers = context.getRequest();
            return new PrefixedUriInfo(uriInfo, headers);
        }
    }
}
