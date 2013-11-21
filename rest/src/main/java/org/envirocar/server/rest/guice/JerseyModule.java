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
package org.envirocar.server.rest.guice;

import java.util.Map;

import org.envirocar.server.rest.PaginationFilter;
import org.envirocar.server.rest.auth.AuthenticationFilter;
import org.envirocar.server.rest.auth.AuthenticationResourceFilterFactory;
import org.envirocar.server.rest.validation.JSONSchemaResourceFilterFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyModule extends JerseyServletModule {
    private static final String TRUE = String.valueOf(true);
    private static final String FALSE = String.valueOf(false);

    @Override
    protected void configureServlets() {
        serve("/*").with(GuiceContainer.class, getContainerFilterConfig());
    }

    protected Map<String, String> getContainerFilterConfig() {
        return ImmutableMap.<String, String>builder()
                .put(ResourceConfig.FEATURE_DISABLE_WADL, "false")
                .put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, classList(requestFilters()))
                .put(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, classList(responseFilters()))
                .put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES, classList(filterFactories()))
                .build();
    }

    private String classList(Iterable<? extends Class<?>> classes) {
        return Joiner.on(",").join(Iterables.transform(classes, new Function<Class<?>, String>() {
            @Override public String apply(Class<?> type) { return type.getName(); }
        }));
    }

    protected ImmutableList<Class<? extends ContainerResponseFilter>> responseFilters() {
        return ImmutableList.of(PaginationFilter.class, GZIPContentEncodingFilter.class);
    }

    protected ImmutableList<Class<? extends ContainerRequestFilter>> requestFilters() {
        return ImmutableList.of(GZIPContentEncodingFilter.class, AuthenticationFilter.class);
    }

    protected ImmutableList<Class<? extends ResourceFilterFactory>> filterFactories() {
        return ImmutableList.of(AuthenticationResourceFilterFactory.class, JSONSchemaResourceFilterFactory.class);
    }
}
