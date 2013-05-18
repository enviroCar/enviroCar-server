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



import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class JerseyValidationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JsonSchemaFactory.class).toProvider(JSONSchemaFactoryProvider.class).in(Scopes.SINGLETON);
    }
    
}
