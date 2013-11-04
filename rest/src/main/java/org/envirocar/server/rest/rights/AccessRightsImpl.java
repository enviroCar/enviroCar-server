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
package org.envirocar.server.rest.rights;

import org.envirocar.server.core.FriendService;
import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class AccessRightsImpl extends AbstractAccessRights {
    public AccessRightsImpl() {
        super();
    }

    public AccessRightsImpl(User user, GroupService groupService,
                            FriendService friendService) {
        super(user, groupService, friendService);
    }

    @Override
    public boolean canSeeTracksOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeMeasurementsOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeFriendsOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeGroupsOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canModify(User user) {
        return isSelf(user);
    }

    @Override
    public boolean canModify(Track track) {
        return isSelf(track.getUser());
    }

    @Override
    public boolean canModify(Measurement measurement) {
        return isSelf(measurement.getUser());
    }

    @Override
    public boolean canModify(Group group) {
        return isSelf(group.getOwner());
    }

    @Override
    public boolean canModify(Sensor sensor) {
        return false;
    }

    @Override
    public boolean canModify(Phenomenon phenomenon) {
        return false;
    }

    @Override
    public boolean canDelete(User user) {
        return isSelf(user);
    }

    @Override
    public boolean canDelete(Track track) {
        return isSelf(track.getUser());
    }

    @Override
    public boolean canDelete(Measurement measurement) {
        return isSelf(measurement.getUser());
    }

    @Override
    public boolean canDelete(Group group) {
        return isSelf(group.getOwner());
    }

    @Override
    public boolean canDelete(Sensor sensor) {
        return false;
    }

    @Override
    public boolean canDelete(Phenomenon phenomenon) {
        return false;
    }

    @Override
    public boolean canSeeUserOf(Track track) {
        return isSelfFriendOfOrShareGroup(track.getUser());
    }

    @Override
    public boolean canSeeUserOf(Measurement measurement) {
        return isSelfFriendOfOrShareGroup(measurement.getUser());
    }

    @Override
    public boolean canSeeActivitiesOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeActivitiesOf(Group group) {
        return isMember(group);
    }

    @Override
    public boolean canJoinGroup(Group group) {
        return isAuthenticated();
    }

    @Override
    public boolean canLeaveGroup(Group group) {
        return isAuthenticated();
    }

    @Override
    public boolean canSeeNameOf(User user) {
        return isAuthenticated();
    }

    @Override
    public boolean canSeeMailOf(User user) {
        return isSelf(user);
    }

    @Override
    public boolean canSeeIsAdminOf(User user) {
        return user.isAdmin();
    }

    @Override
    public boolean canSeeCountryOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeDayOfBirthOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeFirstNameOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeGenderOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeLanguageOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeLastNameOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeLocationOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeUrlOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeAboutMeOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeCreationTimeOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeModificationTimeOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeStatisticsOf(User user) {
        return isSelfFriendOfOrShareGroup(user);
    }

    @Override
    public boolean canSeeFriendActivitiesOf(User user) {
        return isSelf(user);
    }

    @Override
    public boolean canSeeAvatarOf(User user) {
        return isAuthenticated();
    }

    @Override
    public boolean canSeeNameOf(Track track) {
        return isSelfFriendOfOrShareGroup(track.getUser());
    }

    @Override
    public boolean canSeeDescriptionOf(Track track) {
        return isSelfFriendOfOrShareGroup(track.getUser());
    }

    @Override
    public boolean canSeeSensorOf(Track track) {
        return true;
    }

    @Override
    public boolean canSeeMeasurementsOf(Track track) {
        return true;
    }

    @Override
    public boolean canSeeSensorOf(Measurement measurement) {
        return true;
    }

    @Override
    public boolean canSeeTimeOf(Measurement measurement) {
        return true;
    }

    @Override
    public boolean canSeeCreationTimeOf(Measurement measurement) {
        return isSelfFriendOfOrShareGroup(measurement.getUser());
    }

    @Override
    public boolean canSeeModificationTimeOf(Measurement measurement) {
        return isSelfFriendOfOrShareGroup(measurement.getUser());
    }

    @Override
    public boolean canSeeNameOf(Group group) {
        return isAuthenticated();
    }

    @Override
    public boolean canSeeDescriptionOf(Group group) {
        return isAuthenticated();
    }

    @Override
    public boolean canSeeMembersOf(Group group) {
        return isMember(group);
    }

    @Override
    public boolean canSeeCreationTimeOf(Track track) {
        return isSelfFriendOfOrShareGroup(track.getUser());
    }

    @Override
    public boolean canSeeModificationTimeOf(Track track) {
        return isSelfFriendOfOrShareGroup(track.getUser());
    }

    @Override
    public boolean canSeeOwnerOf(Group group) {
        return isAuthenticated();
    }

    @Override
    public boolean canSeeCreationTimeOf(Group group) {
        return isMember(group);
    }

    @Override
    public boolean canSeeModificationTimeOf(Group group) {
        return isMember(group);
    }

    @Override
    public boolean canUnfriend(User user, User friend) {
        return isSelf(user) && !isSelf(friend);
    }

    @Override
    public boolean canFriend(User user, User friend) {
        return isSelf(user) && !isSelf(friend);
    }

    @Override
    public boolean canSeeStatisticsOf(Track track) {
        return true;
    }

    @Override
    public boolean canSeeStatisticsOf(Phenomenon phenomenon) {
        return true;
    }

    @Override
    public boolean canSeeValuesOf(Measurement measurement) {
        return true;
    }

    @Override
    public boolean canSeeGeometryOf(Measurement t) {
        return true;
    }

    @Override
    public boolean canSee(User user) {
        return isAuthenticated();
    }

    @Override
    public boolean canSee(Track track) {
        return true;
    }

    @Override
    public boolean canSee(Measurement measurement) {
        return true;
    }

    @Override
    public boolean canSee(Group group) {
        return isAuthenticated();
    }

    @Override
    public boolean canSee(Sensor sensor) {
        return true;
    }

    @Override
    public boolean canSee(Phenomenon phenomenon) {
        return true;
    }

    @Override
    public boolean canSeeUsers() {
        return isAuthenticated();
    }

    @Override
    public boolean canSeeTracks() {
        return true;
    }

    @Override
    public boolean canSeeMeasurements() {
        return true;
    }

    @Override
    public boolean canSeeGroups() {
        return isAuthenticated();
    }

    @Override
    public boolean canSeeSensors() {
        return true;
    }

    @Override
    public boolean canSeePhenomenons() {
        return true;
    }

    @Override
    public boolean canSeeStatistics() {
        return true;
    }

    @Override
    public boolean canSeeStatisticsOf(Sensor sensor) {
        return true;
    }

    @Override
    public boolean canSeeBadgesOf(User user) {
        return isAuthenticated();
    }

	@Override
	public boolean canSeeTermsOfUse() {
		return true;
	}

	@Override
	public boolean canSee(TermsOfUseInstance t) {
		return true;
	}

    @Override
    public boolean canSeeSchema() {
        return true;
    }

	@Override
	public boolean canSeeAppVersionOf(Track track) {
		return true;
	}

	@Override
	public boolean canSeeObdDeviceOf(Track track) {
		//TODO re-visit regarding privacy
		return true;
	}

	@Override
	public boolean canSeeTouVersionOf(Track track) {
		//TODO re-visit regarding privacy
		return true;
	}

	@Override
	public boolean canSeeAnnouncements() {
		return true;
	}
}
