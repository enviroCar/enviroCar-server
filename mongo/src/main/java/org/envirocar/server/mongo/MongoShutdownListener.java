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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.envirocar.server.mongo;

import com.google.inject.Inject;
import com.mongodb.Mongo;
import org.envirocar.server.core.guice.ResourceShutdownListener;

/**
 * @author matthes
 */
public class MongoShutdownListener implements ResourceShutdownListener {

    private final Mongo mongo;

    @Inject
    public MongoShutdownListener(Mongo mongo) {
        this.mongo = mongo;
    }

    @Override
    public void shutdownResources() {
        if (mongo != null) {
            mongo.close();
        }
    }

}
