/*
 * Copyright (C) 2013 The enviroCar project
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

import java.util.Collections;
import java.util.Set;

import org.envirocar.server.core.dao.UserDao;
import org.envirocar.server.core.entities.PasswordReset;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.InvalidUserMailCombinationException;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.dao.privates.PasswordResetDAO;
import org.envirocar.server.mongo.entity.MongoPasswordReset;
import org.envirocar.server.mongo.entity.MongoUser;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateResults;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
public class MongoUserDao extends AbstractMongoDao<String, MongoUser, Users>
        implements UserDao {

    private static final Logger log = LoggerFactory
            .getLogger(MongoUserDao.class);
    private MongoTrackDao trackDao;
    private MongoMeasurementDao measurementDao;
    private MongoGroupDao groupDao;
    private MongoFuelingDao fuelingDao;
    private final PasswordResetDAO passwordResetDao;

    @Inject
    public MongoUserDao(MongoDB mongoDB, PasswordResetDAO dao) {
        super(MongoUser.class, mongoDB);
        this.passwordResetDao = dao;
    }

    @Inject
    public void setTrackDao(MongoTrackDao trackDao) {
        this.trackDao = trackDao;
    }

    @Inject
    public void setMeasurementDao(MongoMeasurementDao measurementDao) {
        this.measurementDao = measurementDao;
    }

    @Inject
    public void setFuelingDao(MongoFuelingDao fuelingDao) {
        this.fuelingDao = fuelingDao;
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
        save(mu);
        return mu;
    }

    @Override
    public void delete(User u, boolean deleteContent) {
        MongoUser user = (MongoUser) u;
        if (deleteContent) {
            trackDao.deleteUser(user);
            measurementDao.deleteUser(user);
        } else {
            trackDao.removeUser(user);
            measurementDao.removeUser(user);
        }
        groupDao.removeUser(user);
        fuelingDao.removeUser(user);
        Key<MongoUser> userRef = key(user);
        UpdateResults result = update(
                q().field(MongoUser.FRIENDS).hasThisElement(userRef),
                up().removeAll(MongoUser.FRIENDS, userRef));
        if (result.getWriteResult() != null && !result.getWriteResult().wasAcknowledged()) {
            log.error("Error removing user {} as friend: {}",
                    u, result.getWriteResult());
        } else {
            log.debug("Removed user {} from {} friend lists",
                    u, result.getUpdatedCount());
        }
        delete(user.getName());
    }

    @Override
    protected Users createPaginatedIterable(Iterable<MongoUser> i, Pagination p,
            long count) {
        return Users.from(i).withPagination(p).withElements(count).build();
    }

    @Override
    public Users getFriends(User user) {
        Iterable<MongoUser> friends;
        Set<Key<MongoUser>> friendRefs = getFriendRefs(user);
        if (friendRefs != null) {
            friends = deref(MongoUser.class, friendRefs);
        } else {
            friends = Collections.emptyList();
        }
        return Users.from(friends).build();
    }

    @Override
    public User getFriend(User user, String friendName) {
        Set<Key<MongoUser>> friendRefs = getFriendRefs(user);
        if (friendRefs != null) {
            Key<MongoUser> friendRef = key(new MongoUser(friendName));
            getMapper().updateCollection(friendRef);
            if (friendRefs.contains(friendRef)) {
                return deref(MongoUser.class, friendRef);
            }
        }
        return null;
    }

    @Override
    public void addFriend(User user, User friend) {
        MongoUser g = (MongoUser) user;
        update(g.getName(), up()
                .add(MongoUser.FRIENDS, key(friend))
                .set(MongoUser.LAST_MODIFIED, new DateTime()));
    }

    @Override
    public void removeFriend(User user, User friend) {
        MongoUser g = (MongoUser) user;
        update(g.getName(), up()
                .removeAll(MongoUser.FRIENDS, key(friend))
                .set(MongoUser.LAST_MODIFIED, new DateTime()));
    }

    @Override
    protected Users fetch(Query<MongoUser> q, Pagination p) {
        return super.fetch(q.retrievedFields(false, MongoUser.FRIENDS), p);
    }

    public Set<Key<MongoUser>> getFriendRefs(User user) {
        MongoUser u = (MongoUser) user;
        Set<Key<MongoUser>> friendRefs = u.getFriends();
        if (friendRefs == null) {
            MongoUser userWithFriends = q()
                    .field(MongoUser.NAME).equal(u.getName())
                    .retrievedFields(true, MongoUser.FRIENDS).get();
            if (userWithFriends != null) {
                friendRefs = userWithFriends.getFriends();
            }
        }
        if (friendRefs == null) {
            friendRefs = Collections.emptySet();
        }
        return friendRefs;
    }

    public Set<Key<MongoUser>> getBidirectionalFriendRefs(User user) {
        final Set<Key<MongoUser>> friendRefs = getFriendRefs(user);
        final Set<String> ids = Sets.newHashSetWithExpectedSize(friendRefs.size());
        for (Key<MongoUser> key : friendRefs) {
            ids.add((String) key.getId());
        }

        if (ids.isEmpty()) {
            return Sets.newHashSet();
        }

        final Iterable<Key<MongoUser>> filtered = q()
                .field(MongoUser.NAME).in(ids)
                .field(MongoUser.FRIENDS).hasThisElement(key(user))
                .fetchKeys();
        return Sets.newHashSet(filtered);
    }

    @Override
    public PasswordReset requestPasswordReset(User user) throws BadRequestException {
        if (user == null || user.getName() == null) {
            throw new InvalidUserMailCombinationException();
        }

        return this.passwordResetDao.requestPasswordReset(user);
    }

    @Override
    public void resetPassword(User user, String verificationCode) throws BadRequestException {
        MongoPasswordReset status = this.passwordResetDao.getPasswordResetStatus(user, verificationCode);

        if (status != null && !status.isExpired()) {
            if (status.getCode().equals(verificationCode)) {
                save(user);
            } else {
                throw new BadRequestException("Wrong verification code.");
            }
        } else {
            throw new BadRequestException("Verification code already expired.");
        }

        this.passwordResetDao.remove(status);
    }

    @Override
    public Users getPendingIncomingFriendRequests(User user) {
        final Set<Key<MongoUser>> friendRefs = getFriendRefs(user);
        final Set<String> ids = Sets.newHashSetWithExpectedSize(friendRefs.size());

        for (Key<MongoUser> key : friendRefs) {
            ids.add((String) key.getId());
        }
        final Iterable<Key<MongoUser>> filtered;

        if (ids.isEmpty()) {
            filtered = q()
                    .field(MongoUser.FRIENDS).hasThisElement(key(user))
                    .fetchKeys();
        } else {
            filtered = q()
                    .field(MongoUser.NAME).notIn(ids)
                    .field(MongoUser.FRIENDS).hasThisElement(key(user))
                    .fetchKeys();
        }

        return Users.from(deref(MongoUser.class, filtered)).build();
    }

    @Override
    public Users getPendingOutgoingFriendRequests(User user) {
        Set<Key<MongoUser>> candidates = getFriendRefs(user);

        Set<Key<MongoUser>> biDis = getBidirectionalFriendRefs(user);

        Set<Key<MongoUser>> result = Sets.difference(candidates, biDis).immutableCopy();

        return Users.from(deref(MongoUser.class, result)).build();
    }

}
