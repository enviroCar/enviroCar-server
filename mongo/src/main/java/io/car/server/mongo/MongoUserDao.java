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

import javax.inject.Inject;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;

import io.car.server.core.User;
import io.car.server.core.Users;
import io.car.server.core.db.UserDao;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class MongoUserDao extends BasicDAO<MongoUser, String> implements UserDao {
    @Inject
    public MongoUserDao(Datastore datastore) {
        super(MongoUser.class, datastore);
    }

    @Override
    public User getUserByName(String name) {
        return get(name);
    }

    @Override
    public User getUserByMail(String mail) {
        return find(createQuery().field(MongoUser.MAIL).equal(mail)).get();
    }

    @Override
    public Users getAll(int limit) {
        return fetch(createQuery().limit(limit).order(MongoUser.CREATION_DATE));
    }

    @Override
    public User createUser(User user) {
        // todo  check for existence
        save((MongoUser) user);
        return user;
    }

    @Override
    public User saveUser(User user) {
        save((MongoUser) user);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        delete((MongoUser) user);
    }

    protected Users fetch(Query<MongoUser> q) {
        return new Users(find(q).fetch());
    }
}
