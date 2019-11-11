/*
 * Copyright (C) 2013-2019 The enviroCar project
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
package org.envirocar.server.rest;

import org.envirocar.server.core.guice.ResourceShutdownListener;
import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.geotools.referencing.factory.DeferredAuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class GeotoolsShutdownListener implements ResourceShutdownListener {

    private static final Logger LOG = LoggerFactory.getLogger(GeotoolsShutdownListener.class);

    @Override
    public void shutdownResources() {
        List<CRSAuthorityFactory> candidates = new ArrayList<>(2);
        candidates.add(org.geotools.referencing.CRS.getAuthorityFactory(true));
        candidates.add(org.geotools.referencing.CRS.getAuthorityFactory(false));

        candidates.stream().filter(Objects::nonNull).peek(factory -> {
            if (factory instanceof DeferredAuthorityFactory) {
                DeferredAuthorityFactory.exit();
            }
        }).filter(factory -> factory instanceof AbstractAuthorityFactory).forEach(factory -> {
            try {
                ((AbstractAuthorityFactory) factory).dispose();
            } catch (FactoryException fe) {
                LOG.error("Error while GeometryHandler clean up", fe);
            }
        });
    }

}
