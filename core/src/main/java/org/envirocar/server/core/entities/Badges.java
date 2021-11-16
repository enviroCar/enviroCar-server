/*
 * Copyright (C) 2013-2021 The enviroCar project
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

public class Badges extends UpCastingIterable<Badge> {

    protected Badges(Builder builder) {
        super(builder);
    }

    public static Builder from(Iterable<? extends Badge> delegate) {
        return new Builder(delegate);
    }

    public static class Builder extends UpCastingIterable.Builder<Builder, Badges, Badge> {
        protected Builder(Iterable<? extends Badge> delegate) {
            super(delegate);
        }

        @Override
        public Badges build() {
            return new Badges(this);
        }
    }

}
