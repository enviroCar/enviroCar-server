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

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.envirocar.server.rest.mapper.*;
import org.envirocar.server.rest.resources.ResourceFactory;
import org.envirocar.server.rest.resources.RootResource;

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
        bind(BadRequestExceptionMapper.class).in(Scopes.SINGLETON);
        install(new FactoryModuleBuilder().build(ResourceFactory.class));
        bind(RootResource.class);
    }
}
