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

import org.envirocar.server.rest.encoding.rdf.vocab.SSN;
import org.envirocar.server.rest.encoding.rdf.vocab.DUL;
import javax.ws.rs.core.UriBuilder;

import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValue;
import org.envirocar.server.core.entities.MeasurementValues;
import org.envirocar.server.rest.encoding.rdf.vocab.W3CGeo;
import org.envirocar.server.rest.resources.PhenomenonsResource;
import org.envirocar.server.rest.resources.RootResource;
import org.envirocar.server.rest.resources.SensorsResource;
import org.envirocar.server.rest.rights.AccessRights;
import org.joda.time.format.DateTimeFormatter;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.vividsolutions.jts.geom.Point;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MeasurementSSNLinker extends AbstractSSNLinker<Measurement> {
    private static final String TIME_FRAGMENT = "time";
    private static final String CREATIONTIME_FRAGMENT = "creation-time";
    private static final String SENSOR_FRAGMENT = "sensor";
    private static final String SENSING_FRAGMENT = "sensing";
    private static final String VALUE_FRAGMENT_POSTFIX = "_value";
    private static final String OUT_FRAGMENT_POSTFIX = "_out";
    public static final String UNIT_FRAGMENT = "unit";
    public static final String FEATURE_FRAGMENT = "feature";
    private final DateTimeFormatter formatter;

    @Inject
    public MeasurementSSNLinker(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    protected void link1(Model m, Measurement t, AccessRights rights,
                         Resource measurement, Provider<UriBuilder> uriBuilder) {
        MeasurementValues values = t.getValues();

        Resource samplingTime = createSamplingTime(m, measurement, t);
        Resource resultTime = createResultTime(m, measurement, t);
        Resource feature = createFeatureOfInterest(m, measurement, t);
        Resource ourSensor = createOurSensor(m, uriBuilder, t);
        Resource sensor = createSensor(m, ourSensor);
        Resource sensing = createSensing(m, sensor, ourSensor);

        for (MeasurementValue v : values) {
            Resource phenomenon = createPhenomenon(m, uriBuilder, v);
            Resource unit = createUnit(m, phenomenon, v);

            phenomenon.addProperty(SSN.isPropertyOf, feature);
            feature.addProperty(SSN.hasProperty, phenomenon);
            phenomenon.addProperty(DUL.isClassifiedBy, unit);
            sensing.addProperty(SSN.hasOutput, phenomenon);
            Resource sensorOutput =
                    createSensorOutput(m, measurement, v, sensor, unit);
            Resource observation = m.createResource(fragment(
                    measurement, v.getPhenomenon().getName()));

            observation.addProperty(SSN.observedProperty, phenomenon);
            observation.addProperty(SSN.observationResultTime, resultTime);
            observation.addProperty(SSN.observationSamplingTime, samplingTime);
            observation.addProperty(SSN.featureOfInterest, feature);
            observation.addProperty(SSN.observedBy, sensor);
            observation.addProperty(SSN.observationResult, sensorOutput);
        }

        measurement.addProperty(RDF.type, SSN.Observation);
    }

    protected Resource createResultTime(Model m, Resource r, Measurement t) {
        Resource creationTime = m
                .createResource(fragment(r, CREATIONTIME_FRAGMENT));
        creationTime.addProperty(RDF.type, DUL.TimeInterval);
        creationTime.addProperty(DUL.hasDataValue,
                                 formatter.print(t.getCreationTime()),
                                 XSDDatatype.XSDdateTime);
        return creationTime;
    }

    protected Resource createSamplingTime(Model m, Resource r, Measurement t) {
        Resource time = m.createResource(fragment(r, TIME_FRAGMENT));
        time.addProperty(RDF.type, DUL.TimeInterval);
        time.addProperty(DUL.hasDataValue,
                         formatter.print(t.getTime()),
                         XSDDatatype.XSDdateTime);
        return time;
    }

    protected Resource createFeatureOfInterest(Model m, Resource r,
                                               Measurement t) {
        Resource feature = m.createResource(fragment(r, FEATURE_FRAGMENT));
        feature.addProperty(RDF.type, SSN.FeatureOfInterest);
        if (t.getGeometry() instanceof Point) {
            m.setNsPrefix(W3CGeo.PREFIX, W3CGeo.URI);
            Point p = (Point) t.getGeometry();
            r.addLiteral(W3CGeo.lat, p.getY())
                    .addLiteral(W3CGeo.lon, p.getX());
        }
        return feature;
    }

    protected Resource createOurSensor(Model m,
                                       Provider<UriBuilder> uriBuilder,
                                       Measurement t) {
        Resource ourSensor = m.createResource(uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.SENSORS)
                .path(SensorsResource.SENSOR)
                .build(t.getSensor().getIdentifier())
                .toASCIIString());
        return ourSensor;
    }

    protected Resource createSensor(Model m, Resource ourSensor) {
        Resource sensor = m.createResource(fragment(ourSensor, SENSOR_FRAGMENT));
        sensor.addProperty(RDF.type, SSN.Sensor);
        return sensor;
    }

    protected Resource createSensing(Model m, Resource sensor,
                                     Resource ourSensor) {
        Resource sensing = m.createResource(fragment(sensor, SENSING_FRAGMENT));
        sensing.addProperty(RDF.type, SSN.Sensing);
        sensing.addProperty(SSN.hasInput, ourSensor);
        return sensing;
    }

    protected Resource createAmount(Model m, Resource measurement,
                                    MeasurementValue v, Resource unit) {
        Resource amount = m.createResource(
                fragment(measurement, v.getPhenomenon().getName() +
                                      VALUE_FRAGMENT_POSTFIX));
        amount.addProperty(RDF.type, DUL.Amount);
        amount.addProperty(DUL.isClassifiedBy, unit);

        if (v.getValue() instanceof Number) {
            amount.addLiteral(DUL.hasDataValue,
                              ((Number) v.getValue()).doubleValue());
        } else if (v.getValue() instanceof Boolean) {
            amount.addLiteral(DUL.hasDataValue,
                              ((Boolean) v.getValue()).booleanValue());
        } else if (v.getValue() instanceof String) {
            amount.addProperty(DUL.hasDataValue,
                               (String) v.getValue(),
                               XSDDatatype.XSDstring);
        } else {
            amount.addProperty(DUL.hasDataValue,
                               v.getValue().toString(),
                               XSDDatatype.XSDstring);
        }
        return amount;
    }

    protected Resource createSensorOutput(Model m, Resource measurement,
                                          MeasurementValue v, Resource sensor,
                                          Resource unit) {
        Resource sensorOutput = m.createResource(
                fragment(measurement, v.getPhenomenon().getName() +
                                      OUT_FRAGMENT_POSTFIX));
        sensorOutput.addProperty(RDF.type, SSN.SensorOutput);
        sensorOutput.addProperty(SSN.isProducedBy, sensor);
        Resource amount = createAmount(m, measurement, v, unit);
        sensorOutput.addProperty(SSN.hasValue, amount);
        return sensorOutput;
    }

    protected Resource createPhenomenon(Model m,
                                        Provider<UriBuilder> uriBuilder,
                                        MeasurementValue v) {
        Resource phenomenon = m.createResource(uriBuilder.get()
                .path(RootResource.class)
                .path(RootResource.PHENOMENONS)
                .path(PhenomenonsResource.PHENOMENON)
                .build(v.getPhenomenon().getName()).toASCIIString());
        phenomenon.addProperty(RDF.type, SSN.Property);
        return phenomenon;
    }

    protected Resource createUnit(Model m, Resource phenomenon,
                                  MeasurementValue v) {
        Resource unit = m
                .createResource(fragment(phenomenon, UNIT_FRAGMENT));
        unit.addProperty(RDF.type, DUL.UnitOfMeasure);
        unit.addLiteral(RDFS.comment, v.getPhenomenon().getUnit());
        return unit;
    }
}
