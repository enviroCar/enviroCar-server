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
package io.car.server.rest.provider;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

import io.car.server.rest.coding.CodingFactory;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public abstract class AbstractJsonEntityProvider<T> extends AbstracJsonProvider<T> {
    @Inject
    private CodingFactory codingFactory;

    public AbstractJsonEntityProvider(Class<T> classType, Set<MediaType> readableMediaTypes,
                                      Set<MediaType> writableMediaTypes) {
        super(classType, readableMediaTypes, writableMediaTypes);
    }

    public AbstractJsonEntityProvider(Class<T> classType, MediaType get, MediaType post, MediaType put) {
        this(classType, ImmutableSet.of(post, put), ImmutableSet.of(get));
    }

    public AbstractJsonEntityProvider(Class<T> classType, MediaType get, MediaType post) {
        this(classType, ImmutableSet.of(post), ImmutableSet.of(get));
    }

    public AbstractJsonEntityProvider(Class<T> classType, MediaType get) {
        this(classType, Collections.<MediaType>emptySet(), ImmutableSet.of(get));
    }

    protected CodingFactory getCodingFactory() {
        return codingFactory;
    }
}
