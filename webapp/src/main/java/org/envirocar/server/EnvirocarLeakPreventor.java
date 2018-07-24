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
package org.envirocar.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.jiderhamn.classloader.leak.prevention.ClassLoaderLeakPreventor;

/**
 *
 * @author matthes
 */
public class EnvirocarLeakPreventor extends ClassLoaderLeakPreventor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvirocarLeakPreventor.class);

    @Override
    protected void error(Throwable t) {
        LOGGER.warn("Exception on shutting down resource.", t);
    }

    @Override
    protected void fixGeoToolsLeak() {
        //original method broken; uses static call on non-static method
    }



}
