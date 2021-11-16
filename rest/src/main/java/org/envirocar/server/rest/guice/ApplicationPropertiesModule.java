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
package org.envirocar.server.rest.guice;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.google.inject.AbstractModule;

/**
 * @author Christian Autermann
 */
public class ApplicationPropertiesModule extends AbstractModule {

    @Override
    protected void configure() {
        Properties properties = new Properties();

        URL resource = getClass().getResource("/application.properties");

        if (resource != null) {
            try (InputStream stream = resource.openStream()) {
                properties.load(stream);
            } catch (IOException ex) {
                throw new Error("Can not read application.properties", ex);
            }
        }

        bind(Properties.class).toInstance(properties);
    }

}
