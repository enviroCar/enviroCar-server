/*
 * Copyright (C) 2013-2019 The enviroCar project
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
package org.envirocar.server.core.entities;

import org.envirocar.server.core.util.CloseableIterator;
import org.envirocar.server.core.util.PaginatedIterableImpl;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class Sensors extends PaginatedIterableImpl<Sensor> {
    protected Sensors(Builder builder) {
        super(builder);
    }

    public static Builder from(CloseableIterator<? extends Sensor> delegate) {
        return new Builder(delegate);
    }
    public static Builder from(Iterable<? extends Sensor> delegate) {
        return new Builder(delegate);
    }

    public static class Builder extends PaginatedIterableImpl.Builder<Builder, Sensors, Sensor> {

        protected Builder(CloseableIterator<? extends Sensor> delegate) {
            super(delegate);
        }
        protected Builder(Iterable<? extends Sensor> delegate) {
            super(delegate);
        }

        @Override
        public Sensors build() {
            return new Sensors(this);
        }
    }
}
