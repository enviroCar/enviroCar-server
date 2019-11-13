/*
 * Copyright (C) 2013-2019 The enviroCar project
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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
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
import org.envirocar.server.rest.ConfirmationLinkFactoryImpl;
import org.envirocar.server.rest.resources.ResourceFactory;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.rights.AccessRights;
import org.envirocar.server.rest.rights.AccessRightsImpl;
import org.envirocar.server.rest.rights.NonRestrictiveRights;
import org.envirocar.server.rest.rights.ReadOnlyRights;

import javax.ws.rs.core.SecurityContext;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyResourceModule extends AbstractModule {

    private static final String READ_ONLY_PROPERTY = "enviroCar.readOnly";

    @Override
    protected void configure() {
        bind(new TypeLiteral<Optional<Set<String>>>() {})
                .annotatedWith(Names.named(AbstractResource.ALLOWED_MAIL_ADDRESSES))
                .toProvider(AddressProvider.class);

        bind(IllegalModificationExceptionMapper.class);
        bind(ResourceNotFoundExceptionMapper.class);
        bind(ValidationExceptionMapper.class);
        bind(ResourceAlreadyExistExceptionMapper.class);
        bind(JsonValidationExceptionMapper.class);
        bind(BadRequestExceptionMapper.class);
        bind(LegalPolicyExceptionMapper.class);
        bind(ForbiddenExceptionMapper.class);
        bind(InternalServerErrorMapper.class);
        bind(UnauthorizedExceptionMapper.class);
        bind(ThrowableExceptionMapper.class);
        bind(WebApplicationExceptionMapper.class);

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

        if (user != null && user.isAdmin()) {
            return new NonRestrictiveRights();
        } else if (isReadOnly(properties)) {
            return new ReadOnlyRights(user, groupService, friendService);
        } else {
            return new AccessRightsImpl(user, groupService, friendService);
        }
    }

    private static boolean isReadOnly(Properties properties) {
        try {
            return Boolean.parseBoolean(properties.getProperty(READ_ONLY_PROPERTY, "false"));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
