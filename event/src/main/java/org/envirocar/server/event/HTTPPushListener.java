/*
 * Copyright (C) 2013-2021 The enviroCar project
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
package org.envirocar.server.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.TrackStatus;
import org.envirocar.server.core.event.ChangedTrackEvent;
import org.envirocar.server.core.event.CreatedTrackEvent;
import org.envirocar.server.core.event.DeletedTrackEvent;
import org.envirocar.server.core.event.ChangedTrackStatusEvent;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.encoding.JSONEntityEncoder;
import org.envirocar.server.rest.rights.AccessRightsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Singleton
public class HTTPPushListener {
    //TODO make configurable
    private static final String host = "http://ags.52north.org:8080/enviroCar-broker/";
    private static final Logger LOG = LoggerFactory.getLogger(HTTPPushListener.class);
    private static final AccessRightsImpl DEFAULT_ACCESS_RIGHTS = new AccessRightsImpl();
    private final HttpClient client;
    private final JSONEntityEncoder<? super Track> encoder;
    private final ObjectWriter writer;

    @Inject
    public HTTPPushListener(JSONEntityEncoder<Track> encoder, ObjectWriter writer) {
        this.client = createClient();
        this.encoder = encoder;
        this.writer = writer;
    }

    @Subscribe
    public void onCreatedTrackEvent(CreatedTrackEvent e) {
        if (e.getTrack().getStatus() == TrackStatus.ONGOING) {
            LOG.info("Created ongoing track, won't publish");
            return;
        }
        pushNewTrack(e.getTrack());
    }

    @Subscribe
    public void onCreatedTrackEvent(ChangedTrackEvent e) {
        if (e instanceof ChangedTrackStatusEvent) {
            if (((ChangedTrackStatusEvent) e).matches(TrackStatus.ONGOING, TrackStatus.FINISHED)) {
                LOG.debug("Finished ongoing track, publish");
                pushNewTrack(e.getTrack());
            }
        }
    }

    @Subscribe
    public void onDeletedTrackEvent(DeletedTrackEvent e) {
        popDeletedTrack(e.getTrack());
    }

    private synchronized void popDeletedTrack(Track track) {
        // TODO: USE ME
    }

    private synchronized void pushNewTrack(Track track) {
        HttpResponse resp = null;
        try {

            MediaType mediaType = MediaTypes.jsonWithSchema(Schemas.TRACK);
            JsonNode jsonTrack = this.encoder.encodeJSON(track, DEFAULT_ACCESS_RIGHTS, mediaType);
            String content = this.writer.writeValueAsString(jsonTrack);
            //logger.debug("Entity: {}", content);
            HttpEntity entity = new StringEntity(content, ContentType.APPLICATION_JSON);
            HttpPost hp = new HttpPost(host);
            hp.setEntity(entity);
            resp = this.client.execute(hp);
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
        } finally {
            if (resp != null) {
                try {
                    EntityUtils.consume(resp.getEntity());
                } catch (IOException e) {
                    LOG.warn(e.getMessage());
                }
            }
        }
    }

    private HttpClient createClient() {
        return HttpClientBuilder.create().build();
    }
}
