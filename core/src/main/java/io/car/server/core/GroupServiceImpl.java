/*
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.core;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import io.car.server.core.dao.GroupDao;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.core.event.ChangedGroupEvent;
import io.car.server.core.event.CreatedGroupEvent;
import io.car.server.core.event.DeletedGroupEvent;
import io.car.server.core.event.JoinedGroupEvent;
import io.car.server.core.event.LeftGroupEvent;
import io.car.server.core.exception.GroupNotFoundException;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.core.update.EntityUpdater;
import io.car.server.core.util.Pagination;
import io.car.server.core.validation.EntityValidator;

/**
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
