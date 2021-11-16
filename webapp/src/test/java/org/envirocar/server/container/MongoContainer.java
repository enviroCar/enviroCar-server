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
package org.envirocar.server.container;

import com.google.common.base.Strings;
import org.testcontainers.containers.GenericContainer;

import java.util.Objects;

public class MongoContainer extends GenericContainer<MongoContainer> {

    private static final int PORT = 27017;

    public MongoContainer(String version) {
        super(String.format("mongo:%s", Objects.requireNonNull(Strings.emptyToNull(version))));
    }

    public MongoContainer() {
        this("latest");
        withExposedPorts(PORT);
    }

    public int getPort() {
        return getMappedPort(PORT);
    }
}
