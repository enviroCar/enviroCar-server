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

import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.rest.encoding.rdf.RDFLinker;
import org.envirocar.server.rest.encoding.rdf.vocab.DBPedia;
import org.envirocar.server.rest.encoding.rdf.vocab.GoodRelations;
import org.envirocar.server.rest.encoding.rdf.vocab.VSO;
import org.envirocar.server.rest.rights.AccessRights;

import com.google.inject.Provider;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * @author Jan Wirwahn
 */
public class SensorVSOLinker implements RDFLinker<Sensor> {
    public static final String CONSTRUCTION_YEAR_PROPERTY = "constructionYear";
    public static final String FUEL_TYPE_PROPERTY = "fuelType";
    public static final String MANUFACTURER_PROPERTY = "manufacturer";
    public static final String MODEL_PROPERTY = "model";
    public static final String FUEL_TYPE_DIESEL = "diesel";
    public static final String FUEL_TYPE_GASOLINE = "gasoline";
    public static final String FUEL_TYPE_BIODIESEL = "biodiesel";
    public static final String FUEL_TYPE_KEROSENE = "kerosene";
    public static final String CAR_TYPE = "car";

    @Override
    public void link(Model m, Sensor t, AccessRights rights,
                     Resource sensor, Provider<UriBuilder> uriBuilder) {

        if (t.hasType() && t.getType().equals(CAR_TYPE)) {
            if (t.hasProperties()) {
                final Map<String, Object> p = t.getProperties();
                //Subclass of  http://purl.org/goodrelations/v1#ProductOrService
                m.setNsPrefix(VSO.PREFIX, VSO.URI);
                m.setNsPrefix(GoodRelations.PREFIX, GoodRelations.URI);
                m.setNsPrefix(DBPedia.PREFIX, DBPedia.URI);
                sensor.addProperty(RDF.type, VSO.Automobile);
                addFuelType(p, m, sensor);
                addContructionYear(p, sensor);

                final String manufacturer = (String) p
                        .get(MANUFACTURER_PROPERTY);
                if (manufacturer != null) {
                    sensor.addLiteral(GoodRelations.hasManufacturer,
                                      manufacturer);
                    String model = (String) p.get(MODEL_PROPERTY);
                    if (model != null) {
                        final String hasMakeAndModel =
                                manufacturer + "_" + model;
                        sensor.addLiteral(GoodRelations.hasMakeAndModel,
                                          hasMakeAndModel);
                    }
                }
            }
        }
    }

    protected void addFuelType(
            final Map<String, Object> p, Model m,
            final Resource sensor) {
        final String fuelType = (String) p.get(FUEL_TYPE_PROPERTY);
        if (fuelType != null) {
            if (fuelType.equals(FUEL_TYPE_DIESEL)) {
                sensor.addProperty(VSO.fuelType, DBPedia.DBPEDIA_DIESEL);
            } else if (fuelType.equals(FUEL_TYPE_GASOLINE)) {
                sensor.addProperty(VSO.fuelType, DBPedia.DBPEDIA_GASOLINE);
            } else if (fuelType.equals(FUEL_TYPE_BIODIESEL)) {
                sensor.addProperty(VSO.fuelType, DBPedia.DBPEDIA_BIODIESEL);
            } else if (fuelType.equals(FUEL_TYPE_KEROSENE)) {
                sensor.addProperty(VSO.fuelType, DBPedia.DBPEDIA_KEROSENE);
            }
        }
    }

    protected void addContructionYear(
            final Map<String, Object> p,
            final Resource sensor) {
        Number year = (Number) p.get(CONSTRUCTION_YEAR_PROPERTY);
        if (year != null) {
            final String modelDate = year.intValue() + "-01-01";
            sensor.addProperty(VSO.modelDate, modelDate, XSDDatatype.XSDdate);
        }
    }
}
