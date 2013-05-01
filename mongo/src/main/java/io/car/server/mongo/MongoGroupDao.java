/**
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
package io.car.server.mongo;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;

import io.car.server.core.Group;
import io.car.server.core.Groups;
import io.car.server.core.User;
import io.car.server.core.db.GroupDao;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MongoGroupDao extends BasicDAO<MongoGroup, String> implements GroupDao {
    @Inject
    public MongoGroupDao(Datastore ds) {
        super(MongoGroup.class, ds);
    }

    @Override
    public MongoGroup getByName(String name) {
        return createQuery().field(MongoGroup.NAME).equal(name).get();
    }

    @Override
    public Groups search(String search) {
        Query<MongoGroup> q = createQuery();
        q.or(q.criteria(MongoGroup.NAME).containsIgnoreCase(search),
             q.criteria(MongoGroup.DESCRIPTION).containsIgnoreCase(search));
        return fetch(q);
    }

    @Override
    public Groups getByOwner(User owner) {
        return fetch(createQuery().field(MongoGroup.OWNER).equal(owner));
    }

    protected Groups fetch(Query<MongoGroup> q) {
        return new Groups(find(q).fetch());
    }

    @Override
    public Groups getAll(int limit) {
        return fetch(createQuery().limit(limit));
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
}
