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
public class DBPediaPhenomenonLinker implements RDFLinker<Phenomenon> {
    private static final String PREFIX = "dbpedia.";
    private static final Logger log = LoggerFactory
            .getLogger(EEAPhenomenonLinker.class);
    private static final String PROPERTIES = "/DBPedia.properties";
    private final Properties properties;
    
    public DBPediaPhenomenonLinker(){
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
    public void link(Model m, Phenomenon t, AccessRights rights, Resource r,
            Provider<UriBuilder> uriBuilder) {
        String property = properties.getProperty(PREFIX + t.getName());
        if (property != null) {
            r.addProperty(OWL.sameAs, m.createResource(property));
        }
    }
}
