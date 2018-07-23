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
package org.envirocar.server.event.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public abstract class AbstractSendMail implements SendMail {

	private String user;
	private String password;
	private String from;
	private String host;
	private int port;
	
	@Override
	public void setup(String user, String password, String fromEmail,
			String smtpHost, int smtpPort) {
		this.user = user;
		this.password = password;
		this.from = fromEmail;
		this.host = smtpHost;
		this.port = smtpPort;
	}

	@Override
	public void setup(Properties conf) {
		setup(conf.getProperty("USER"), conf.getProperty("PASSWORD"),
				conf.getProperty("FROM_MAIL"), conf.getProperty("SMTP_HOST"),
				Integer.parseInt(conf.getProperty("SMTP_PORT")));
	}
	
	@Override
	public void send(String email, String subject, String content)
			throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", Integer.toString(port));

		injectProperties(props);
		
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email));
		message.setSubject(subject);
		message.setText(content);

		Transport.send(message);
	}

	protected abstract void injectProperties(Properties props);
	
}
