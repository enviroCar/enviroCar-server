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

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;

import io.car.server.core.dao.GroupDao;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;
import io.car.server.core.entities.User;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.entity.MongoGroup;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoGroupDao extends BasicDAO<MongoGroup, String>
        implements GroupDao {
    @Inject
    public MongoGroupDao(Datastore ds) {
        super(MongoGroup.class, ds);
    }

    @Override
    public MongoGroup getByName(String name) {
        return createQuery().field(MongoGroup.NAME).equal(name).get();
    }

    @Override
    public Groups getByOwner(User owner, Pagination p) {
        Query<MongoGroup> q = createQuery().field(MongoGroup.OWNER).equal(owner);
        return fetch(q, p);
    }

    protected Groups fetch(Query<MongoGroup> q, Pagination p) {
        long count = count(q);
        q.limit(p.getLimit()).offset(p.getOffset());
        Iterable<MongoGroup> entities = find(q).fetch();
        return Groups.from(entities).withElements(count).withPagination(p)
                .build();
    }

    @Override
    public Groups get(Pagination p) {
        Query<MongoGroup> q = createQuery().order(MongoGroup.LAST_MODIFIED);
        return fetch(q, p);
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
        Query<MongoGroup> q =
                createQuery().field(MongoGroup.MEMBERS)
                .hasThisElement(member);
        return fetch(q, p);
    }

    @Override
    public Groups search(String search, Pagination p) {
        Query<MongoGroup> q = createQuery();
        q.or(q.criteria(MongoGroup.NAME).containsIgnoreCase(search),
             q.criteria(MongoGroup.DESCRIPTION).containsIgnoreCase(search));
        return fetch(q, p);
    }
}
