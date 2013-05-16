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
package io.car.server.rest.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import io.car.server.rest.resources.ResourceFactory;
import io.car.server.rest.resources.FriendsResource;
import io.car.server.rest.resources.GroupMemberResource;
import io.car.server.rest.resources.GroupMembersResource;
import io.car.server.rest.resources.GroupResource;
import io.car.server.rest.resources.GroupsResource;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.UserResource;
import io.car.server.rest.resources.UsersResource;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class JerseyResourceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(UserResource.class, UserResource.class)
                .implement(UsersResource.class, UsersResource.class)
                .implement(FriendsResource.class, FriendsResource.class)
                .implement(GroupResource.class, GroupResource.class)
                .implement(GroupsResource.class, GroupsResource.class)
                .implement(GroupMembersResource.class, GroupMembersResource.class)
                .implement(GroupMemberResource.class, GroupMemberResource.class)
                .build(ResourceFactory.class));

        bind(RootResource.class);
    }

}
