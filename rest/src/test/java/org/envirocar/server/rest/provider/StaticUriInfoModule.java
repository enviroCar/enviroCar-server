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
package org.envirocar.server.rest.provider;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.mockito.Mockito;

import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class StaticUriInfoModule extends AbstractModule {
    @Provides
    @Singleton
    public UriInfo uriInfo() {
        final UriInfo mock = Mockito.mock(UriInfo.class);
        Mockito.when(mock.getBaseUriBuilder())
                .thenAnswer(invocation -> UriBuilder.fromUri("https://example.org/enviroCar/api/"));
        return mock;
    }
}