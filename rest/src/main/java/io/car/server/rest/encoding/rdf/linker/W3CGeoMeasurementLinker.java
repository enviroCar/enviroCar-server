package io.car.server.rest.encoding.rdf.linker;

import javax.ws.rs.core.UriBuilder;

import com.google.inject.Provider;
import com.hp.hpl.jena.rdf.model.Model;
import com.vividsolutions.jts.geom.Point;

import io.car.server.core.entities.Measurement;
import io.car.server.rest.encoding.rdf.RDFLinker;
import io.car.server.rest.resources.MeasurementsResource;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.rights.AccessRights;

/**
 * 
 * @author Arne de Wall
 * 
 */
public class W3CGeoMeasurementLinker implements RDFLinker<Measurement> {

    @Override
    public void link(Model m, Measurement t, AccessRights rights,
            Provider<UriBuilder> uriBuilder) {
        UriBuilder measurementBuilder = uriBuilder.get()
                .path(RootResource.class).path(RootResource.MEASUREMENTS)
                .path(MeasurementsResource.MEASUREMENT);

        if (t.getGeometry() instanceof Point) {
            Point p = (Point) t.getGeometry();
            m.createResource(
                    measurementBuilder.build(t.getIdentifier()).toASCIIString())
                    .addLiteral(W3CGeo.lat, p.getY())
                    .addLiteral(W3CGeo.lon, p.getX()); 
        }
    }
}
