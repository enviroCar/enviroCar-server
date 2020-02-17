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
package org.envirocar.server.core.entities;

import org.envirocar.server.core.util.UpCastingIterable;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class Groups extends UpCastingIterable<Group> {
    protected Groups(Builder builder) {
        super(builder);
    }

    public static Builder from(Iterable<? extends Group> delegate) {
        return new Builder(delegate);
    }

    public static class Builder extends UpCastingIterable.Builder<Builder, Groups, Group> {
        protected Builder(Iterable<? extends Group> delegate) {
            super(delegate);
        }

        @Override
        public Groups build() {
            return new Groups(this);
        }
    }
}
