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
