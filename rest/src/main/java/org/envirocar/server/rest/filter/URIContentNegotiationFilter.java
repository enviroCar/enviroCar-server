/*
 * Copyright (C) 2013-2020 The enviroCar project
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
package org.envirocar.server.rest.filter;

import com.sun.jersey.api.container.filter.UriConnegFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class URIContentNegotiationFilter extends UriConnegFilter {
    private static final Logger LOG = LoggerFactory.getLogger(URIContentNegotiationFilter.class);
    private static final String FILE_EXTENSIONS_PROPERTIES = "/file-extensions.properties";

    public URIContentNegotiationFilter() {
        super(getMapping());
    }

    private static Map<String, MediaType> getMapping() {
        Properties properties = new Properties();
        InputStream stream = URIContentNegotiationFilter.class.getResourceAsStream(FILE_EXTENSIONS_PROPERTIES);
        if (stream == null) {
            LOG.warn("no file extensions found");
        } else {
            try (InputStream inputStream = stream;
                 InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                properties.load(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        return properties.stringPropertyNames().stream()
                         .collect(toMap(identity(), ex -> MediaType.valueOf(properties.getProperty(ex))));
    }
}
