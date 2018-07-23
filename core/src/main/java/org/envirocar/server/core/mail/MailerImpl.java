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
package org.envirocar.server.core.mail;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.common.base.Strings;
import com.google.common.net.MediaType;

public class MailerImpl implements Mailer {

    private static final String MAIL_FROM_ADDRESS = "mail.from.address";
    private static final String MAIL_FROM_NAME = "mail.from.name";
    private static final String MAIL_REPLY_TO_ADDRESS = "mail.replyTo.address";
    private static final String MAIL_REPLY_TO_NAME = "mail.replyTo.name";
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_USERNAME = "mail.smtp.username";
    private static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";
    private static final String MAIL_SMTP_USE_SSL = "mail.smtp.useSSL";
    private static final String MAIL_SMTP_SOCKET_FACTORY_PORT = "mail.smtp.socketFactory.port";
    private static final String MAIL_SMTP_SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String SOCKET_FACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";
    private static final String TRUE = Boolean.toString(true);
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String CHARSET = DEFAULT_CHARSET.name();

    private final Properties configuration;
    private final Session session;
    private final InternetAddress from;
    private final InternetAddress replyTo;

    @Inject
    public MailerImpl(@Named("mail") Properties configuration) throws MailConfigurationException {
        this.configuration = Objects.requireNonNull(configuration);

        checkNotEmpty(MAIL_SMTP_USERNAME);
        checkNotEmpty(MAIL_SMTP_PASSWORD);
        checkNotEmpty(MAIL_SMTP_HOST);
        checkNotEmpty(MAIL_SMTP_PORT);
        checkNotEmpty(MAIL_FROM_ADDRESS);

        this.session = createSession();
        this.from = createFromAddress();
        this.replyTo = createReplyToAddress();
    }

    private void setContent(MailContent content, Part part) throws MailerException {
        if (content.getContent() == null || content.getContent().isEmpty()) {
            throw new MailerException("missing content");
        }
        MediaType contentType = content.getContentType();
        if (contentType == null) {
            throw new MailerException("missing contentType");
        }
        if (!contentType.charset().isPresent()) {
            contentType = contentType.withCharset(DEFAULT_CHARSET);
        }
        try {
            part.setContent(content.getContent(), contentType.toString());
        } catch (MessagingException ex) {
            throw new MailerException(ex);
        }
    }

    @Override
    public void send(Mail mail) throws MailerException {

        try {
            InternetAddress address = createAddress(mail.getRecipientAddress(), mail.getRecipientName());

            if (address == null) {
                throw new MailerException("missing address");
            }
            if (mail.getSubject() == null || mail.getSubject().isEmpty()) {
                throw new MailerException("missing subject");
            }
            List<MailContent> contents = mail.getContents();
            if (contents == null || contents.isEmpty()) {
                throw new MailerException("missing content");
            }
            MimeMessage message = new MimeMessage(this.session);
            message.setFrom(this.from);
            if (this.replyTo != null) {
                message.setReplyTo(new InternetAddress[] { replyTo });
            }
            message.setRecipients(Message.RecipientType.TO, new Address[] { address });
            message.setSubject(mail.getSubject());

            if (contents.size() == 1) {
                setContent(contents.iterator().next(), message);
            } else {
                message.setContent(createMultiPart(contents));
            }

            Transport.send(message);
        } catch (UnsupportedEncodingException | MessagingException ex) {
            throw new MailerException(ex);
        }
    }

    private MimeMultipart createMultiPart(List<MailContent> contents) throws MailerException {
        MimeMultipart mp = new MimeMultipart("alternative");
        try {
            for (MailContent content : contents) {
                MimeBodyPart part = new MimeBodyPart();
                setContent(content, part);
                mp.addBodyPart(part);
            }
        } catch (MessagingException ex) {
            throw new MailerException(ex);
        }
        return mp;
    }

    private Properties createProperties() throws MailConfigurationException {
        Properties props = new Properties();
        int port;
        try {
            port = Integer.parseInt(getProperty(MAIL_SMTP_PORT), 10);
            if (port < 0 || port > 65535) {
                throw new MailConfigurationException("%d is not a valid port", port);
            }
        } catch (NumberFormatException ex) {
            String message = String.format("%s is not a valid port", getProperty(MAIL_SMTP_PORT));
            throw new MailConfigurationException(message, ex);
        }

        props.put(MAIL_SMTP_HOST, getProperty(MAIL_SMTP_HOST));
        props.put(MAIL_SMTP_AUTH, TRUE);
        props.put(MAIL_SMTP_PORT, Integer.toString(port));

        if (Boolean.parseBoolean(getProperty(MAIL_SMTP_USE_SSL))) {
            props.put(MAIL_SMTP_SOCKET_FACTORY_PORT, Integer.toString(port));
            props.put(MAIL_SMTP_SOCKET_FACTORY_CLASS, SOCKET_FACTORY_CLASS);
        } else {
            props.put(MAIL_SMTP_STARTTLS_ENABLE, TRUE);
        }
        return props;
    }

    private AuthenticatorImpl createAuthenticator() {
        String password = getProperty(MAIL_SMTP_PASSWORD);
        String username = getProperty(MAIL_SMTP_USERNAME);
        return new AuthenticatorImpl(username, password);
    }

    private Session createSession() throws MailConfigurationException {
        Properties properties = createProperties();
        Authenticator authenticator = createAuthenticator();
        return Session.getInstance(properties, authenticator);
    }

    private void checkNotEmpty(String propertyName) throws MailConfigurationException {
        if (!hasProperty(propertyName)) {
            throw new MailConfigurationException("%s is required", propertyName);
        }
    }

    private InternetAddress createFromAddress() throws MailConfigurationException {
        try {
            return createAddress(getProperty(MAIL_FROM_ADDRESS),
                                 getProperty(MAIL_FROM_NAME));
        } catch (UnsupportedEncodingException | AddressException ex) {
            throw new MailConfigurationException("could not create from address", ex);
        }
    }

    private InternetAddress createReplyToAddress() throws MailConfigurationException {
        try {
            return createAddress(getProperty(MAIL_REPLY_TO_ADDRESS),
                                 getProperty(MAIL_REPLY_TO_NAME));
        } catch (UnsupportedEncodingException | AddressException ex) {
            throw new MailConfigurationException("could not create from address", ex);
        }
    }

    private InternetAddress createAddress(String address, String name)
            throws UnsupportedEncodingException, AddressException {
        if (Strings.nullToEmpty(address).isEmpty()) {
            return null;
        }
        if (Strings.nullToEmpty(name).isEmpty()) {
            return new InternetAddress(address);

        } else {
            return new InternetAddress(address, name, CHARSET);
        }
    }

    private boolean hasProperty(String property) {
        return !getProperty(property).isEmpty();
    }

    private String getProperty(String property) {
        return Strings.nullToEmpty(configuration.getProperty(property));
    }

    private static class AuthenticatorImpl extends Authenticator {
        private final String username;
        private final String password;

        AuthenticatorImpl(String username, String password) {
            this.username = Objects.requireNonNull(username);
            this.password = Objects.requireNonNull(password);
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
}
