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
package io.car.server.mongo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.query.Query;
import com.github.jmkgreen.morphia.query.UpdateResults;
import com.google.inject.Inject;

import io.car.server.core.dao.GroupDao;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;
import io.car.server.core.entities.User;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.entity.MongoGroup;
import io.car.server.mongo.entity.MongoUser;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoGroupDao extends AbstractMongoDao<String, MongoGroup, Groups>
        implements GroupDao {
    private static final Logger log = LoggerFactory
            .getLogger(MongoGroupDao.class);
    @Inject
    public MongoGroupDao(Datastore ds) {
        super(MongoGroup.class, ds);
    }

    @Override
    public MongoGroup getByName(String name) {
        return q().field(MongoGroup.NAME).equal(name).get();
    }

    @Override
    public Groups getByOwner(User owner, Pagination p) {
        return fetch(q().field(MongoGroup.OWNER).equal(owner), p);
    }

    @Override
    public Groups get(Pagination p) {
        return fetch(q().order(MongoGroup.LAST_MODIFIED), p);
    }

    @Override
    public MongoGroup create(Group group) {
        return save(group);
    }

    @Override
    public MongoGroup save(Group group) {
        MongoGroup mug = (MongoGroup) group;
        save(mug);
        return mug;
    }

    @Override
    public void delete(Group group) {
        delete((MongoGroup) group);
    }

    @Override
    public Groups getByMember(User member, Pagination p) {
        return fetch(q().field(MongoGroup.MEMBERS)
                .hasThisElement(member), p);
    }

    @Override
    public Groups search(String search, Pagination p) {
        Query<MongoGroup> q = q();
        q.or(q.criteria(MongoGroup.NAME).containsIgnoreCase(search),
             q.criteria(MongoGroup.DESCRIPTION).containsIgnoreCase(search));
        return fetch(q, p);
    }

    void removeUser(MongoUser user) {

        UpdateResults<MongoGroup> result = update(
                q().field(MongoGroup.MEMBERS).hasThisElement(user),
                up().removeAll(MongoGroup.MEMBERS, user));

        if (result.getHadError()) {
            log.error("Error removing user {} as group member: {}",
                      user, result.getError());
        } else {
            log.debug("Removed user {} from {} groups",
                      user, result.getUpdatedCount());
        }

        result = update(q().field(MongoGroup.OWNER).equal(user),
                        up().unset(MongoGroup.OWNER));

        if (result.getHadError()) {
            log.error("Error removing user {} as group owner: {}",
                      user, result.getError());
        } else {
            log.debug("Removed user {} as owner from {} groups",
                      user, result.getUpdatedCount());
        }
    }

    @Override
    protected Groups createPaginatedIterable(Iterable<MongoGroup> i,
                                             Pagination p, long count) {
        return Groups.from(i).withElements(count).withPagination(p).build();
    }
}
