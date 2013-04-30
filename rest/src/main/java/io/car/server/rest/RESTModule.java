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

import com.google.common.collect.ImmutableMap;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import io.car.server.rest.auth.AuthenticationFilter;
import io.car.server.rest.auth.AuthenticationResourceFilterFactory;
import io.car.server.rest.resources.FriendsResource;
import io.car.server.rest.resources.UserResource;
import io.car.server.rest.resources.UsersResource;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class RESTModule extends JerseyServletModule {
    public static final String FALSE = String.valueOf(false);
    public static final String TRUE = String.valueOf(true);

    @Override
    protected void configureServlets() {
        install(new FactoryModuleBuilder()
                .implement(UserResource.class, UserResource.class)
                .implement(UsersResource.class, UsersResource.class)
                .implement(FriendsResource.class, FriendsResource.class)
                .build(ResourceFactory.class));
        serve("/schema/*").with(SchemaServlet.class);
        serve("/rest*").with(GuiceContainer.class, ImmutableMap.<String, String>builder()
                .put(PackagesResourceConfig.PROPERTY_PACKAGES,
                     getClass().getPackage().getName())
                .put(ResourceConfig.FEATURE_DISABLE_WADL, TRUE)
                .put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS,
                     classList(AuthenticationFilter.class,
                               GZIPContentEncodingFilter.class))
                .put(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS,
                     classList(GZIPContentEncodingFilter.class))
                .put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
                     classList(AuthenticationResourceFilterFactory.class))
                .put(FeaturesAndProperties.FEATURE_FORMATTED, TRUE)
                .put(JSONConfiguration.FEATURE_POJO_MAPPING, FALSE)
                .build());
    }

    private String classList(Class<?> clazz, Class<?>... classes) {
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getName());
        for (Class<?> c : classes) {
            sb.append(",").append(c.getName());
        }
        return sb.toString();
    }
}
