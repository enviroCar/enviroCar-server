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
package org.envirocar.server.mongo.dao.privates;

import com.google.inject.Inject;
import com.mongodb.client.MongoCursor;
import dev.morphia.Datastore;
import dev.morphia.Key;
import dev.morphia.query.Query;
import org.envirocar.server.core.entities.EntityFactory;
import org.envirocar.server.core.entities.PasswordReset;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoPasswordReset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

public class MongoPasswordResetDAO implements PasswordResetDAO {
    private static final Logger LOG = LoggerFactory.getLogger(MongoPasswordResetDAO.class);
    private final EntityFactory entityFactory;
    private final MongoDB mongoDB;
    private final Datastore datastore;

    @Inject
    public MongoPasswordResetDAO(MongoDB mongoDB, EntityFactory entityFactory) {
        this.mongoDB = Objects.requireNonNull(mongoDB);
        this.entityFactory = Objects.requireNonNull(entityFactory);
        datastore = mongoDB.getDatastore();
    }

    @Override
    public synchronized PasswordReset requestPasswordReset(User user) throws BadRequestException {
        MongoPasswordReset status = getPasswordResetStatus(user);
        if (status == null || status.isExpired()) {
            return createNewPasswordReset(user);
        } else {
            throw new BadRequestException("The given user already has requested a verification code.");
        }
    }

    private MongoPasswordReset createNewPasswordReset(User user) {
        MongoPasswordReset entity = (MongoPasswordReset) entityFactory.createPasswordReset();
        entity.setCode(UUID.randomUUID().toString());
        entity.setUser(user);

        datastore.save(entity);
        LOG.info("Stored password reset status for user {} (key={})", user, mongoDB.key(user));

        return entity;
    }

    @Override
    public synchronized MongoPasswordReset getPasswordResetStatus(User user) {
        return getPasswordResetStatus(user, null);
    }

    @Override
    public synchronized MongoPasswordReset getPasswordResetStatus(User user, String verificationCode) {
        Key<User> key = mongoDB.key(user);
        LOG.info("Querying password reset status for user {} (key={})", user, key);

        Query<MongoPasswordReset> q = q().field(MongoPasswordReset.USER).equal(key);

        if (verificationCode != null) {
            q.field(MongoPasswordReset.VERIFICATION_CODE).equal(verificationCode);
        }

        try (MongoCursor<MongoPasswordReset> fetch = q.find()) {
            if (fetch.hasNext()) {
                return fetch.next();
            }
            LOG.info("No result for query.");
            return null;
        }
    }

    @Override
    public synchronized void remove(MongoPasswordReset status) {
        datastore.delete(q().field(MongoPasswordReset.ID).equal(status.getId()));
    }

    private Query<MongoPasswordReset> q() {
        return datastore.createQuery(MongoPasswordReset.class);
    }
}
