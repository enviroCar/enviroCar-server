/*
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.rest.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.activities.Activity;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.statistics.Statistic;
import io.car.server.rest.encoding.rdf.RDFLinker;
import io.car.server.rest.encoding.rdf.linker.PhenomenonDCTermsLinker;
import io.car.server.rest.encoding.rdf.linker.SensorVSOLinker;
import io.car.server.rest.encoding.rdf.linker.SensorDCTermsLinker;
import io.car.server.rest.encoding.rdf.linker.TrackDCTermsLinker;
import io.car.server.rest.encoding.rdf.linker.UserDCTermsLinker;
import io.car.server.rest.encoding.rdf.linker.UserFOAFLinker;
import io.car.server.rest.encoding.rdf.linker.UserVCardLinker;

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
    }

    protected void bindMeasurementLinker() {
        Multibinder<RDFLinker<Measurement>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Measurement>>() {
        });
    }

    protected void bindPhenomenonLinker() {
        Multibinder<RDFLinker<Phenomenon>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Phenomenon>>() {
        });
        b.addBinding().to(PhenomenonDCTermsLinker.class);
    }

    protected void bindSensorLinker() {
        Multibinder<RDFLinker<Sensor>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Sensor>>() {
        });
        b.addBinding().to(SensorVSOLinker.class);
        b.addBinding().to(SensorDCTermsLinker.class);
    }

    protected void bindStatisticLinker() {
        Multibinder<RDFLinker<Statistic>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Statistic>>() {
        });
    }

    protected void bindTrackLinker() {
        Multibinder<RDFLinker<Track>> b = Multibinder.newSetBinder(
                binder(), new TypeLiteral<RDFLinker<Track>>() {
        });
        b.addBinding().to(TrackDCTermsLinker.class);
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
