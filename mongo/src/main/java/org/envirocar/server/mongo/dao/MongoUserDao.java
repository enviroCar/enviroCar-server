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

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.mongodb.DuplicateKeyException;
import com.mongodb.client.MongoCursor;
import dev.morphia.Key;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.UpdateOperations;
import dev.morphia.query.UpdateResults;
import org.envirocar.server.core.dao.UserDao;
import org.envirocar.server.core.entities.PasswordReset;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.exception.InvalidUserMailCombinationException;
import org.envirocar.server.core.util.CloseableIterator;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.dao.privates.PasswordResetDAO;
import org.envirocar.server.mongo.entity.MongoPasswordReset;
import org.envirocar.server.mongo.entity.MongoUser;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall
 */
public class MongoUserDao extends AbstractMongoDao<String, MongoUser, Users> implements UserDao {
    private static final Logger LOG = LoggerFactory.getLogger(MongoUserDao.class);
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
    public MongoUser getByName(final String name, boolean includeUnconfirmed) {
        return get(q().field(MongoUser.NAME).equal(name), includeUnconfirmed);
    }

    @Override
    public MongoUser getByMail(String mail, boolean includeUnconfirmed) {
        return get(q().field(MongoUser.MAIL).equal(mail), includeUnconfirmed);
    }

    private MongoUser get(Query<MongoUser> q, boolean includeUnconfirmed) {
        if (!includeUnconfirmed) {
            q = q.field(MongoUser.CONFIRMATION_CODE).doesNotExist();
        }
        return q.first();
    }

    @Override
    public Users get(Pagination p) {
        return fetch(q().field(MongoUser.CONFIRMATION_CODE).doesNotExist()
                        .order(Sort.descending(MongoUser.CREATION_DATE)), p);
    }

    @Override
    public MongoUser create(User user) {

        // add a confirmation code
        user.setConfirmationCode(UUID.randomUUID().toString());
        user.setExpirationDate(DateTime.now().plus(Duration.standardDays(1)));

        try {
            save(user);
        } catch (DuplicateKeyException ex) {
            LOG.warn("Error saving user, retrying with different confirmation code", ex);
            user.setConfirmationCode(UUID.randomUUID().toString());
            save(user);
        }
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
            LOG.error("Error removing user {} as friend: {}", u, result.getWriteResult());
        } else {
            LOG.debug("Removed user {} from {} friend lists", u, result.getUpdatedCount());
        }
        delete(user.getName());
    }

    @Override
    protected Users createPaginatedIterable(MongoCursor<MongoUser> i, Pagination p, long count) {
        return Users.from(asCloseableIterator(i)).withPagination(p).withElements(count).build();
    }

    @Override
    public Users getFriends(User user) {
        Set<MongoUser> friends = ((MongoUser) user).getFriends();
        return Users.from(friends == null ? Collections.emptySet() : friends).build();
    }

    @Override
    public User getFriend(User user, String friendName) {
        if (friendName == null || friendName.isEmpty()) {
            return null;
        }
        final Set<MongoUser> friends = ((MongoUser) user).getFriends();
        if (friends == null || friends.isEmpty()) {
            return null;
        }
        return friends.stream().filter(friend -> friend.getName().equals(friendName)).findFirst().orElse(null);
    }

    @Override
    public void addFriend(User user, User friend) {
        MongoUser g = (MongoUser) user;
        update(g.getName(), up().addToSet(MongoUser.FRIENDS, key(friend))
                                .set(MongoUser.LAST_MODIFIED, DateTime.now()));
    }

    @Override
    public void removeFriend(User user, User friend) {
        MongoUser g = (MongoUser) user;
        update(g.getName(), up().removeAll(MongoUser.FRIENDS, key(friend))
                                .set(MongoUser.LAST_MODIFIED, DateTime.now()));
    }

    @Override
    protected Users fetch(Query<MongoUser> q, Pagination p) {
        return super.fetch(q.field(MongoUser.CONFIRMATION_CODE).doesNotExist().project(MongoUser.FRIENDS, false), p);
    }

    private Set<MongoUser> getFriendRefs(User user) {
        Set<MongoUser> friends = ((MongoUser) user).getFriends();
        return friends == null ? Collections.emptySet() : friends;
    }

    public Set<MongoUser> getBidirectionalFriends(User user) {
        Set<String> ids = getFriendRefs(user).stream().map(MongoUser::getName).collect(toSet());

        if (ids.isEmpty()) {
            return Collections.emptySet();
        }

        return getFriendRefs(user).stream()
                                  .filter(friend -> friend.getFriends().stream().anyMatch(user::equals))
                                  .collect(toSet());

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
        final Set<MongoUser> friendRefs = getFriendRefs(user);
        final Set<String> ids = friendRefs.stream().map(User::getName).collect(toSet());
        final MongoCursor<MongoUser> filtered;

        if (ids.isEmpty()) {
            filtered = q().field(MongoUser.FRIENDS).hasThisElement(key(user)).find();
        } else {
            filtered = q().field(MongoUser.NAME).notIn(ids)
                          .field(MongoUser.FRIENDS).hasThisElement(key(user))
                          .find();
        }

        return Users.from(CloseableIterator.of(filtered)).build();
    }

    @Override
    public Users getPendingOutgoingFriendRequests(User user) {
        Set<MongoUser> candidates = getFriendRefs(user);

        Set<MongoUser> biDis = getBidirectionalFriends(user);

        Set<MongoUser> result = Sets.difference(candidates, biDis).immutableCopy();

        return Users.from(result).build();
    }

    @Override
    public User confirm(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        Query<MongoUser> query = q().field(MongoUser.CONFIRMATION_CODE).equal(code);
        UpdateOperations<MongoUser> update = up().unset(MongoUser.CONFIRMATION_CODE)
                                                 .unset(MongoUser.EXPIRE_AT)
                                                 .set(MongoUser.LAST_MODIFIED, DateTime.now());
        return findAndModify(query, update, true);
    }

}
