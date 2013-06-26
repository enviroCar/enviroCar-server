/*
 * Copyright (C) 2013 The enviroCar project
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
package org.envirocar.server.rest.decoding.json;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.rest.JSONConstants;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Provider
public class SensorDecoder extends AbstractJSONEntityDecoder<Sensor> {
    public SensorDecoder() {
        super(Sensor.class);
    }

    @Override
    public Sensor decode(JsonNode j, MediaType mediaType) {
        Sensor s = getEntityFactory().createSensor();
        s.setType(j.path(JSONConstants.TYPE_KEY).textValue());
        JsonNode properties = j.path(JSONConstants.PROPERTIES_KEY);
        // do not allow a property called id...
        if (!properties.path(JSONConstants.IDENTIFIER_KEY).isMissingNode()) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        Iterator<String> iter = properties.fieldNames();
        while (iter.hasNext()) {
            String name = iter.next();
            s.addProperty(name, parseNode(properties.get(name)));
        }
        return s;
    }

    private Object parseNode(JsonNode value) {
        if (value.isContainerNode()) {
            if (value.isArray()) {
                return parseArrayNode(value);
            } else if (value.isObject()) {
                return parseObjectNode(value);
            }
        } else if (value.isValueNode()) {
            return parseValueNode(value);
        } else if (value.isMissingNode()) {
            return null;
        }
        throw new IllegalArgumentException("can not decode " + value);
    }

    private Object parseObjectNode(JsonNode value) {
        Iterator<String> names = value.fieldNames();
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(value.size());
        while (names.hasNext()) {
            String name = names.next();
            map.put(name, parseNode(value.path(name)));
        }
        return map;
    }

    private Object parseArrayNode(JsonNode value) {
        List<Object> list = Lists.newArrayListWithExpectedSize(value.size());
        for (int i = 0; i < value.size(); ++i) {
            list.add(parseNode(value.get(i)));
        }
        return list;
    }

    private Object parseValueNode(JsonNode value) {
        if (value.isNull()) {
            return null;
        } else if (value.isNumber()) {
            if (value.isFloatingPointNumber()) {
                if (value.isBigDecimal()) {
                    return value.decimalValue();
                } else if (value.isDouble()) {
                    return value.asDouble();
                }
            } else if (value.isIntegralNumber()) {
                if (value.isBigInteger()) {
                    return value.bigIntegerValue();
                } else if (value.isLong()) {
                    return value.longValue();
                } else if (value.isInt()) {
                    return value.intValue();
                }
            }
            return value.numberValue();
        } else if (value.isTextual()) {
            return value.asText();
        } else if (value.isBoolean()) {
            return value.booleanValue();
        }
        throw new IllegalArgumentException("can not decode " + value);
    }
}
