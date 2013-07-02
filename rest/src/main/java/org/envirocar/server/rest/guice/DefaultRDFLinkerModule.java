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
package org.envirocar.server.rest.guice;

import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.rest.encoding.rdf.RDFLinker;
import org.envirocar.server.rest.encoding.rdf.linker.ActivityDCTermsLinker;
import org.envirocar.server.rest.encoding.rdf.linker.DBPediaPhenomenonLinker;
import org.envirocar.server.rest.encoding.rdf.linker.EEAPhenomenonLinker;
import org.envirocar.server.rest.encoding.rdf.linker.GroupDCTermsLinker;
import org.envirocar.server.rest.encoding.rdf.linker.GroupFOAFLinker;
import org.envirocar.server.rest.encoding.rdf.linker.MeasurementDCTermsLinker;
import org.envirocar.server.rest.encoding.rdf.linker.MeasurementSSNLinker;
import org.envirocar.server.rest.encoding.rdf.linker.PhenomenonDCTermsLinker;
import org.envirocar.server.rest.encoding.rdf.linker.PhenomenonSSNLinker;
import org.envirocar.server.rest.encoding.rdf.linker.SensorDCTermsLinker;
import org.envirocar.server.rest.encoding.rdf.linker.SensorSSNLinker;
import org.envirocar.server.rest.encoding.rdf.linker.SensorVSOLinker;
import org.envirocar.server.rest.encoding.rdf.linker.StatisticDCTermsLinker;
import org.envirocar.server.rest.encoding.rdf.linker.TrackMeasurementsLinker;
import org.envirocar.server.rest.encoding.rdf.linker.UserDCTermsLinker;
import org.envirocar.server.rest.encoding.rdf.linker.UserFOAFLinker;
import org.envirocar.server.rest.encoding.rdf.linker.UserVCardLinker;
import org.envirocar.server.rest.encoding.rdf.linker.W3CGeoMeasurementLinker;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.vividsolutions.jts.geom.Geometry;

public class DefaultRDFLinkerModule extends AbstractModule {
    @Override
    protected void configure() {
        bindActivityLinker();
        bindGeometryLinker();
        bindGroupLinker();
        bindMeasurementLinker();
        bindPhenomenonLinker();
        bindSensorLinker();
        bindStatisticLinker();
        bindTrackLinker();
        bindUserLinker();
    }

    protected void bindActivityLinker() {
        Multibinder<RDFLinker<Activity>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Activity>>() {
        });
        b.addBinding().to(ActivityDCTermsLinker.class);
    }

    protected void bindGeometryLinker() {
        Multibinder<RDFLinker<Geometry>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Geometry>>() {
        });
    }

    protected void bindGroupLinker() {
        Multibinder<RDFLinker<Group>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Group>>() {
        });
        b.addBinding().to(GroupDCTermsLinker.class);
        b.addBinding().to(GroupFOAFLinker.class);
    }

    protected void bindMeasurementLinker() {
        Multibinder<RDFLinker<Measurement>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Measurement>>() {
        });
        b.addBinding().to(W3CGeoMeasurementLinker.class);
        b.addBinding().to(MeasurementDCTermsLinker.class);
        b.addBinding().to(MeasurementSSNLinker.class);
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
        b.addBinding().to(TrackMeasurementsLinker.class);
    }

    protected void bindUserLinker() {
        Multibinder<RDFLinker<User>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<User>>() {
        });
        b.addBinding().to(UserVCardLinker.class);
        b.addBinding().to(UserFOAFLinker.class);
        b.addBinding().to(UserDCTermsLinker.class);
    }
}
