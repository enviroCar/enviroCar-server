/*
 * Copyright (C) 2013-2020 The enviroCar project
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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class JtsModule extends AbstractModule {

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

}
