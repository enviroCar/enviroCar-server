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

import io.car.server.core.activities.Activities;
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
import io.car.server.core.filter.ActivityFilter;
import io.car.server.core.filter.MeasurementFilter;
import io.car.server.core.filter.SensorFilter;
import io.car.server.core.filter.TrackFilter;
import io.car.server.core.util.Pagination;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public interface Service {
    void addFriend(User user, User friend) throws
            UserNotFoundException;

    void addGroupMember(Group group, User user);

    Group createGroup(User user, Group group) throws
            ResourceAlreadyExistException;

    Measurement createMeasurement(Measurement measurement);

    Measurement createMeasurement(Track track, Measurement measurement);

    Phenomenon createPhenomenon(Phenomenon phenomenon);

    Sensor createSensor(Sensor sensor);

    Track createTrack(Track track) throws
            ValidationException;

    User createUser(User user) throws
            ValidationException,
            ResourceAlreadyExistException;

    void deleteGroup(Group group) throws
            GroupNotFoundException;

    void deleteMeasurement(Measurement measurement);

    void deleteTrack(Track track);

    void deleteUser(User user);

    Activities getActivities(ActivityFilter request);

    User getFriend(User user, String friendName) throws
            UserNotFoundException;

    Users getFriends(User user);

    Group getGroup(String name) throws
            GroupNotFoundException;

    Group getGroup(User user, String groupName) throws
            GroupNotFoundException;

    User getGroupMember(Group group, String username) throws
            UserNotFoundException;

    Users getGroupMembers(Group group, Pagination pagination);

    Groups getGroups(Pagination p);

    Groups getGroups(User user, Pagination p);

    Measurement getMeasurement(String id) throws
            MeasurementNotFoundException;

    Phenomenon getPhenomenonByName(String name) throws
            PhenomenonNotFoundException;

    Phenomenons getPhenomenons(Pagination p);

    Sensor getSensorByName(String id) throws
            SensorNotFoundException;

    Track getTrack(String id) throws
            TrackNotFoundException;

    User getUser(String name) throws
            UserNotFoundException;

    Users getUsers(Pagination p);

    boolean isFriend(User user1, User user2);

    boolean isGroupMember(Group group, User user);

    Group modifyGroup(Group group, Group changes) throws
            ValidationException,
            IllegalModificationException;

    Measurement modifyMeasurement(Measurement measurement, Measurement changes)
            throws ValidationException,
                   IllegalModificationException;

    Track modifyTrack(Track track, Track changes) throws
            ValidationException,
            IllegalModificationException;

    User modifyUser(User user, User changes) throws
            UserNotFoundException,
            IllegalModificationException,
            ValidationException,
            ResourceAlreadyExistException;

    void removeFriend(User user, User friend) throws
            UserNotFoundException;

    void removeGroupMember(Group group, User user) throws
            UserNotFoundException,
            GroupNotFoundException;

    Groups searchGroups(String search, Pagination p);

    boolean shareGroup(User user, User user0);

    Measurements getMeasurements(MeasurementFilter request);

    Tracks getTracks(TrackFilter request);

    Sensors getSensors(SensorFilter request);
}
