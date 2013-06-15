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

import javax.ws.rs.core.SecurityContext;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import io.car.server.core.Service;
import io.car.server.core.entities.User;
import io.car.server.rest.auth.PrincipalImpl;
import io.car.server.rest.mapper.IllegalModificationExceptionMapper;
import io.car.server.rest.mapper.JsonValidationExceptionMapper;
import io.car.server.rest.mapper.ResourceAlreadyExistExceptionMapper;
import io.car.server.rest.mapper.ResourceNotFoundExceptionMapper;
import io.car.server.rest.mapper.ValidationExceptionMapper;
import io.car.server.rest.resources.ResourceFactory;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.rights.AccessRights;
import io.car.server.rest.rights.AccessRightsImpl;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyResourceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IllegalModificationExceptionMapper.class).in(Scopes.SINGLETON);
        bind(ResourceNotFoundExceptionMapper.class).in(Scopes.SINGLETON);
        bind(ValidationExceptionMapper.class).in(Scopes.SINGLETON);
        bind(ResourceAlreadyExistExceptionMapper.class).in(Scopes.SINGLETON);
        bind(JsonValidationExceptionMapper.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(ResourceFactory.class));
        bind(RootResource.class);
    }

    @Provides
    public AccessRights accessRights(SecurityContext ctx,
                                     Service service) {
        PrincipalImpl p = (PrincipalImpl) ctx.getUserPrincipal();
        User user = p == null ? null : p.getUser();
        return new AccessRightsImpl(user, service);
    }
}
