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

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vividsolutions.jts.geom.Polygon;

import io.car.server.core.activities.Activities;
import io.car.server.core.activities.ActivityType;
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
import io.car.server.core.event.ChangedGroupEvent;
import io.car.server.core.event.ChangedMeasurementEvent;
import io.car.server.core.event.ChangedProfileEvent;
import io.car.server.core.event.ChangedTrackEvent;
import io.car.server.core.event.CreatedGroupEvent;
import io.car.server.core.event.CreatedMeasurementEvent;
import io.car.server.core.event.CreatedPhenomenonEvent;
import io.car.server.core.event.CreatedSensorEvent;
import io.car.server.core.event.CreatedTrackEvent;
import io.car.server.core.event.DeletedGroupEvent;
import io.car.server.core.event.DeletedMeasurementEvent;
import io.car.server.core.event.DeletedTrackEvent;
import io.car.server.core.event.DeletedUserEvent;
import io.car.server.core.event.FriendedUserEvent;
import io.car.server.core.event.JoinedGroupEvent;
import io.car.server.core.event.LeftGroupEvent;
import io.car.server.core.event.UnfriendedUserEvent;
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
public class ServiceImpl implements Service {
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
    @Inject
    private EventBus eventBus;

    @Override
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

    @Override
    public User getUser(String name) throws UserNotFoundException {
        User user = this.userDao.getByName(name);
        if (user == null) {
            throw new UserNotFoundException(name);
        }
        return user;
    }

    @Override
    public Users getUsers(Pagination p) {
        return this.userDao.get(p);
    }

    @Override
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
        this.eventBus.post(new ChangedProfileEvent(user));
        return user;
    }

    @Override
    public void deleteUser(User user) {
        this.userDao.delete(user);
        this.eventBus.post(new DeletedUserEvent(user));
    }

    @Override
    public void removeFriend(User user, User friend)
            throws UserNotFoundException {
        this.userDao.removeFriend(user, friend);
        this.eventBus.post(new UnfriendedUserEvent(user, friend));
    }

    @Override
    public void addFriend(User user, User friend) throws UserNotFoundException {
        this.userDao.addFriend(user, friend);
        this.eventBus.post(new FriendedUserEvent(user, friend));
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
    public Track modifyTrack(Track track, Track changes)
            throws ValidationException, IllegalModificationException {
        this.trackValidator.validateCreate(track);
        this.trackUpdater.update(changes, track);
        this.trackDao.save(track);
        this.eventBus.post(new ChangedTrackEvent(track.getUser(), track));
        return track;
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
    public Tracks getTracks(Pagination p) {
        return this.trackDao.get(p);
    }

    @Override
    public Tracks getTracks(User user, Pagination p) {
        return this.trackDao.getByUser(user, p);
    }

    @Override
    public Track getTrack(String id) throws TrackNotFoundException {
        Track track = trackDao.getById(id);
        if (track == null) {
            throw new TrackNotFoundException(id);
        }
        return track;
    }

    @Override
    public Track createTrack(Track track) throws ValidationException {
        this.trackValidator.validateCreate(track);
        this.trackDao.create(track);
        this.eventBus.post(new CreatedTrackEvent(track.getUser(), track));
        return track;
    }

    @Override
    public void deleteTrack(Track track) {
        this.trackDao.delete(track);
        this.eventBus.post(new DeletedTrackEvent(track, track.getUser()));
    }

    @Override
    public Measurement createMeasurement(Measurement m) {
        this.measurementValidator.validateCreate(m);
        this.measurementDao.create(m);
        this.eventBus.post(new CreatedMeasurementEvent(m.getUser(), m));
        return m;
    }

    @Override
    public Measurement createMeasurement(Track track, Measurement m) {
        this.measurementValidator.validateCreate(m);
        m.setTrack(track);
        this.measurementDao.create(m);
        this.trackDao.update(track);
        this.eventBus.post(new CreatedMeasurementEvent(m.getUser(), m));
        return m;
    }

    @Override
    public Measurements getMeasurements(Pagination p) {
        return this.measurementDao.get(p);
    }

    @Override
    public Measurements getMeasurements(User user, Pagination p) {
        return this.measurementDao.getByUser(user, p);
    }

    @Override
    public Measurements getMeasurementsByUser(User user, Pagination p) {
        return this.measurementDao.getByUser(user, p);
    }

    @Override
    public Measurements getMeasurementsByTrack(Track track, Pagination p) {
        return this.measurementDao.getByTrack(track, p);
    }

    @Override
    public Measurement getMeasurement(String id) throws
            MeasurementNotFoundException {
        Measurement m = this.measurementDao.getById(id);
        if (m == null) {
            throw new MeasurementNotFoundException(id);
        }
        return m;
    }

    @Override
    public Measurement modifyMeasurement(Measurement m,
                                         Measurement changes)
            throws ValidationException, IllegalModificationException {
        this.measurementValidator.validateCreate(m);
        this.measurementUpdater.update(changes, m);
        this.measurementDao.save(m);
        this.eventBus.post(new ChangedMeasurementEvent(m, m.getUser()));
        return m;
    }

    public void deleteMeasurement(Measurement m) {
        this.measurementDao.delete(m);
        this.eventBus.post(new DeletedMeasurementEvent(m, m.getUser()));
    }

    @Override
    public Phenomenon getPhenomenonByName(String name)
            throws PhenomenonNotFoundException {
        Phenomenon p = this.phenomenonDao.getByName(name);
        if (p == null) {
            throw new PhenomenonNotFoundException(name);
        }
        return p;
    }

    @Override
    public Phenomenon createPhenomenon(Phenomenon phenomenon) {
        this.phenomenonDao.create(phenomenon);
        this.eventBus.post(new CreatedPhenomenonEvent(phenomenon));
        return phenomenon;
    }

    @Override
    public Phenomenons getPhenomenons(Pagination p) {
        return this.phenomenonDao.get(p);
    }

    @Override
    public Sensor getSensorByName(String id) throws SensorNotFoundException {
        Sensor s = this.sensorDao.getByIdentifier(id);
        if (s == null) {
            throw new SensorNotFoundException(id);
        }
        return s;
    }

    @Override
    public Sensors getSensors(Set<PropertyFilter> filters, Pagination p) {
        return this.sensorDao.get(filters, p);
    }

    @Override
    public Sensor createSensor(Sensor sensor) {
        this.sensorDao.create(sensor);
        this.eventBus.post(new CreatedSensorEvent(sensor));
        return sensor;
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

    @Override
    public Users getFriends(User user) {
        return this.userDao.getFriends(user);
    }

    @Override
    public User getFriend(User user, String friendName) throws
            UserNotFoundException {
        User f = this.userDao.getFriend(user, friendName);
        if (f == null) {
            throw new UserNotFoundException(friendName);
        }
        return f;
    }

    @Override
    public Groups getGroups(User user, Pagination p) {
        return this.groupDao.getByMember(user, p);
    }

    @Override
    public Activities getActivities(Pagination p) {
        return activityDao.get(p);
    }

    @Override
    public Activities getActivities(User user, Pagination p) {
        return activityDao.get(user, p);
    }

    @Override
    public Activities getFriendActivities(User user, Pagination p) {
        return activityDao.getForFriends(user, p);
    }

    @Override
    public Activities getActivities(ActivityType type, Pagination p) {
        return activityDao.get(type, p);
    }

    @Override
    public Activities getActivities(ActivityType type, User user, Pagination p) {
        return activityDao.get(type, user, p);
    }

    @Override
    public Activities getActivities(Group user, Pagination p) {
        return activityDao.get(user, p);
    }

    @Override
    public Activities getActivities(ActivityType type, Group user, Pagination p) {
        return activityDao.get(type, user, p);
    }

    @Override
    public Sensors getSensorsByType(String type, Set<PropertyFilter> filters,
                                    Pagination p) {
        return this.sensorDao.getByType(type, filters, p);
    }

    @Override
    public boolean isGroupMember(Group group, User user) {
        return this.groupDao.getMember(group, user.getName()) != null;
    }

    @Override
    public boolean isFriend(User user1, User user2) {
        return this.userDao.getFriend(user1, user2.getName()) != null;
    }

    @Override
    public boolean shareGroup(User user, User user0) {
        return this.groupDao.shareGroup(user, user0);
    }

    @Override
    public Measurements getMeasurementsByBbox(Double minx, Double miny,
            Double maxx, Double maxy, Pagination p) {
        return this.measurementDao.getByBbox(minx, miny, maxx, maxy, p);
    }

    @Override
    public Tracks getTracksByBbox(Polygon bbox, Pagination p) {
        return this.trackDao.getByBbox(bbox, p);
    }
}
