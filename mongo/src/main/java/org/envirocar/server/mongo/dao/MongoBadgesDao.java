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
package org.envirocar.server.mongo.dao;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.envirocar.server.core.dao.BadgesDao;
import org.envirocar.server.core.entities.Badges;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoBadge;

public class MongoBadgesDao extends AbstractMongoDao<ObjectId, MongoBadge, Badges>
        implements BadgesDao {

    @Inject
    public MongoBadgesDao(MongoDB mongoDB) {
        super(MongoBadge.class, mongoDB);
    }

    @Override
    public long getCount() {
        return count();
    }

    @Override
    public Badges get(Pagination p) {
        return fetch(q(), p);
    }

    @Override
    protected Badges createPaginatedIterable(
            Iterable<MongoBadge> i, Pagination p, long count) {
        return Badges.from(i).withPagination(p).withElements(count).build();
    }

}
