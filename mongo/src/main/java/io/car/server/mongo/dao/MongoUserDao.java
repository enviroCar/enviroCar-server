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
import com.github.jmkgreen.morphia.query.UpdateResults;
import com.google.inject.Inject;

import io.car.server.core.dao.UserDao;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.cache.EntityCache;
import io.car.server.mongo.entity.MongoGroup;
import io.car.server.mongo.entity.MongoUser;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
public class MongoUserDao extends AbstractMongoDao<MongoUser, Users>
        implements UserDao {
    private static final Logger log = LoggerFactory
            .getLogger(MongoUserDao.class);
    private EntityCache<MongoUser> userCache;
    private MongoTrackDao trackDao;
    private MongoMeasurementDao measurementDao;
    private MongoGroupDao groupDao;

    @Inject
    public MongoUserDao(Datastore datastore) {
        super(MongoUser.class, datastore);
    }

    @Inject
    public void setUserCache(
            EntityCache<MongoUser> userCache) {
        this.userCache = userCache;
    }

    @Inject
    public void setTrackDao(MongoTrackDao trackDao) {
        this.trackDao = trackDao;
    }

    @Inject
    public void setMeasurementDao(
            MongoMeasurementDao measurementDao) {
        this.measurementDao = measurementDao;
    }

    @Inject
    public void setGroupDao(MongoGroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public MongoUser getByName(final String name) {
        return q().field(MongoUser.NAME).equal(name).get();
    }

    @Override
    public MongoUser getByMail(String mail) {
        return q().field(MongoUser.MAIL).equal(mail).get();
    }

    @Override
    public Users get(Pagination p) {
        return fetch(q().order(MongoUser.CREATION_DATE), p);
    }

    @Override
    public MongoUser create(User user) {
        return save(user);
    }

    @Override
    public MongoUser save(User user) {
        MongoUser mu = (MongoUser) user;
        userCache.invalidate(mu);
        save(mu);
        return mu;
    }

    @Override
    public void delete(User u) {
        MongoUser user = (MongoUser) u;

        trackDao.removeUser(user);
        measurementDao.removeUser(user);
        groupDao.removeUser(user);
        UpdateResults<MongoUser> result = update(
                q().field(MongoUser.FRIENDS).hasThisElement(user),
                up().removeAll(MongoUser.FRIENDS, user));
        if (result.getHadError()) {
            log.error("Error removing user {} as friend: {}",
                      u, result.getError());
        } else {
            log.debug("Removed user {} from {} friend lists",
                      u, result.getUpdatedCount());
        }
        delete(user);
    }

    @Override
    public Users getByGroup(Group group, Pagination p) {
        return fetch(q().field(MongoUser.GROUPS).hasThisElement(group), p);
    }

    void removeGroup(MongoGroup group) {
        UpdateResults<MongoUser> result = update(
                q().field(MongoUser.GROUPS).hasThisElement(group),
                up().removeAll(MongoUser.GROUPS, group));

        if (result.getHadError()) {
            log.error("Error removing members of group {}: {}",
                      group, result.getError());
        } else {
            log.debug("Removed group from {} users",
                      group, result.getUpdatedCount());
        }
    }

    @Override
    protected Users createPaginatedIterable(Iterable<MongoUser> i, Pagination p,
                                            long count) {
        return Users.from(i).withPagination(p).withElements(count).build();
    }
}
