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

import static org.envirocar.server.mongo.util.MongoUtils.reverse;

import org.bson.types.ObjectId;
import org.envirocar.server.core.activities.Activities;
import org.envirocar.server.core.activities.Activity;
import org.envirocar.server.core.dao.ActivityDao;
import org.envirocar.server.core.filter.ActivityFilter;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.activities.MongoActivity;
import org.envirocar.server.mongo.entity.MongoUser;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoActivityDao extends AbstractMongoDao<ObjectId, MongoActivity, Activities>
        implements ActivityDao {
    private MongoGroupDao groupDao;
    private MongoUserDao userDao;

    @Inject
    public MongoActivityDao(MongoDB mongoDB) {
        super(MongoActivity.class, mongoDB);
    }

    public MongoGroupDao getGroupDao() {
        return groupDao;
    }

    @Inject
    public void setGroupDao(MongoGroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public MongoUserDao getUserDao() {
        return userDao;
    }

    @Inject
    public void setUserDao(MongoUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected Activities createPaginatedIterable(Iterable<MongoActivity> i,
                                                 Pagination p, long count) {
        return Activities.from(i).withPagination(p).withElements(count).build();
    }

    @Override
    public void save(Activity activity) {
        save((MongoActivity) activity);
    }

    @Override
    public Activities get(ActivityFilter request) {
        Query<MongoActivity> q = q();
        if (request.hasUser()) {
            MongoUser u = (MongoUser) request.getUser();
            if (request.isFriendActivities()) {
                q.field(MongoActivity.USER)
                        .in(userDao.getBidirectionalFriendRefs(u));
            } else {
                q.field(MongoActivity.USER)
                        .equal(key(u));
            }
        }
        if (request.hasGroup()) {
            q.field(MongoActivity.USER)
                    .in(groupDao.getMemberRefs(request.getGroup()));
        }
        if (request.hasType()) {
            q.field(MongoActivity.TYPE)
                    .equal(request.getType());
        }
        return fetch(q, request.getPagination());
    }

    @Override
    protected Iterable<MongoActivity> fetch(Query<MongoActivity> q) {
        return super.fetch(q.order(reverse(MongoActivity.TIME)));
    }

    @Override
    protected Activities fetch(Query<MongoActivity> q, Pagination p) {
        return super.fetch(q.order(reverse(MongoActivity.TIME)), p);
    }

    @Override
    public Activity get(ActivityFilter request, String id) {
        Query<MongoActivity> q = q();
        if (request.hasUser()) {
            MongoUser u = (MongoUser) request.getUser();
            if (request.isFriendActivities()) {
                q.field(MongoActivity.USER)
                        .in(userDao.getFriendRefs(u));
            } else {
                q.field(MongoActivity.USER)
                        .equal(key(u));
            }
        }
        if (request.hasGroup()) {
            q.field(MongoActivity.USER)
                    .in(groupDao.getMemberRefs(request.getGroup()));
        }
        if (request.hasType()) {
            q.field(MongoActivity.TYPE)
                    .equal(request.getType());
        }
        try {
            q.field(MongoActivity.ID).equal(new ObjectId(id));
        } catch (IllegalArgumentException e) {
            return null;
        }
        return q.get();
    }
}
