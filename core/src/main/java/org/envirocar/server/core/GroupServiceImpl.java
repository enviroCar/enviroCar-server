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
package org.envirocar.server.core;

import org.envirocar.server.core.dao.GroupDao;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Groups;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.core.entities.Users;
import org.envirocar.server.core.event.ChangedGroupEvent;
import org.envirocar.server.core.event.CreatedGroupEvent;
import org.envirocar.server.core.event.DeletedGroupEvent;
import org.envirocar.server.core.event.JoinedGroupEvent;
import org.envirocar.server.core.event.LeftGroupEvent;
import org.envirocar.server.core.exception.GroupNotFoundException;
import org.envirocar.server.core.exception.IllegalModificationException;
import org.envirocar.server.core.exception.ResourceAlreadyExistException;
import org.envirocar.server.core.exception.UserNotFoundException;
import org.envirocar.server.core.exception.ValidationException;
import org.envirocar.server.core.update.EntityUpdater;
import org.envirocar.server.core.util.Pagination;
import org.envirocar.server.core.validation.EntityValidator;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Jan Wirwahn <jan.wirwahn@wwu.de>
 */
public class GroupServiceImpl implements GroupService {
    private final EventBus eventBus;
    private final GroupDao groupDao;
    private final EntityValidator<Group> groupValidator;
    private final EntityUpdater<Group> groupUpdater;

    @Inject
    public GroupServiceImpl(EventBus eventBus, GroupDao groupDao,
                            EntityValidator<Group> groupValidator,
                            EntityUpdater<Group> groupUpdater) {
        this.eventBus = eventBus;
        this.groupDao = groupDao;
        this.groupValidator = groupValidator;
        this.groupUpdater = groupUpdater;
    }

    @Override
    public Group getGroup(String name) throws GroupNotFoundException {
        Group group = this.groupDao.getByName(name);
        if (group == null) {
            throw new GroupNotFoundException(name);
        }
        return group;
    }

    @Override
    public Groups getGroups(Pagination p) {
        return this.groupDao.get(p);
    }

    @Override
    public Users getGroupMembers(Group group, Pagination pagination) {
        return this.groupDao.getMembers(group, pagination);
    }

    @Override
    public Group modifyGroup(Group group, Group changes)
            throws ValidationException, IllegalModificationException {
        this.groupValidator.validateUpdate(group);
        this.groupUpdater.update(changes, group);
        this.groupDao.save(group);
        this.eventBus.post(new ChangedGroupEvent(group, group.getOwner()));
        return group;
    }

    @Override
    public void deleteGroup(Group group) throws GroupNotFoundException {
        this.groupDao.delete(group);
        this.eventBus.post(new DeletedGroupEvent(group, group.getOwner()));
    }

    @Override
    public Groups searchGroups(String search, Pagination p) {
        return this.groupDao.search(search, p);
    }

    @Override
    public Group createGroup(User user, Group group) throws
            ResourceAlreadyExistException {
        group.setOwner(user);
        this.groupValidator.validateCreate(group);
        if (groupDao.getByName(group.getName()) != null) {
            throw new ResourceAlreadyExistException();
        }
        this.groupDao.save(group);
        addGroupMember(group, user);
        this.eventBus.post(new CreatedGroupEvent(group, group.getOwner()));
        return group;
    }

    @Override
    public void addGroupMember(Group group, User user) {
        this.groupDao.addMember(group, user);
        this.eventBus.post(new JoinedGroupEvent(group, user));
    }

    @Override
    public void removeGroupMember(Group group, User user)
            throws UserNotFoundException, GroupNotFoundException {
        this.groupDao.removeMember(group, user);
        this.eventBus.post(new LeftGroupEvent(group, user));
    }

    @Override
    public Groups getGroups(User user, Pagination p) {
        return this.groupDao.getByMember(user, p);
    }

    @Override
    public boolean isGroupMember(Group group, User user) {
        return this.groupDao.getMember(group, user.getName()) != null;
    }

    @Override
    public boolean shareGroup(User user, User user0) {
        return this.groupDao.shareGroup(user, user0);
    }

    @Override
    public Group getGroup(User user, String groupName) throws
            GroupNotFoundException {
        Group g = this.groupDao.get(user, groupName);
        if (g == null) {
            throw new GroupNotFoundException(groupName);
        }
        return g;
    }

    @Override
    public User getGroupMember(Group group, String username) throws
            UserNotFoundException {
        User u = this.groupDao.getMember(group, username);
        if (u == null) {
            throw new UserNotFoundException(username);
        }
        return u;
    }
}
