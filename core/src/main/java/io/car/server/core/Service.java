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

import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.car.server.core.activities.Activities;
import io.car.server.core.activities.ActivityFactory;
import io.car.server.core.activities.ActivityType;
import io.car.server.core.activities.TrackActivity;
import io.car.server.core.dao.ActivityDao;
import io.car.server.core.dao.GroupDao;
import io.car.server.core.dao.MeasurementDao;
import io.car.server.core.dao.PhenomenonDao;
import io.car.server.core.dao.SensorDao;
import io.car.server.core.dao.TrackDao;
import io.car.server.core.dao.UserDao;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.Groups;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.Measurements;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Phenomenons;
import io.car.server.core.entities.PropertyFilter;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Sensors;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.core.event.EventBus;
import io.car.server.core.exception.GroupNotFoundException;
import io.car.server.core.exception.IllegalModificationException;
import io.car.server.core.exception.MeasurementNotFoundException;
import io.car.server.core.exception.PhenomenonNotFoundException;
import io.car.server.core.exception.ResourceAlreadyExistException;
import io.car.server.core.exception.SensorNotFoundException;
import io.car.server.core.exception.TrackNotFoundException;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.core.exception.ValidationException;
import io.car.server.core.update.EntityUpdater;
import io.car.server.core.util.Pagination;
import io.car.server.core.util.PasswordEncoder;
import io.car.server.core.validation.EntityValidator;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Jan Wirwahn <jan.wirwahn@wwu.de>
 */
@Singleton
public class Service {
    @Inject
    private UserDao userDao;
    @Inject
    private GroupDao groupDao;
    @Inject
    private TrackDao trackDao;
    @Inject
    private MeasurementDao measurementDao;
    @Inject
    private SensorDao sensorDao;
    @Inject
    private PhenomenonDao phenomenonDao;
    @Inject
    private ActivityDao activityDao;
    @Inject
    private ActivityFactory activityFactory;
    @Inject
    private EntityValidator<User> userValidator;
    @Inject
    private EntityValidator<Group> groupValidator;
    @Inject
    private EntityValidator<Track> trackValidator;
    @Inject
    private EntityUpdater<Track> trackUpdater;
    @Inject
    private EntityUpdater<Group> groupUpdater;
    @Inject
    private EntityUpdater<User> userUpdater;
    @Inject
    private EntityUpdater<Measurement> measurementUpdater;
    @Inject
    private EntityValidator<Measurement> measurementValidator;
    @Inject
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) throws ValidationException,
                                             ResourceAlreadyExistException {
        userValidator.validateCreate(user);
        if (userDao.getByName(user.getName()) != null) {
            throw new ResourceAlreadyExistException();
        }
        if (userDao.getByMail(user.getMail()) != null) {
            throw new ResourceAlreadyExistException();
        }
        user.setToken(passwordEncoder.encode(user.getToken()));
        return this.userDao.create(user);
    }

    public User getUser(String name) throws UserNotFoundException {
        User user = this.userDao.getByName(name);
        if (user == null) {
            throw new UserNotFoundException(name);
        }
        return user;
    }

    public Users getUsers(Pagination p) {
        return this.userDao.get(p);
    }

    public User modifyUser(User user, User changes) throws UserNotFoundException,
                                                           IllegalModificationException,
                                                           ValidationException,
                                                           ResourceAlreadyExistException {
        this.userValidator.validateUpdate(changes);
        if (changes.hasMail() && !changes.getMail().equals(user.getMail())) {
            if (this.userDao.getByMail(changes.getMail()) != null) {
                throw new ResourceAlreadyExistException();
            }
        }
        this.userUpdater.update(changes, user);
        this.userDao.save(user);
        this.activityDao.save(activityFactory
                .createActivity(ActivityType.CHANGED_PROFILE, user));
        return user;
    }

    public void deleteUser(User user) {
        this.userDao.delete(user);
    }

    public void removeFriend(User user, User friend)
            throws UserNotFoundException {
        this.userDao.removeFriend(user, friend);
        this.activityDao.save(activityFactory
                .createUserActivity(ActivityType.UNFRIENDED_USER, user, friend));
    }

    public void addFriend(User user, User friend) throws UserNotFoundException {
        this.userDao.addFriend(user, friend);
        this.activityDao.save(activityFactory
                .createUserActivity(ActivityType.FRIENDED_USER, user, friend));
    }

    public Group getGroup(String name) throws GroupNotFoundException {
        Group group = this.groupDao.getByName(name);
        if (group == null) {
            throw new GroupNotFoundException(name);
        }
        return group;
    }

    public Groups getGroups(Pagination p) {
        return this.groupDao.get(p);
    }

    public Users getGroupMembers(Group group, Pagination pagination) {
        return this.groupDao.getMembers(group, pagination);
    }

    public Group modifyGroup(Group group, Group changes)
            throws ValidationException, IllegalModificationException {
        groupValidator.validateUpdate(group);
        this.activityDao.save(activityFactory
                .createGroupActivity(ActivityType.CHANGED_GROUP, group
                .getOwner(), group));
        return this.groupDao.save(this.groupUpdater.update(changes, group));
    }

    public Track modifyTrack(Track track, Track changes)
            throws ValidationException, IllegalModificationException {
        trackValidator.validateCreate(track);
        return this.trackDao.save(this.trackUpdater.update(changes, track));
    }

    public void deleteGroup(Group group) throws GroupNotFoundException {
        this.activityDao.save(activityFactory
                .createGroupActivity(ActivityType.DELETED_GROUP, group
                .getOwner(), group));
        this.groupDao.delete(group);
    }

    public Groups searchGroups(String search, Pagination p) {
        return this.groupDao.search(search, p);
    }

    public Group createGroup(User user, Group group) throws
            ResourceAlreadyExistException {
        group.setOwner(user);
        groupValidator.validateCreate(group);
        if (groupDao.getByName(group.getName()) != null) {
            throw new ResourceAlreadyExistException();
        }
        this.groupDao.save(group);
        addGroupMember(group, user);
        this.activityDao.save(activityFactory
                .createGroupActivity(ActivityType.CREATED_GROUP, user, group));
        return group;
    }

    public void addGroupMember(Group group, User user) {
        this.groupDao.addMember(group, user);
        this.activityDao.save(activityFactory
                .createGroupActivity(ActivityType.JOINED_GROUP, user, group));
    }

    public void removeGroupMember(Group group, User user)
            throws UserNotFoundException, GroupNotFoundException {
        this.groupDao.removeMember(group, user);
        this.activityDao.save(activityFactory
                .createGroupActivity(ActivityType.LEFT_GROUP, user, group));
    }

    public Tracks getTracks(Pagination p) {
        return trackDao.get(p);
    }

    public Tracks getTracks(User user, Pagination p) {
        return this.trackDao.getByUser(user, p);
    }

    public Track getTrack(String id) throws TrackNotFoundException {
        Track track = trackDao.getById(id);
        if (track == null) {
            throw new TrackNotFoundException(id);
        }
        return track;
    }

    public Track createTrack(Track track) throws ValidationException {
        this.trackDao.create(this.trackValidator.validateCreate(track));
        TrackActivity ac = activityFactory
                .createTrackActivity(ActivityType.CREATED_TRACK, track.getUser(), track);
        this.activityDao.save(ac);
        EventBus.getInstance().pushActivity(ac);
        return track;
    }

    public void deleteTrack(Track track) {
        this.trackDao.delete(track);
    }

    public Measurement createMeasurement(Measurement measurement) {
        this.measurementValidator.validateCreate(measurement);
        this.measurementDao.create(measurement);
        return measurement;
    }

    public Measurement createMeasurement(Track track, Measurement measurement) {
        this.measurementValidator.validateCreate(measurement);
        measurement.setTrack(track);
        this.measurementDao.create(measurement);
        this.trackDao.update(track);
        return measurement;
    }

    public Measurements getMeasurements(Pagination p) {
        return this.measurementDao.get(p);
    }

    public Measurements getMeasurements(User user, Pagination p) {
        return this.measurementDao.getByUser(user, p);
    }

    public Measurements getMeasurementsByUser(User user, Pagination p) {
        return this.measurementDao.getByUser(user, p);
    }

    public Measurements getMeasurementsByTrack(Track track, Pagination p) {
        return this.measurementDao.getByTrack(track, p);
    }

    public Measurement getMeasurement(String id) throws
            MeasurementNotFoundException {
        Measurement m = this.measurementDao.getById(id);
        if (m == null) {
            throw new MeasurementNotFoundException(id);
        }
        return m;
    }

    public Measurement modifyMeasurement(Measurement measurement,
                                         Measurement changes)
            throws ValidationException, IllegalModificationException {
        measurementValidator.validateCreate(measurement);
        return this.measurementDao.save(this.measurementUpdater.update(changes,
                                                                       measurement));
    }

    public void deleteMeasurement(Measurement measurement) {
        this.measurementDao.delete(measurement);
    }

    public Phenomenon getPhenomenonByName(String name)
            throws PhenomenonNotFoundException {
        Phenomenon p = this.phenomenonDao.getByName(name);
        if (p == null) {
            throw new PhenomenonNotFoundException(name);
        }
        return p;
    }

    public Phenomenon createPhenomenon(Phenomenon phenomenon) {
        return this.phenomenonDao.create(phenomenon);
    }

    public Phenomenons getPhenomenons(Pagination p) {
        return this.phenomenonDao.get(p);
    }

    public Sensor getSensorByName(String id) throws SensorNotFoundException {
        Sensor s = this.sensorDao.getByIdentifier(id);
        if (s == null) {
            throw new SensorNotFoundException(id);
        }
        return s;
    }

    public Sensors getSensors(Set<PropertyFilter> filters, Pagination p) {
        return this.sensorDao.get(filters, p);
    }

    public Sensor createSensor(Sensor sensor) {
        return this.sensorDao.create(sensor);
    }

    public Group getGroup(User user, String groupName) throws
            GroupNotFoundException {
        Group g = this.groupDao.get(user, groupName);
        if (g == null) {
            throw new GroupNotFoundException(groupName);
        }
        return g;
    }

    public User getGroupMember(Group group, String username) throws
            UserNotFoundException {
        User u = this.groupDao.getMember(group, username);
        if (u == null) {
            throw new UserNotFoundException(username);
        }
        return u;
    }

    public Users getFriends(User user) {
        return this.userDao.getFriends(user);
    }

    public User getFriend(User user, String friendName) throws
            UserNotFoundException {
        User f = this.userDao.getFriend(user, friendName);
        if (f == null) {
            throw new UserNotFoundException(friendName);
        }
        return f;
    }

    public Groups getGroups(User user, Pagination p) {
        return this.groupDao.getByMember(user, p);
    }

    public Activities getActivities(Pagination p) {
        return activityDao.get(p);
    }

    public Activities getActivities(User user, Pagination p) {
        return activityDao.get(user, p);
    }

    public Activities getFriendActivities(User user, Pagination p) {
        return activityDao.getForFriends(user, p);
    }

    public Activities getActivities(ActivityType type, Pagination p) {
        return activityDao.get(type, p);
    }

    public Activities getActivities(ActivityType type, User user, Pagination p) {
        return activityDao.get(type, user, p);
    }

    public Activities getActivities(Group user, Pagination p) {
        return activityDao.get(user, p);
    }

    public Activities getActivities(ActivityType type, Group user, Pagination p) {
        return activityDao.get(type, user, p);
    }

    public Sensors getSensorsByType(String type, Set<PropertyFilter> filters,
                                    Pagination p) {
        return this.sensorDao.getByType(type, filters, p);
    }
}
