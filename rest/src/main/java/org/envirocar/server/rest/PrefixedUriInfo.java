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
package org.envirocar.server.rest;

import com.sun.jersey.api.uri.UriComponent;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrefixedUriInfo extends DelegatingUriInfo {
    private final List<String> prefix;

    public PrefixedUriInfo(UriInfo delegate, List<String> prefix) {
        super(delegate);
        this.prefix = prefix;
    }

    public PrefixedUriInfo(UriInfo delegate, HttpHeaders headers) {
        super(delegate);
        this.prefix = headers.getRequestHeader("x-forwarded-prefix");
    }


    @Override
    public URI getBaseUri() {
        return getBaseUriBuilder().build();
    }

    @Override
    public URI getAbsolutePath() {
        return getAbsolutePathBuilder().build();
    }

    @Override
    public URI getRequestUri() {
        return getRequestUriBuilder().build();
    }

    @Override
    public UriBuilder getBaseUriBuilder() {
        return addPrefix(super.getBaseUri());
    }

    @Override
    public UriBuilder getRequestUriBuilder() {
        return addPrefix(super.getRequestUri());
    }

    @Override
    public UriBuilder getAbsolutePathBuilder() {
        return addPrefix(super.getAbsolutePath());
    }

    @Override
    public List<PathSegment> getPathSegments() {
        return getPathSegments(true);
    }

    @Override
    public List<PathSegment> getPathSegments(boolean decode) {
        return Stream.concat(getPrefixSegments(decode).stream(),
                super.getPathSegments(decode).stream()).collect(Collectors.toList());
    }

    private List<PathSegment> getPrefixSegments(boolean decode) {
        if (this.prefix == null || this.prefix.isEmpty()) {
            return Collections.emptyList();
        }
        Iterator<String> iter = this.prefix.iterator();
        UriBuilder builder = UriBuilder.fromPath(iter.next());
        while (iter.hasNext()) {
            builder = builder.path(iter.next());
        }
        return UriComponent.decodePath(builder.build(), decode);
    }

    private UriBuilder addPrefix(URI uri) {
        UriBuilder builder = UriBuilder.fromUri(uri);
        if (this.prefix != null && !this.prefix.isEmpty()) {
            builder = builder.replacePath("");
            for (String path : this.prefix) {
                if (path != null && !path.isEmpty()) {
                    builder = builder.path(path);
                }
            }
            builder = builder.path(uri.getPath());
        }
        return builder;
    }
}
