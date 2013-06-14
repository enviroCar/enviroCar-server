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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import io.car.server.core.activities.Activity;
import io.car.server.core.activities.ActivityType;
import io.car.server.core.activities.TrackActivity;
import io.car.server.core.event.EventBusListener;
import io.car.server.rest.CodingFactory;
import io.car.server.rest.MediaTypes;

public class HTTPPushListener implements EventBusListener {

	private HttpClient client;

	@Inject
	private CodingFactory coding;

	//TODO make configurable
	private String host = "https://localhost:6143/geoevent/rest/receiver/car-io-tracks-in-rest?f=generic-json";

	private static final Logger logger = LoggerFactory.getLogger(HTTPPushListener.class);

	public HTTPPushListener() throws Exception {
		SSLSocketFactory sslsf = new SSLSocketFactory(new TrustStrategy() {

			public boolean isTrusted(final X509Certificate[] chain,
					String authType) throws CertificateException {
				return true;
			}

		});

		Scheme httpsScheme2 = new Scheme("https", 443, sslsf);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(httpsScheme2);

		BasicClientConnectionManager cm = new BasicClientConnectionManager(schemeRegistry);
		
		this.client = new DefaultHttpClient(cm);
	}
	
	
	@Override
	public void onNewActivity(Activity ac) {
		if (ac.getType() == ActivityType.CREATED_TRACK) {
			pushNewTrack((TrackActivity) ac);
		}
	}


	private synchronized void pushNewTrack(TrackActivity ac) {
		ObjectNode jsonTrack = this.coding.createTrackEncoder().encode(ac.getTrack(), MediaTypes.TRACK_TYPE);
		String content = jsonTrack.toString();
		HttpResponse resp = null;
		try {
			HttpPost hp = new HttpPost(host);
			hp.setEntity(new StringEntity(content, ContentType.create(MediaTypes.TRACK)));
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

}
