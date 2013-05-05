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
import io.car.server.core.Track;
import io.car.server.core.User;
import io.car.server.core.Users;
import io.car.server.core.db.UserDao;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 * @author Arne de Wall
 */
public class MongoUserDao extends BasicDAO<MongoUser, String> implements UserDao {

    @Inject
    public MongoUserDao(Datastore datastore) {
        super(MongoUser.class, datastore);
    }

    @Override
    public MongoUser getByName(String name) {
        return find(createQuery().field(MongoUser.NAME).equal(name)).get();
    }

    @Override
    public MongoUser getByMail(String mail) {
        return find(createQuery().field(MongoUser.MAIL).equal(mail)).get();
    }

    @Override
    public Users getAll() {
        return getAll(0);
    }

    @Override
    public Users getAll(int limit) {
        return fetch(createQuery().limit(limit).order(MongoUser.CREATION_DATE));
    }

    @Override
    public MongoUser create(User user) {
        return save(user);
    }

    @Override
    public MongoUser save(User user) {
        MongoUser mu = (MongoUser) user;
        save(mu);
        return mu;
    }

    @Override
    public void delete(User user) {
        delete((MongoUser) user);
    }

    @Override
    public Users getByGroup(Group group) {
        return group.getMembers();
    }

    protected Users fetch(Query<MongoUser> q) {
        return new Users(find(q).fetch());
    }

	@Override
	public Users getByTrack(Track track) {
		return fetch(createQuery().field(MongoUser.TRACKS).hasThisElement(track));
	}
}
