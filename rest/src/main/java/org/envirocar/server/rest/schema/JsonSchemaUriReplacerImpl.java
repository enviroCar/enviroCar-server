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
package org.envirocar.server.rest.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.IntStream;

public class JsonSchemaUriReplacerImpl implements JsonSchemaUriReplacer {
    private final JsonNodeCreator jsonNodeCreator;
    private final JsonSchemaUriConfiguration schemaUriConfiguration;

    @Inject
    public JsonSchemaUriReplacerImpl(JsonNodeCreator jsonNodeCreator,
                                     JsonSchemaUriConfiguration schemaUriConfiguration) {
        this.jsonNodeCreator = jsonNodeCreator;
        this.schemaUriConfiguration = schemaUriConfiguration;
    }

    @Override
    public JsonNode replaceSchemaLinks(JsonNode node) {
        if (node.isObject()) {
            return handleObjectNode(node);
        } else if (node.isArray()) {
            return handleArrayNode(node);
        } else {
            return node;
        }
    }

    private JsonNode handleArrayNode(JsonNode node) {
        ArrayNode arrayNode = jsonNodeCreator.arrayNode();
        IntStream.range(0, node.size())
                .mapToObj(node::path)
                .map(this::replaceSchemaLinks)
                .forEachOrdered(arrayNode::add);
        return arrayNode;
    }

    private JsonNode handleObjectNode(JsonNode node) {
        ObjectNode objectNode = jsonNodeCreator.objectNode();
        node.fieldNames().forEachRemaining(name -> {
            JsonNode child = node.path(name);
            if (name.equals("$ref") && child.isTextual()) {
                String textValue = child.textValue();
                try {
                    objectNode.put(name, schemaUriConfiguration.toExternalURI(new URI(textValue)).toString());
                } catch (URISyntaxException ignored) {
                    objectNode.set(name, child);
                }
            } else {
                objectNode.set(name, replaceSchemaLinks(child));
            }


        });
        return objectNode;
    }


}
