/*
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
package io.car.server.event;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.car.server.core.entities.Track;
import io.car.server.core.event.CreatedTrackEvent;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.encoding.EntityEncoder;
import io.car.server.rest.rights.AccessRightsImpl;

@Singleton
public class HTTPPushListener {
    //TODO make configurable
    private static final String host =
            "https://localhost:6143/geoevent/rest/receiver/car-io-tracks-in-rest?f=generic-json";
    private static final Logger logger = LoggerFactory
            .getLogger(HTTPPushListener.class);
    public static final AccessRightsImpl DEFAULT_ACCESS_RIGHTS =
            new AccessRightsImpl();
    private final HttpClient client;
    private final EntityEncoder<Track> encoder;
    private final ObjectWriter writer;

    @Inject
    public HTTPPushListener(EntityEncoder<Track> encoder,
                            ObjectWriter writer) throws Exception {
        this.client = createClient();
        this.encoder = encoder;
        this.writer = writer;
    }

    @Subscribe
    public void onCreatedTrackEvent(CreatedTrackEvent e) {
        pushNewTrack(e.getTrack());
    }

    private synchronized void pushNewTrack(Track track) {
        HttpResponse resp = null;
        try {
            ObjectNode jsonTrack = encoder.encode(track, DEFAULT_ACCESS_RIGHTS,
                                                  MediaTypes.TRACK_TYPE);
            String content = writer.writeValueAsString(jsonTrack);
            logger.debug("Entity: {}", content);
            HttpEntity entity = new StringEntity(
                    content, ContentType.create(MediaTypes.TRACK));
            HttpPost hp = new HttpPost(host);
            hp.setEntity(entity);
            resp = this.client.execute(hp);
        } catch (ClientProtocolException e) {
            logger.warn(e.getMessage(), e);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        } finally {
            if (resp != null) {
                try {
                    EntityUtils.consume(resp.getEntity());
                } catch (IOException e) {
                    logger.warn(e.getMessage());
                }
            }
        }
    }

    private HttpClient createClient() throws UnrecoverableKeyException,
                                             KeyManagementException,
                                             KeyStoreException,
                                             NoSuchAlgorithmException {
        SSLSocketFactory sslsf = new SSLSocketFactory(new TrustStrategy() {
            @Override
            public boolean isTrusted(final X509Certificate[] chain,
                                     String authType) {
                //FIXME kind of bad practice...
                return true;
            }
        });
        Scheme httpsScheme2 = new Scheme("https", 443, sslsf);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(httpsScheme2);

        BasicClientConnectionManager cm =
                new BasicClientConnectionManager(schemeRegistry);

        return new DefaultHttpClient(cm);
    }
}
