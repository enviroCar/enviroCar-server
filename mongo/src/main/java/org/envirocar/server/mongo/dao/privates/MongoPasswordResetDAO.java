/*
 * Copyright (C) 2013-2021 The enviroCar project
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
package org.envirocar.server.mongo.dao.privates;

import com.google.inject.Inject;
import com.mongodb.client.MongoCursor;
import dev.morphia.query.Query;
import dev.morphia.query.internal.MorphiaCursor;
import org.bson.types.ObjectId;
import org.envirocar.server.core.entities.PasswordReset;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.util.UpCastingIterable;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.dao.AbstractMongoDao;
import org.envirocar.server.mongo.entity.MongoPasswordReset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.UUID;

public class MongoPasswordResetDAO extends AbstractMongoDao<ObjectId, MongoPasswordReset, MongoPasswordResetDAO.MongoPasswordResetStatusCollection>
        implements PasswordResetDAO {

    private static final Logger LOG = LoggerFactory.getLogger(MongoPasswordResetDAO.class);
    private final MongoDB mongo;

    @Inject
    public MongoPasswordResetDAO(MongoDB mongoDB) {
        super(MongoPasswordReset.class, mongoDB);
        this.mongo = mongoDB;
    }

    @Override
    protected MongoPasswordResetStatusCollection createPaginatedIterable(
            Iterable<MongoPasswordReset> i, Pagination p, long count) {
        /*
		 * not required
         */
        return null;
    }

    @Override
    public synchronized PasswordReset requestPasswordReset(User user) throws BadRequestException {
        MongoPasswordReset status = getPasswordResetStatus(user);

        if (status == null || status.isExpired()) {
            MongoPasswordReset result = createNewPasswordReset(user);
            return result;
        } else {
            throw new BadRequestException("The given user already has requested a verification code.");
        }
    }

    private MongoPasswordReset createNewPasswordReset(User user) {
        String uuid = UUID.randomUUID().toString();
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.HOUR, 24);

        MongoPasswordReset entity = createMongoPasswordResetStatus();
        entity.setCode(uuid);
        entity.setUser(user);

        save(entity);
        LOG.info("Stored password reset status for user {} (key={})", user, key(user));

        return entity;
    }

    private MongoPasswordReset createMongoPasswordResetStatus() {
        MongoPasswordReset result = new MongoPasswordReset();
        result.setMongoDB(this.mongo);
        return result;
    }

    @Override
    public synchronized MongoPasswordReset getPasswordResetStatus(User user) {
        return getPasswordResetStatus(user, null);
    }

    @Override
    public synchronized MongoPasswordReset getPasswordResetStatus(User user, String verificationCode) {
        LOG.info("Querying password reset status for user {} (key={})", user, key(user));

        Query<MongoPasswordReset> result = q().field(MongoPasswordReset.USER).equal(key(user));

        if (verificationCode != null) {
            result.field(MongoPasswordReset.VERIFICATION_CODE).equal(verificationCode);
        }

        try (MongoCursor<MongoPasswordReset> cursor = result.find()) {
            if (cursor.hasNext()) {
                return cursor.next();
            }
        }

        LOG.info("No result for query.");
        return null;
    }

    @Override
    public synchronized void remove(MongoPasswordReset status) {
        delete(status.getId());
    }

    public static class MongoPasswordResetStatusCollection extends UpCastingIterable<MongoPasswordReset> {

        public MongoPasswordResetStatusCollection(Builder<?, ?, MongoPasswordReset> builder) {
            super(builder);
        }

    }

}
