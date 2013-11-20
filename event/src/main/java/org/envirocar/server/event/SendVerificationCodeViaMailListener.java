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
package org.envirocar.server.event;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.MessagingException;

import org.envirocar.server.core.event.PasswordResetEvent;
import org.envirocar.server.event.mail.SendMail;
import org.envirocar.server.event.mail.SendMailSSL;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Singleton;

@Singleton
public class SendVerificationCodeViaMailListener {

	private static final String USERNAME = "{username}";
	private static final String CODE = "{code}";
	private static final String EXPIRATION_TIME = "{expirationTime}";
	private static final Logger logger = LoggerFactory
			.getLogger(SendVerificationCodeViaMailListener.class);
	private static final DateTimeFormatter dateFormat = ISODateTimeFormat.dateTimeNoMillis();
	private static final String VERIFICATION_CODE_SUBJECT = "VERIFICATION_CODE_SUBJECT";

	public SendVerificationCodeViaMailListener() {
	}

	@Subscribe
	public void onPasswordResetRequestedEvent(PasswordResetEvent ev) {
		CharSequence template = fillTemplate(ev);

		try {
			Properties mailConfiguration = new Properties();
			mailConfiguration.load(openStreamForResource(
					"mail-configuration.properties"));

			/*
			 * TODO guice it
			 */
			SendMail sender = new SendMailSSL();
			sender.setup(mailConfiguration);

			sender.send(ev.getUser().getMail(),
					mailConfiguration.getProperty(VERIFICATION_CODE_SUBJECT),
					template.toString());
		} catch (MessagingException e) {
			logger.warn(e.getMessage(), e);
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}
	}


	private CharSequence fillTemplate(PasswordResetEvent e) {
		String template = readFile("mail-verification-code-template.html")
				.toString();
		return template
				.replace(USERNAME, e.getUser().getName())
				.replace(CODE, e.getCode())
				.replace(EXPIRATION_TIME,
						e.getExpiration().toString(dateFormat));
	}

	private CharSequence readFile(String string) {
		StringBuilder sb = new StringBuilder();
		Scanner sc = null;

		String sep = System.getProperty("line.separator");
		try {
			URLConnection conn = getClass().getResource(string)
					.openConnection();
			conn.setUseCaches(false);

			sc = new Scanner(new InputStreamReader(openStreamForResource(string)));
			while (sc.hasNext()) {
				sb.append(sc.nextLine());
				sb.append(sep);
			}
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		} finally {
			if (sc != null) {
				sc.close();
			}
		}

		return sb;
	}

	private InputStream openStreamForResource(String string) throws IOException {
		URLConnection conn = getClass().getResource(string)
				.openConnection();
		conn.setUseCaches(false);
		return conn.getInputStream();
	}


}
