/*
 * Copyright (C) 2013-2022 The enviroCar project
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
    String JSON = MediaType.APPLICATION_JSON;
    String XML_RDF = "application/rdf+xml";
    String TURTLE = "text/turtle";
    String CSV = "text/csv";
    String TURTLE_ALT = "application/x-turtle";
    String JPEG = "image/jpeg";
    String PNG = "image/png";
    String YAML = "text/yaml";
    String APPLICATION_ZIPPED_SHP = "application/x-zipped-shp";
    String APPLICATION_X_SHAPEFILE = "application/x-shapefile";

    MediaType JSON_TYPE = MediaType.APPLICATION_JSON_TYPE;
    MediaType YAML_TYPE = MediaType.valueOf(YAML);
    MediaType XML_RDF_TYPE = MediaType.valueOf(XML_RDF);
    MediaType APPLICATION_ZIPPED_SHP_TYPE = MediaType.valueOf(APPLICATION_ZIPPED_SHP);
    MediaType APPLICATION_X_SHAPEFILE_TYPE = MediaType.valueOf(APPLICATION_X_SHAPEFILE);
    MediaType CSV_TYPE = MediaType.valueOf(CSV);
    MediaType TURTLE_TYPE = MediaType.valueOf(TURTLE);
    MediaType TURTLE_ALT_TYPE = MediaType.valueOf(TURTLE_ALT);
    MediaType JSON_EXCEPTION_TYPE = jsonWithSchema(Schemas.EXCEPTION);
    MediaType YAML_EXCEPTION_TYPE = yamlWithSchema(Schemas.EXCEPTION);

    String SCHEMA_ATTRIBUTE = "schema";

    static Map<String, String> schema(String schema) {
        return Collections.singletonMap(SCHEMA_ATTRIBUTE, schema);
    }

    static MediaType jsonWithSchema(String schema) {
        return new MediaType(JSON_TYPE.getType(), JSON_TYPE.getSubtype(), schema(schema));
    }

    static MediaType yamlWithSchema(String schema) {
        return new MediaType(YAML_TYPE.getType(), YAML_TYPE.getSubtype(), schema(schema));
    }

    static boolean hasSchemaAttribute(MediaType mediaType) {
        return mediaType != null
               && (mediaType.isCompatible(JSON_TYPE) || mediaType.isCompatible(YAML_TYPE))
               && mediaType.getParameters().containsKey(SCHEMA_ATTRIBUTE);
    }

    static Optional<String> getSchemaAttribute(MediaType mediaType) {
        return Optional.ofNullable(mediaType)
                       .filter(MediaTypes::hasSchemaAttribute)
                       .map(MediaType::getParameters)
                       .map(m -> m.get(SCHEMA_ATTRIBUTE));
    }

}
