/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

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
import dev.morphia.Key;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoGroupDao extends AbstractMongoDao<String, MongoGroup, Groups>
        implements GroupDao {
    private static final Logger log = LoggerFactory
            .getLogger(MongoGroupDao.class);
    private MongoUserDao userDao;

    @Override
    public long getCount() {
        return count();
    }

    @Inject
    public MongoGroupDao(MongoDB mongoDB) {
        super(MongoGroup.class, mongoDB);
    }

    public MongoUserDao getUserDao() {
        return this.userDao;
    }

    @Inject
    public void setUserDao(MongoUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public MongoGroup getByName(String name) {
        return q().field(MongoGroup.NAME).equal(name).get();
    }

    @Override
    public Groups getByOwner(User owner, Pagination p) {
        return fetch(q().field(MongoGroup.OWNER).equal(key(owner)), p);
    }

    @Override
    public Groups get(Pagination p) {
        return fetch(q().order(MongoGroup.LAST_MODIFIED), p);
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
    protected Groups createPaginatedIterable(Iterable<MongoGroup> i,
                                             Pagination p, long count) {
        return Groups.from(i).withElements(count).withPagination(p).build();
    }

    @Override
    public Group get(User user, String groupName) {
        return q().field(MongoGroup.NAME).equal(groupName)
                .field(MongoGroup.MEMBERS).hasThisElement(key(user)).get();
    }

    @Override
    public Groups getByMember(User user, Pagination p) {
        return fetch(q().field(MongoGroup.MEMBERS)
                .hasThisElement(key(user)), p);
    }

    @Override
    public void removeMember(Group group, User user) {
        MongoGroup g = (MongoGroup) group;
        update(g.getName(), up()
               .removeAll(MongoGroup.MEMBERS, key(user))
               .set(MongoGroup.LAST_MODIFIED, new DateTime()));
    }

    @Override
    public void addMember(Group group, User user) {
        MongoGroup g = (MongoGroup) group;
        update(g.getName(), up()
               .addToSet(MongoGroup.MEMBERS, key(user))
               .set(MongoGroup.LAST_MODIFIED, new DateTime()));
    }

    protected void removeOwner(MongoUser user) {
        UpdateResults result = update(
                q().field(MongoGroup.OWNER).equal(key(user)),
                up().unset(MongoGroup.OWNER));

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
        Set<Key<MongoUser>> memberRefs = getMemberRefs(group);
        Iterable<MongoUser> members;
        if (memberRefs != null) {
            members = deref(MongoUser.class, memberRefs);
        } else {
            members = Collections.emptyList();
        }
        return Users.from(members).build();
    }

    @Override
    protected Groups fetch(Query<MongoGroup> q, Pagination p) {
        return super.fetch(q.project(MongoGroup.MEMBERS, false), p);
    }

    @Override
    public User getMember(Group group, String username) {
        Set<Key<MongoUser>> memberRefs = getMemberRefs(group);
        if (memberRefs != null) {
            Key<MongoUser> memberRef = key(new MongoUser(username));
            getMapper().updateCollection(memberRef);
            if (memberRefs.contains(memberRef)) {
                return deref(MongoUser.class, memberRef);
            }
        }
        return null;
    }

    public Set<Key<MongoUser>> getMemberRefs(Group group) {
        MongoGroup g = (MongoGroup) group;
        Set<Key<MongoUser>> memberRefs = g.getMembers();
        if (memberRefs == null) {
            MongoGroup groupWithMembers = q()
                    .field(MongoGroup.NAME).equal(g.getName())
                    .project(MongoGroup.MEMBERS, true).first();
            if (groupWithMembers != null) {
                memberRefs = groupWithMembers.getMembers();
            }
        }
        return memberRefs;
    }

    @Override
    public boolean shareGroup(User user1, User user2) {
        Iterable<Key<User>> users = Arrays.asList(key(user1), key(user2));
        return q().field(MongoGroup.MEMBERS).hasAllOf(users).first() != null;
    }
}
