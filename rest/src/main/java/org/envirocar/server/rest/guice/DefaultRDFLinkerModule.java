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
package org.envirocar.server.rest.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.vividsolutions.jts.geom.Geometry;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.rest.encoding.rdf.RDFLinker;
import org.envirocar.server.rest.encoding.rdf.linker.*;

public class DefaultRDFLinkerModule extends AbstractModule {
    @Override
    protected void configure() {
        bindGeometryLinker();
        bindMeasurementLinker();
        bindPhenomenonLinker();
        bindSensorLinker();
        bindStatisticLinker();
        bindTrackLinker();
    }

    protected void bindGeometryLinker() {
        Multibinder<RDFLinker<Geometry>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Geometry>>() {
                });
    }


    protected void bindMeasurementLinker() {
        Multibinder<RDFLinker<Measurement>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Measurement>>() {
                });
        b.addBinding().to(W3CGeoMeasurementLinker.class);
        b.addBinding().to(MeasurementDCTermsLinker.class);
        b.addBinding().to(MeasurementSSNLinker.class);
        b.addBinding().to(MeasurementDULLinker.class);
    }

    protected void bindPhenomenonLinker() {
        Multibinder<RDFLinker<Phenomenon>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Phenomenon>>() {
                });
        b.addBinding().to(PhenomenonDCTermsLinker.class);
        b.addBinding().to(EEAPhenomenonLinker.class);
        b.addBinding().to(PhenomenonSSNLinker.class);
        b.addBinding().to(DBPediaPhenomenonLinker.class);
    }

    protected void bindSensorLinker() {
        Multibinder<RDFLinker<Sensor>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Sensor>>() {
                });
        b.addBinding().to(SensorVSOLinker.class);
        b.addBinding().to(SensorDCTermsLinker.class);
        b.addBinding().to(SensorSSNLinker.class);
    }

    protected void bindStatisticLinker() {
        Multibinder<RDFLinker<Statistic>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Statistic>>() {
                });
        b.addBinding().to(StatisticDCTermsLinker.class);
    }

    protected void bindTrackLinker() {
        Multibinder<RDFLinker<Track>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Track>>() {
                });
//        b.addBinding().to(TrackMeasurementsLinker.class);
        b.addBinding().to(TrackDULLinker.class);
        b.addBinding().to(TrackDCTermsLinker.class);
    }

}
