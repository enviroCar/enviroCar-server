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
package org.envirocar.server.rest.resources;

import javax.annotation.Nullable;

import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;

import com.google.inject.assistedinject.Assisted;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 * @author Arne de Wall <a.dewall@52north.org>
 */
public interface ResourceFactory {
    UserResource createUserResource(User user);

    UsersResource createUsersResource();

    FriendsResource createFriendsResource(User user);

    FriendResource createFriendResource(@Assisted("user") User user,
                                        @Assisted("friend") User friend);

    GroupResource createGroupResource(Group group);

    GroupsResource createGroupsResource(User user);

    GroupMemberResource createGroupMemberResource(Group group, User member);

    GroupMembersResource createGroupMembersResource(Group group);

    TrackResource createTrackResource(Track track);

    TracksResource createTracksResource(@Nullable User user);

    MeasurementResource createMeasurementResource(Measurement measurement,
                                                  @Nullable User user,
                                                  @Nullable Track track);

    MeasurementsResource createMeasurementsResource(@Nullable User user,
                                                    @Nullable Track track);

    PhenomenonResource createPhenomenonResource(Phenomenon phenomenon);

    PhenomenonsResource createPhenomenonsResource();

    SensorResource createSensorResource(Sensor sensor);

    SensorsResource createSensorsResource();

    StatisticsResource createStatisticsResource();

    StatisticsResource createStatisticsResource(User user);

    StatisticsResource createStatisticsResource(Track track);

    StatisticsResource createStatisticsResource(Sensor sensor);

    StatisticResource createStatisticResource(Phenomenon phenomenon,
                                              @Nullable User user,
                                              @Nullable Track track,
                                              @Nullable Sensor sensor);

    ActivitiesResource createActivitiesResource();

    ActivitiesResource createActivitiesResource(@Nullable User user);

    ActivitiesResource createActivitiesResource(@Nullable Group group);

    AvatarResource createAvatarResource(User user);

    FriendsActivitiesResource createFriendActivitiesResource(User user);

    TermsOfUseResource createTermsOfUseResource();

    TermsOfUseInstanceResource createTermsOfUseInstanceResource(
            TermsOfUseInstance t);

    JSONSchemaResource createSchemaResource();
    
    AnnouncementsResource createAnnouncementsResource();
}
