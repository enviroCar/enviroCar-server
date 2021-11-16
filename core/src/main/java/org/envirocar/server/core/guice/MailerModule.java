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
package org.envirocar.server.core.guice;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.inject.Named;

import org.envirocar.server.core.mail.Mailer;
import org.envirocar.server.core.mail.MailerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class MailerModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(MailerModule.class);

    @Provides
    @Named("mail")
    public Properties getMailProperties() throws IOException {
        Properties properties = new Properties();

        Path file;

        file = Paths.get(".").resolve("mail.properties").toAbsolutePath();
        if (!Files.exists(file)) {
            LOG.warn("{} does not exists", file);
            String home = System.getProperty("user.home");
            if (home != null) {
                file = Paths.get(home).resolve("mail.properties");
                if (!Files.exists(file)) {
                    LOG.warn("{} does not exists", file);
                }
            }
        }

        if (!Files.exists(file)) {
            throw new IOException("could not locate mail.properties");
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            properties.load(reader);
        }

        for (Object key : properties.keySet()) {
            LOG.debug("mail config: {} -> {}", key, properties.get(key));
        }

        return properties;

    }

    @Override
    protected void configure() {
        bind(Mailer.class).to(MailerImpl.class).asEagerSingleton();
    }
}
