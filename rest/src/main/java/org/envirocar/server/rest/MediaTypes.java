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
package org.envirocar.server.rest;

import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
public interface MediaTypes {
    String XML_RDF = "application/rdf+xml";
    String TURTLE = "text/turtle";
    String TEXT_CSV = "text/csv";
    String TURTLE_ALT = "application/x-turtle";
    String IMAGE_JPEG = "image/jpeg";
    String IMAGE_PNG = "image/png";
    String APPLICATION_ZIPPED_SHP = "application/x-zipped-shp";

    MediaType XML_RDF_TYPE = new MediaType("application", "rdf+xml");
    MediaType APPLICATION_ZIPPED_SHP_TYPE = new MediaType("application", "x-zipped-shp");
    MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");
    MediaType TURTLE_TYPE = new MediaType("text", "turtle");
    MediaType TURTLE_ALT_TYPE = new MediaType("application", "x-turtle");
    MediaType IMAGE_JPEG_TYPE = new MediaType("image", "jpeg");
    String SCHEMA_ATTRIBUTE = "schema";
    MediaType EXCEPTION_TYPE = new MediaType("application", "json", schema(Schemas.EXCEPTION));
    String JSON = "application/json";

    static Map<String, String> schema(String schema) {
        return Collections.singletonMap(SCHEMA_ATTRIBUTE, schema);
    }

    static boolean hasSchemaAttribute(MediaType mediaType) {
        return mediaType != null
               && mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)
               && mediaType.getParameters().containsKey(SCHEMA_ATTRIBUTE);
    }

    static Optional<String> getSchemaAttribute(MediaType mediaType) {
        return hasSchemaAttribute(mediaType)
               ? Optional.ofNullable(mediaType.getParameters().get(SCHEMA_ATTRIBUTE))
               : Optional.empty();
    }

    static MediaType jsonWithSchema(String schema) {
        return new MediaType("application", "json", schema(schema));
    }
}
