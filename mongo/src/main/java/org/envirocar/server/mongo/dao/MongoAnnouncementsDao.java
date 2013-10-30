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
import org.envirocar.server.core.dao.AnnouncementsDao;
import org.envirocar.server.core.entities.Announcement;
import org.envirocar.server.core.entities.Announcements;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoAnnouncement;

import com.google.inject.Inject;

public class MongoAnnouncementsDao extends AbstractMongoDao<ObjectId, MongoAnnouncement, Announcements>
		implements AnnouncementsDao {

	@Inject
	public MongoAnnouncementsDao(MongoDB mongoDB) {
		super(MongoAnnouncement.class, mongoDB);
	}

	@Override
	public Announcements get(Pagination p) {
		return fetch(q(), p);
	}

	@Override
	protected Announcements createPaginatedIterable(
			Iterable<MongoAnnouncement> i, Pagination p, long count) {
		return Announcements.from(i).withPagination(p).withElements(count).build();
	}

	@Override
	public Announcement getById(String id) {
		ObjectId oid;
		try {
			oid = new ObjectId(id);
		} catch (IllegalArgumentException e) {
			return null;
		}
		return super.get(oid);
	}

}
