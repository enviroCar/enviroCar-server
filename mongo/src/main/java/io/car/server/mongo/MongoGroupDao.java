/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
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
