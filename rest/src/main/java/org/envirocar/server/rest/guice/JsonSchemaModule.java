/*
 * Copyright (C) 2013-2021 The enviroCar project
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

import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import org.envirocar.server.rest.Schemas;

import javax.ws.rs.core.UriInfo;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JsonSchemaModule extends AbstractModule {

    public static final String SCHEMAS = "schemas";

    @Override
    protected void configure() {

        requireBinding(JsonNodeCreator.class);
        requireBinding(UriInfo.class);

        bind(JsonSchemaFactory.class).toProvider(JsonSchemaFactoryProvider.class).in(Scopes.SINGLETON);

        Multibinder<String> mb = Multibinder.newSetBinder(binder(), String.class, Names.named(SCHEMAS));
        Schemas.ALL_SCHEMAS.forEach(schema -> mb.addBinding().toInstance(schema));
    }
}
