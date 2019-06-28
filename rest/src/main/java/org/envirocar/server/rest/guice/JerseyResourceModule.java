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

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;
import org.envirocar.server.core.ConfirmationLinkFactory;
import org.envirocar.server.core.FriendService;
import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.auth.PrincipalImpl;
import org.envirocar.server.rest.mapper.*;
import org.envirocar.server.rest.resources.AbstractResource;
import org.envirocar.server.rest.resources.ConfirmationLinkFactoryImpl;
import org.envirocar.server.rest.resources.ResourceFactory;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.rights.AccessRights;
import org.envirocar.server.rest.rights.AccessRightsImpl;
import org.envirocar.server.rest.rights.ReadOnlyRights;

import javax.ws.rs.core.SecurityContext;
import java.util.Properties;
import java.util.Set;

/**
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
        bind(BadRequestExceptionMapper.class).in(Scopes.SINGLETON);
        bind(LegalPolicyExceptionMapper.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(ResourceFactory.class));
        bind(ConfirmationLinkFactory.class).to(ConfirmationLinkFactoryImpl.class);
        bind(RootResource.class);
    }

    @Provides
    @RequestScoped
    public AccessRights accessRights(SecurityContext ctx,
                                     GroupService groupService,
                                     FriendService friendService,
                                     Properties properties) {
        PrincipalImpl p = (PrincipalImpl) ctx.getUserPrincipal();
        User user = p == null ? null : p.getUser();

        if (isReadOnly(properties)) {
            return new ReadOnlyRights(user, groupService, friendService);
        } else {
            return new AccessRightsImpl(user, groupService, friendService);
        }
    }

    private static boolean isReadOnly(Properties properties) {
        try {
            return Boolean.parseBoolean(properties
                    .getProperty("enviroCar.readOnly", "false"));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
