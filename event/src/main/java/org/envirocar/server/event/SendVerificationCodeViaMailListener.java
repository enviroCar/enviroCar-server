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

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.envirocar.server.core.event.PasswordResetEvent;
import org.envirocar.server.core.mail.Mailer;
import org.envirocar.server.core.mail.MailerException;
import org.envirocar.server.core.mail.PlainMail;
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
    private static final Logger LOG = LoggerFactory.getLogger(SendVerificationCodeViaMailListener.class);
    private static final DateTimeFormatter DATE_FORMAT = ISODateTimeFormat.dateTimeNoMillis();
    private static final Path EMAIL_TEMPLATE_FILE = Paths.get("password-recovery-mail-template.txt");
    private Path templateFile;
    private final Mailer mailer;

    @Inject
    public SendVerificationCodeViaMailListener(Mailer mailer) {
        this.mailer = Objects.requireNonNull(mailer);
        String home = System.getProperty("user.home");
        if (home != null) {
            Path homeDirectory = Paths.get(home);
            if (Files.isDirectory(homeDirectory)) {
                this.templateFile = homeDirectory.resolve(EMAIL_TEMPLATE_FILE).toAbsolutePath();
            }
        } else {
            LOG.warn("user.home is not specified. Will try to use fallback resources.");
        }

    }

    @Subscribe
    public void onPasswordResetRequestedEvent(PasswordResetEvent e) {
        try {
            Path path;
            if (Files.exists(templateFile)) {
                path = templateFile;
            } else if (Files.exists(EMAIL_TEMPLATE_FILE.toAbsolutePath())) {
                path = EMAIL_TEMPLATE_FILE.toAbsolutePath();
            } else {
                LOG.error("Could not find email template file for password reset at {} or {}",
                          templateFile, EMAIL_TEMPLATE_FILE.toAbsolutePath());
                return;
            }

            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            if (lines.size() < 3) {
                LOG.error("At least 3 lines of content required to be a valid mail template file");
            }

            String subject = lines.stream().findFirst().orElse(null);
            String content = lines.stream().skip(2).collect(joining(System.lineSeparator()))
                    .replace(USERNAME, e.getUser().getName())
                    .replace(CODE, e.getCode())
                    .replace(EXPIRATION_TIME, e.getExpiration().toString(DATE_FORMAT));

            this.mailer.send(new PlainMail(e.getUser(), subject, content));

        } catch (MailerException | IOException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
    }
}
