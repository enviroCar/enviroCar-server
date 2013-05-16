/**
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

import io.car.server.core.util.PasswordEncoder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

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
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Sensors;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.Tracks;
import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
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
import io.car.server.core.validation.EntityValidator;

/**
 * @author Christian Autermann <c.autermann@52north.org>
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

    public Users getUsers(int limit) {
        return this.userDao.get(limit);
    }

    public Users getUsers() {
        return getUsers(0);
    }

    public User modifyUser(User user, User changes)
            throws UserNotFoundException, IllegalModificationException,
                   ValidationException {
        return this.userDao.save(this.userUpdater.update(
                userValidator.validateUpdate(user), user));
    }

    public void deleteUser(String username) throws UserNotFoundException {
        deleteUser(getUser(username));
    }

    public void deleteUser(User user) throws UserNotFoundException {
        this.userDao.delete(user);
    }

    public void removeFriend(User user, User friend)
            throws UserNotFoundException {
        this.userDao.save(user.removeFriend(getUser(friend.getName())));
    }

    public void addFriend(User user, User friend) throws UserNotFoundException {
        this.userDao.save(user.addFriend(getUser(friend.getName())));
    }

    public Group getGroup(String name) throws GroupNotFoundException {
        Group group = this.groupDao.getByName(name);
        if (group == null) {
            throw new GroupNotFoundException(name);
        }
        return group;
    }

    public Groups getGroups(int limit) {
        return this.groupDao.get(limit);
    }

    public Group createGroup(Group group) throws ValidationException,
                                                 ResourceAlreadyExistException {
        groupValidator.validateCreate(group);
        if (groupDao.getByName(group.getName()) != null) {
            throw new ResourceAlreadyExistException();
        }
        return this.groupDao.create(group);
    }

    public Group modifyGroup(Group group, Group changes)
            throws ValidationException, IllegalModificationException {
        groupValidator.validateUpdate(group);
        return this.groupDao.save(this.groupUpdater.update(changes, group));
    }

    public Track modifyTrack(Track track, Track changes)
            throws ValidationException, IllegalModificationException {
        trackValidator.validateCreate(track);
        return this.trackDao.save(this.trackUpdater.update(changes, track));
    }

    public void deleteGroup(String username) throws GroupNotFoundException {
        deleteGroup(getGroup(username));
    }

    public void deleteGroup(Group group) throws GroupNotFoundException {
        this.groupDao.delete(group);
    }

    public Groups getGroupsOfUser(User user, int limit) {
        return this.groupDao.getByMember(user);
    }

    public Groups searchGroups(String search, int limit) {
        return this.groupDao.search(search, limit);
    }

    public void addGroupMember(Group group, User user)
            throws UserNotFoundException {
        this.groupDao.save(group.addMember(getUser(user.getName())));
    }

    public void removeGroupMember(Group group, User user)
            throws UserNotFoundException {
        this.groupDao.save(group.removeMember(getUser(user.getName())));
    }

    public Tracks getTracks() {
        return getTracks(0);
    }

    public Tracks getTracks(int limit) {
        return trackDao.get(limit);
    }

    public Tracks getTracks(User user) {
        return this.trackDao.getByUser(user);
    }

    public Track getTrack(String id) throws TrackNotFoundException {
        Track track = trackDao.getById(id);
        if (track == null) {
            throw new TrackNotFoundException(id);
        }
        return track;
    }

    public Track createTrack(Track track) throws ValidationException {
        return this.trackDao.create(this.trackValidator.validateCreate(track));
    }

    public void deleteTrack(String id) throws TrackNotFoundException {
        this.trackDao.delete(getTrack(id));
    }

    public void deleteTrack(Track track) {
        this.trackDao.delete(track);
    }

    public Measurement createMeasurement(Measurement measurement) {
        return this.measurementDao.create(measurement);
    }

    public Measurement createMeasurement(Track track, Measurement measurement) {
        measurement.setTrack(track);
        measurement = createMeasurement(measurement);
        this.trackDao.save(track.addMeasurement(measurement));
        return measurement;
    }

    public Measurements getMeasurements(int limit) {
        return this.measurementDao.get(limit);
    }

    public Measurements getMeasurements(User user) {
        return this.measurementDao.getByUser(user);
    }

    public Measurement getMeasurement(String id) throws MeasurementNotFoundException {
        Measurement m = this.measurementDao.getById(id);
        if (m == null) {
            throw new MeasurementNotFoundException(id);
        }
        return m;
    }

    public Measurement modifyMeasurement(Measurement measurement,
                                         Measurement changes) throws ValidationException,
                                                                     IllegalModificationException {
        measurementValidator.validateCreate(measurement);
        return this.measurementDao.save(this.measurementUpdater.update(changes,
                                                                       measurement));
    }

    public Track addMeasurement(Track track, Measurement measurement)
            throws ValidationException {
        return trackDao.save(track.addMeasurement(measurementDao
                .save(measurementValidator.validateCreate(measurement))));
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

    public Phenomenons getPhenomenons() {
        return this.phenomenonDao.get();
    }

    public Sensor getSensorByName(String name) throws SensorNotFoundException {
        Sensor s = this.sensorDao.getByName(name);
        if (s == null) {
            throw new SensorNotFoundException(name);
        }
        return s;
    }

    public Sensors getSensors() {
        return this.sensorDao.get();
    }

    public Sensor createSensor(Sensor sensor) {
        return this.sensorDao.create(sensor);
    }
}
