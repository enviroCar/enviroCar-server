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

import java.util.Set;

import javax.ws.rs.core.SecurityContext;

import org.envirocar.server.core.FriendService;
import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.auth.PrincipalImpl;
import org.envirocar.server.rest.mapper.IllegalModificationExceptionMapper;
import org.envirocar.server.rest.mapper.JsonValidationExceptionMapper;
import org.envirocar.server.rest.mapper.ResourceAlreadyExistExceptionMapper;
import org.envirocar.server.rest.mapper.ResourceNotFoundExceptionMapper;
import org.envirocar.server.rest.mapper.ValidationExceptionMapper;
import org.envirocar.server.rest.resources.AbstractResource;
import org.envirocar.server.rest.resources.ResourceFactory;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.rights.AccessRights;
import org.envirocar.server.rest.rights.AccessRightsImpl;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyResourceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<Optional<Set<String>>>() {
        }).annotatedWith(Names.named(AbstractResource.ALLOWED_MAIL_ADDRESSES))
                .toProvider(AddressProvider.class);
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
                                     GroupService groupService,
                                     FriendService friendService) {
        PrincipalImpl p = (PrincipalImpl) ctx.getUserPrincipal();
        User user = p == null ? null : p.getUser();
        return new AccessRightsImpl(user, groupService, friendService);
    }
}
