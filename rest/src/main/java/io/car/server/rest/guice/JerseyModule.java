/*
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
package io.car.server.rest.guice;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import io.car.server.rest.PaginationFilter;
import io.car.server.rest.SchemaServlet;
import io.car.server.rest.auth.AuthenticationFilter;
import io.car.server.rest.auth.AuthenticationResourceFilterFactory;
import io.car.server.rest.validation.JSONSchemaResourceFilterFactory;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyModule extends JerseyServletModule {
    public static final String FALSE = String.valueOf(false);
    public static final String TRUE = String.valueOf(true);

    private static String classList(Class<?> clazz, Class<?>... classes) {
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getName());
        for (Class<?> c : classes) {
            sb.append(",").append(c.getName());
        }
        return sb.toString();
    }

    public static Map<String, String> getContainerFilterConfig() {
        return ImmutableMap.of(
                ResourceConfig.FEATURE_DISABLE_WADL, TRUE,
                ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS,
                classList(GZIPContentEncodingFilter.class,
                          AuthenticationFilter.class),
                ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS,
                classList(PaginationFilter.class,
                          GZIPContentEncodingFilter.class),
                ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
                classList(AuthenticationResourceFilterFactory.class,
                          JSONSchemaResourceFilterFactory.class));
    }

    @Override
    protected void configureServlets() {
        serve("/rest*").with(GuiceContainer.class, getContainerFilterConfig());
        serve("/schema/*").with(SchemaServlet.class);
    }
}
