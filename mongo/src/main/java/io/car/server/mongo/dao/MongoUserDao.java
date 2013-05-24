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

import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.dao.TrackDao;
import io.car.server.core.dao.UserDao;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.cache.EntityCache;
import io.car.server.mongo.entity.MongoUser;

import java.util.concurrent.ExecutionException;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
public class MongoUserDao extends BasicDAO<MongoUser, String> implements
        UserDao {
    private final LoadingCache<String, MongoUser> cache = CacheBuilder
            .newBuilder().maximumSize(1000)
            .build(new CacheLoader<String, MongoUser>() {
                @Override
                public MongoUser load(String key) throws UserNotFoundException {
                    MongoUser user = find(
                            createQuery().field(MongoUser.NAME).equal(key))
                            .get();
                    if (user == null) {
                        throw new UserNotFoundException();
                    } else {
                        return user;
                    }
                }
            });
    private final EntityCache<MongoUser> userCache;
    private final TrackDao trackDao;
    private final MeasurementDao measurementDao;

    @Inject
    public MongoUserDao(Datastore datastore, EntityCache<MongoUser> userCache,
            TrackDao trackDao, MeasurementDao measurementDao) {
        super(MongoUser.class, datastore);
        this.userCache = userCache;
        this.trackDao = trackDao;
        this.measurementDao = measurementDao;
    }

    @Override
    public MongoUser getByName(final String name) {
        try {
            return cache.get(name);
        } catch (ExecutionException ex) {
            if (ex.getCause() instanceof UserNotFoundException) {
                return null;
            }
            throw new RuntimeException(ex);
        }
    }

    @Override
    public MongoUser getByMail(String mail) {
        return find(createQuery().field(MongoUser.MAIL).equal(mail)).get();
    }

    @Override
    public Users get(Pagination p) {
        Query<MongoUser> q = createQuery().order(MongoUser.CREATION_DATE);
        return fetch(q, p);
    }

    @Override
    public MongoUser create(User user) {
        return save(user);
    }

    @Override
    public MongoUser save(User user) {
        MongoUser mu = (MongoUser) user;
        cache.invalidate(mu);
        userCache.invalidate(mu);
        save(mu);
        return mu;
    }

    @Override
    public void delete(User user) {
        // FIXME remove user from groups, friend lists, measurments and tracks
        Pagination page = new Pagination();
        do {
            Tracks tracks = trackDao.getByUser(user, page);
            for (Track t : tracks) {
                t.setUser(null);
                trackDao.save(t);
            }
        } while ((page = page.next(page.getSize())) != null);
        
        page = new Pagination();
        do {
            for (Measurement m : measurementDao.getByUser(user, page)) {
                m.setUser(null);
                measurementDao.save(m);
            }
        } while ((page = page.next(page.getSize())) != null);
        
        user.getFriends();
        delete((MongoUser) user);
    }

    @Override
    public Users getByGroup(Group group) {
        return group.getMembers();
    }

    protected Users fetch(Query<MongoUser> q, Pagination p) {
        long count = count(q);
        q.limit(p.getLimit()).offset(p.getOffset());
        Iterable<MongoUser> entities = find(q).fetch();
        return Users.from(entities).withElements(count).withPagination(p)
                .build();
    }

    private static class UserNotFoundException extends Exception {
        private static final long serialVersionUID = 2040283652624708863L;
    }
}
