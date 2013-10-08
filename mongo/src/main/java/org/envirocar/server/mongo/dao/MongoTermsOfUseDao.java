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

import org.bson.types.ObjectId;
import org.envirocar.server.core.dao.TermsOfUseDao;
import org.envirocar.server.core.entities.TermsOfUse;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoTermsOfUseInstance;
import org.envirocar.server.mongo.util.MongoUtils;

import com.google.inject.Inject;

/**
 * @author matthes rieke
 *
 */
public class MongoTermsOfUseDao extends AbstractMongoDao<ObjectId, MongoTermsOfUseInstance, TermsOfUse> 
	implements TermsOfUseDao {

	@Inject
	public MongoTermsOfUseDao(MongoDB mongoDB) {
		super(MongoTermsOfUseInstance.class, mongoDB);
	}

	@Override
	public TermsOfUse get(Pagination p) {
		return fetch(q().order(MongoUtils.reverse(MongoTermsOfUseInstance.CREATION_DATE)), p);
	}

	@Override
	protected TermsOfUse createPaginatedIterable(
			Iterable<MongoTermsOfUseInstance> i, Pagination p, long count) {
		return TermsOfUse.from(i).withPagination(p).withElements(count).build();
	}

	@Override
	public TermsOfUseInstance getById(String id) {
        ObjectId oid;
        try {
            oid = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return super.get(oid);
	}

}
