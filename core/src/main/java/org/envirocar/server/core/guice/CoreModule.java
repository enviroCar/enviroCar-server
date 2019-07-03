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
package org.envirocar.server.core.guice;

import com.google.common.base.Suppliers;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.envirocar.server.core.*;
import org.envirocar.server.core.dao.PrivacyStatementDao;
import org.envirocar.server.core.dao.TermsDao;
import org.envirocar.server.core.dao.TermsOfUseDao;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.envirocar.server.core.entities.Terms;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.core.util.GeodesicGeometryOperations;
import org.envirocar.server.core.util.GeometryOperations;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
public class CoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataService.class).to(DataServiceImpl.class);
        bind(StatisticsService.class).to(StatisticsServiceImpl.class);
        bind(GeometryOperations.class).to(GeodesicGeometryOperations.class);
        bind(CarSimilarityService.class).to(CarSimilarityServiceImpl.class);
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @Provides
    @Singleton
    public GeometryFactory geometryFactory(PrecisionModel precisionModel) {
        return new GeometryFactory(precisionModel, 4326);
    }

    @Provides
    @Singleton
    public PrecisionModel precisionModel() {
        return new PrecisionModel(PrecisionModel.FLOATING_SINGLE);
    }

    @Provides
    @Singleton
    public DateTimeFormatter formatter() {
        return ISODateTimeFormat.dateTimeNoMillis();
    }

    @Provides
    @Singleton
    public Supplier<Optional<PrivacyStatement>> privacyStatement(PrivacyStatementDao dao) {
        return getLatestTermsSupplier(dao);
    }

    @Provides
    @Singleton
    public Supplier<Optional<TermsOfUseInstance>> latestTermsOfUse(TermsOfUseDao dao) {
        return getLatestTermsSupplier(dao);
    }

    private static <T extends Terms> Supplier<Optional<T>> getLatestTermsSupplier(TermsDao<T, ?> dao) {
        return Suppliers.memoizeWithExpiration(() -> Optional.ofNullable(dao.getLatest()), 1, TimeUnit.DAYS)::get;
    }
}
