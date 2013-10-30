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
public class NonRestrictiveRights implements AccessRights {
    @Override
    public boolean isSelf(User user) {
        return true;
    }

    @Override
    public boolean canSeeStatistics() {
        return true;
    }

    @Override
    public boolean canSeeUsers() {
        return true;
    }

    @Override
    public boolean canSee(User user) {
        return true;
    }

    @Override
    public boolean canSeeNameOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeMailOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeIsAdminOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeCountryOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeDayOfBirthOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeFirstNameOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeGenderOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeLanguageOf(User user) {
        return true;
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
        return true;
    }

    @Override
    public boolean canSeeAboutMeOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeCreationTimeOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeModificationTimeOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeAvatarOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeTracksOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeMeasurementsOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeFriendsOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeStatisticsOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeGroupsOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeActivitiesOf(User user) {
        return true;
    }

    @Override
    public boolean canSeeFriendActivitiesOf(User user) {
        return true;
    }

    @Override
    public boolean canModify(User user) {
        return true;
    }

    @Override
    public boolean canDelete(User user) {
        return true;
    }

    @Override
    public boolean canUnfriend(User user, User friend) {
        return true;
    }

    @Override
    public boolean canFriend(User user, User friend) {
        return true;
    }

    @Override
    public boolean canSeeTracks() {
        return true;
    }

    @Override
    public boolean canSee(Track track) {
        return true;
    }

    @Override
    public boolean canSeeUserOf(Track track) {
        return true;
    }

    @Override
    public boolean canSeeNameOf(Track track) {
        return true;
    }

    @Override
    public boolean canSeeDescriptionOf(Track track) {
        return true;
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
    public boolean canSeeCreationTimeOf(Track track) {
        return true;
    }

    @Override
    public boolean canSeeModificationTimeOf(Track track) {
        return true;
    }

    @Override
    public boolean canModify(Track track) {
        return true;
    }

    @Override
    public boolean canDelete(Track track) {
        return true;
    }

    @Override
    public boolean canSeeStatisticsOf(Track track) {
        return true;
    }

    @Override
    public boolean canSeeMeasurements() {
        return true;
    }

    @Override
    public boolean canSee(Measurement measurement) {
        return true;
    }

    @Override
    public boolean canSeeUserOf(Measurement measurement) {
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
        return true;
    }

    @Override
    public boolean canSeeModificationTimeOf(Measurement measurement) {
        return true;
    }

    @Override
    public boolean canModify(Measurement measurement) {
        return true;
    }

    @Override
    public boolean canDelete(Measurement measurement) {
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
    public boolean canSeeGroups() {
        return true;
    }

    @Override
    public boolean canSee(Group group) {
        return true;
    }

    @Override
    public boolean canSeeActivitiesOf(Group group) {
        return true;
    }

    @Override
    public boolean canJoinGroup(Group group) {
        return true;
    }

    @Override
    public boolean canLeaveGroup(Group group) {
        return true;
    }

    @Override
    public boolean canSeeNameOf(Group group) {
        return true;
    }

    @Override
    public boolean canSeeDescriptionOf(Group group) {
        return true;
    }

    @Override
    public boolean canSeeMembersOf(Group group) {
        return true;
    }

    @Override
    public boolean canSeeOwnerOf(Group group) {
        return true;
    }

    @Override
    public boolean canSeeCreationTimeOf(Group group) {
        return true;
    }

    @Override
    public boolean canSeeModificationTimeOf(Group group) {
        return true;
    }

    @Override
    public boolean canModify(Group group) {
        return true;
    }

    @Override
    public boolean canDelete(Group group) {
        return true;
    }

    @Override
    public boolean canSeeSensors() {
        return true;
    }

    @Override
    public boolean canSee(Sensor sensor) {
        return true;
    }

    @Override
    public boolean canModify(Sensor sensor) {
        return true;
    }

    @Override
    public boolean canDelete(Sensor sensor) {
        return true;
    }

    @Override
    public boolean canSeePhenomenons() {
        return true;
    }

    @Override
    public boolean canSee(Phenomenon phenomenon) {
        return true;
    }

    @Override
    public boolean canModify(Phenomenon phenomenon) {
        return true;
    }

    @Override
    public boolean canDelete(Phenomenon phenomenon) {
        return true;
    }

    @Override
    public boolean canSeeStatisticsOf(Phenomenon phenomenon) {
        return true;
    }

    @Override
    public boolean canSeeStatisticsOf(Sensor sensor) {
        return true;
    }

    @Override
    public boolean canSeeBadgesOf(User user) {
        return true;
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
		return true;
	}

	@Override
	public boolean canSeeTouVersionOf(Track track) {
		return true;
	}

	@Override
	public boolean canSeeAnnouncements() {
		return true;
	}
}
