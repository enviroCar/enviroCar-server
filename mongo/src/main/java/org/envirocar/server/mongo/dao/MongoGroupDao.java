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

import com.google.inject.Inject;
import com.mongodb.client.MongoCursor;
import dev.morphia.Key;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.UpdateResults;
import org.envirocar.server.core.dao.GroupDao;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Groups;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.mongo.MongoDB;
import org.envirocar.server.mongo.entity.MongoGroup;
import org.envirocar.server.mongo.entity.MongoUser;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoGroupDao extends AbstractMongoDao<String, MongoGroup, Groups> implements GroupDao {
    private static final Logger log = LoggerFactory.getLogger(MongoGroupDao.class);

    @Inject
    public MongoGroupDao(MongoDB mongoDB) {
        super(MongoGroup.class, mongoDB);
    }

    @Override
    public MongoGroup getByName(String name) {
        return q().field(MongoGroup.NAME).equal(name).first();
    }

    @Override
    public Groups getByOwner(User owner, Pagination p) {
        return fetch(q().field(MongoGroup.OWNER).equal(key(owner)), p);
    }

    @Override
    public Groups get(Pagination p) {
        return fetch(q().order(Sort.descending(MongoGroup.LAST_MODIFIED)), p);
    }

    @Override
    public MongoGroup create(Group group) {
        return save(group);
    }

    @Override
    public MongoGroup save(Group group) {
        MongoGroup mug = (MongoGroup) group;
        save(mug);
        return mug;
    }

    @Override
    public void delete(Group group) {
        delete(group.getName());
    }

    @Override
    public Groups search(String search, Pagination p) {
        Query<MongoGroup> q = q();
        q.or(q.criteria(MongoGroup.NAME).containsIgnoreCase(search),
             q.criteria(MongoGroup.DESCRIPTION).containsIgnoreCase(search));
        return fetch(q, p);
    }

    void removeUser(MongoUser user) {
        removeOwner(user);
        removeMember(user);
    }

    @Override
    protected Groups createPaginatedIterable(MongoCursor<MongoGroup> i, Pagination p, long count) {
        return Groups.from(asCloseableIterator(i)).withElements(count).withPagination(p).build();
    }

    @Override
    public Group get(User user, String groupName) {
        return q().field(MongoGroup.NAME).equal(groupName)
                  .field(MongoGroup.MEMBERS).hasThisElement(key(user)).first();
    }

    @Override
    public Groups getByMember(User user, Pagination p) {
        return fetch(q().field(MongoGroup.MEMBERS).hasThisElement(key(user)), p);
    }

    @Override
    public void removeMember(Group group, User user) {
        MongoGroup g = (MongoGroup) group;
        update(g.getName(), up().removeAll(MongoGroup.MEMBERS, key(user))
                                .set(MongoGroup.LAST_MODIFIED, new DateTime()));
    }

    @Override
    public void addMember(Group group, User user) {
        MongoGroup g = (MongoGroup) group;
        update(g.getName(), up().addToSet(MongoGroup.MEMBERS, key(user)).set(MongoGroup.LAST_MODIFIED, new DateTime()));
    }

    protected void removeOwner(MongoUser user) {
        UpdateResults result = update(q().field(MongoGroup.OWNER).equal(key(user)), up().unset(MongoGroup.OWNER));

        if (result.getWriteResult() != null && !result.getWriteResult().wasAcknowledged()) {
            log.error("Error removing user {} as group owner: {}",
                      user, result.getWriteResult());
        } else {
            log.debug("Removed user {} as owner from {} groups",
                      user, result.getUpdatedCount());
        }
    }

    protected void removeMember(MongoUser user) {
        Key<MongoUser> userRef = key(user);
        UpdateResults result = update(
                q().field(MongoGroup.MEMBERS).hasThisElement(userRef),
                up().removeAll(MongoGroup.MEMBERS, userRef));

        if (result.getWriteResult() != null && !result.getWriteResult().wasAcknowledged()) {
            log.error("Error removing user {} as group member: {}",
                      user, result.getWriteResult());
        } else {
            log.debug("Removed user {} as member from {} groups",
                      user, result.getUpdatedCount());
        }
    }

    @Override
    public Users getMembers(Group group, Pagination pagination) {
        return Users.from(((MongoGroup) group).getMembers()).build();
    }

    @Override
    protected Groups fetch(Query<MongoGroup> q, Pagination p) {
        return super.fetch(q.project(MongoGroup.MEMBERS, false), p);
    }

    @Override
    public User getMember(Group group, String username) {
        Set<MongoUser> memberRefs = ((MongoGroup) group).getMembers();
        if (memberRefs != null) {
            return memberRefs.stream().filter(member -> member.getName().equals(username)).findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public boolean shareGroup(User user1, User user2) {
        return q().field(MongoGroup.MEMBERS).hasAllOf(Arrays.asList(user1, user2))
                  .project(MongoGroup.NAME, true).first() != null;
    }
}
