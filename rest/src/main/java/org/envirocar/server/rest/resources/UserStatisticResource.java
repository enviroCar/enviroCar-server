/*
 * Copyright (C) 2013-2022 The enviroCar project
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
package org.envirocar.server.rest.resources;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.UserStatistic;
import org.envirocar.server.core.filter.UserStatisticFilter;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.schema.Schema;

import javax.annotation.Nullable;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * TODO JavaDoc
 *
 * @author Maurin Radtke <maurin.radtke@uni-muenster.de>
 */
public class UserStatisticResource extends AbstractResource {
    private final User user;

    @AssistedInject
    public UserStatisticResource() {
        this(null);
    }

    @AssistedInject
    public UserStatisticResource(@Nullable @Assisted User user) {
        this.user = user;
    }

    @GET
    @Schema(response = Schemas.USER_STATISTIC)
    public UserStatistic UserStatistics() {
        return getUserStatisticService()
                .getUserStatistic(new UserStatisticFilter(this.user));
    }

    public User getUser() {
        return this.user;
    }
}
