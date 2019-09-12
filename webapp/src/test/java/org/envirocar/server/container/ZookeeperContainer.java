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
package org.envirocar.server.container;

import org.testcontainers.containers.GenericContainer;

public class ZookeeperContainer extends GenericContainer<ZookeeperContainer> {
    private int id;

    public ZookeeperContainer() {
        super("confluentinc/cp-zookeeper:latest");
    }

    public ZookeeperContainer withId(int id) {
        this.id = id;
        return this;
    }

    @Override
    protected void configure() {
        withExposedPorts(2181);
        withEnv("ZOOKEEPER_CLIENT_PORT", "2181");
        withEnv("zk_id", String.valueOf(id));
        super.configure();
    }
}
