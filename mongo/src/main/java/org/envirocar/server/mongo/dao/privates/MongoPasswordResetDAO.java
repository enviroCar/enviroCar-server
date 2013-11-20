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
package org.envirocar.server.mongo.dao.privates;


import java.util.Calendar;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.envirocar.server.core.entities.PasswordReset;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.core.util.UpCastingIterable;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.dao.AbstractMongoDao;
import org.envirocar.server.mongo.entity.MongoPasswordReset;
import org.envirocar.server.mongo.entity.MongoTrack;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;

public class MongoPasswordResetDAO extends AbstractMongoDao<ObjectId, MongoPasswordReset, MongoPasswordResetDAO.MongoPasswordResetStatusCollection> 
	implements PasswordResetDAO {

    private MongoDB mongo;

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
	

	public PasswordReset requestPasswordReset(User user) throws BadRequestException {
		MongoPasswordReset status = getPasswordResetStatus(user);
		
		if (status == null || status.isExpired()) {
			MongoPasswordReset result = createNewPasswordReset(user);
			return result;
		}
		else {
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
		
		save((MongoPasswordReset) entity);
		
		return entity;
	}

	private MongoPasswordReset createMongoPasswordResetStatus() {
		MongoPasswordReset result = new MongoPasswordReset();
		result.setMongoDB(this.mongo);
		return result;
	}


	public MongoPasswordReset getPasswordResetStatus(User user) {
		Query<MongoPasswordReset> result = q().field(MongoTrack.USER).equal(key(user));
		
		if (result.countAll() > 0) {
			return result.fetch().iterator().next();
		}
		return null;
	}

	@Override
	public void remove(MongoPasswordReset status) {
		delete(status.getId());
	}
	
	public static class MongoPasswordResetStatusCollection extends UpCastingIterable<MongoPasswordReset> {

		public MongoPasswordResetStatusCollection(
				Iterable<? extends MongoPasswordReset> delegate,
				Pagination pagination, long elements) {
			super(delegate, pagination, elements);
		}
		
	}


}
