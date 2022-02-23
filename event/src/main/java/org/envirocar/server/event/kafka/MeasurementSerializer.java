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
package org.envirocar.server.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import org.apache.kafka.common.serialization.Serializer;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRights;
import org.envirocar.server.rest.rights.AccessRightsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.util.Objects;

public class MeasurementSerializer implements Serializer<Measurement> {
    private static final Logger LOG = LoggerFactory.getLogger(TrackSerializer.class);
    private final JSONEntityEncoder<Measurement> encoder;
    private final ObjectWriter objectMapper;

    @Inject
    public MeasurementSerializer(JSONEntityEncoder<Measurement> encoder, ObjectWriter objectMapper) {
        this.encoder = Objects.requireNonNull(encoder);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public byte[] serialize(String topic, Measurement data) {
        try {
            MediaType mediaType = MediaTypes.jsonWithSchema(Schemas.MEASUREMENT);
            AccessRights rights = new AccessRightsImpl();
            JsonNode jsonTrack = this.encoder.encodeJSON(data, rights, mediaType);
            return this.objectMapper.writeValueAsBytes(jsonTrack);
        } catch (JsonProcessingException ex) {
            LOG.error("Error in serializing measurement", ex);
            return null;
        }

    }

}
