/*
 * Copyright (C) 2013 The enviroCar project
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
package org.envirocar.server.rest.encoding.rdf.linker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.rest.encoding.rdf.RDFLinker;
import org.envirocar.server.rest.rights.AccessRights;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Closeables;
import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;

/**
 *
 * @author Arne de Wall
 *
 */
public class EEAPhenomenonLinker implements RDFLinker<Phenomenon> {
    private static final String PREFIX = "eea.phenomenon.";
    private static final Logger log = LoggerFactory
            .getLogger(EEAPhenomenonLinker.class);
    private static final String PROPERTIES = "/EEAphenomenons.properties";
    private final Properties properties;

    public EEAPhenomenonLinker() {
        this.properties = new Properties();
        InputStream in = null;
        try {
            in = EEAPhenomenonLinker.class.getResourceAsStream(PROPERTIES);
            if (in != null) {
                properties.load(in);
            } else {
                log.warn("No {} found!", PROPERTIES);
            }

        } catch (IOException ex) {
            log.error("Error reading " + PROPERTIES, ex);
        } finally {
            Closeables.closeQuietly(in);
        }
    }

    @Override
    public void link(Model m, Phenomenon t, AccessRights rights,
                     Resource r, Provider<UriBuilder> uriBuilder) {
        String property = properties.getProperty(PREFIX + t.getName());
        if (property != null) {
            r.addProperty(OWL.sameAs, m.createResource(property));
        }
    }
}
