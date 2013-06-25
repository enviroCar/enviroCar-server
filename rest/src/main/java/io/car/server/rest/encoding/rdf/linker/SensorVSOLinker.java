/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.rest.encoding.rdf.linker;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.google.inject.Provider;
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.vocabulary.RDFS;

import io.car.server.core.entities.Sensor;
import io.car.server.rest.encoding.rdf.RDFLinker;

import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.SensorsResource;
import io.car.server.rest.rights.AccessRights;
import java.util.Map;

/**
 *
 * @author jan
 */
public class SensorVSOLinker implements RDFLinker<Sensor> {

    @Override
    public void link(Model m, Sensor t, AccessRights rights,
            Provider<UriBuilder> uriBuilder) {
        
        if (t.getType().equals("car")) {
            final Map<String, Object> p = t.getProperties();
            
        
        
        UriBuilder sensorURIBuilder = uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.SENSORS)
                .path(SensorsResource.SENSOR);
        //Subclass of  http://purl.org/goodrelations/v1#ProductOrService
        m.setNsPrefix("vso", VSO.URI);
       
        //hier Hauptattribut angeben?
        URI uri = sensorURIBuilder.build(t.getProperties());
        final Resource sensor = m.createResource(uri.toASCIIString(), VSO.Automobile);
        
        final String fuelType = (String) p.get("fuelType");
        Resource ft = null;
        if (fuelType.equals("diesel")) {
            ft = m.createResource("http://dbpedia.org/resource/Diesel");
        }
        else if (fuelType.equals("gasoline")) {
            ft = m.createResource("http://dbpedia.org/resource/Gasoline");
        }
        else if (fuelType.equals("biodiesel")) {
            ft = m.createResource("http://dbpedia.org/resource/Biodiesel");
        }
        else if (fuelType.equals("kerosene")) {
            ft = m.createResource("http://dbpedia.org/resource/Kerosene");
        }
        if (ft != null) {
            sensor.addProperty(VSO.fuelType, ft);
        }
 
        final String modelDate = (Number) p.get("constructionYear") + "-01-01";
        sensor.addProperty(VSO.modelDate, modelDate, XSDDatatype.XSDdate);
        
        final String hasManufacturer = (String) p.get("manufacturer");
        sensor.addLiteral(GR.hasManufacturer, hasManufacturer);
        
        final String hasMakeAndModel = (String) p.get("manufacturer") + "_" + p.get("model");
        sensor.addLiteral(GR.hasMakeAndModel, hasMakeAndModel);
        }
    }
}
