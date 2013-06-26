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
package io.car.server.mongo.dao;

import static io.car.server.mongo.util.MongoUtils.reverse;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;

import io.car.server.core.activities.Activities;
import io.car.server.core.activities.Activity;
import io.car.server.core.dao.ActivityDao;
import io.car.server.core.filter.ActivityFilter;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.MongoDB;
import io.car.server.mongo.activities.MongoActivity;
import io.car.server.mongo.entity.MongoUser;

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
}
