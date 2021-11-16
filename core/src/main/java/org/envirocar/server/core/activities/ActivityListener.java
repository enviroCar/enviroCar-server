/*
 * Copyright (C) 2013-2021 The enviroCar project
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
package org.envirocar.server.core.activities;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.envirocar.server.core.dao.ActivityDao;
import org.envirocar.server.core.event.ChangedGroupEvent;
import org.envirocar.server.core.event.ChangedProfileEvent;
import org.envirocar.server.core.event.CreatedGroupEvent;
import org.envirocar.server.core.event.CreatedTrackEvent;
import org.envirocar.server.core.event.DeletedGroupEvent;
import org.envirocar.server.core.event.FriendedUserEvent;
import org.envirocar.server.core.event.JoinedGroupEvent;
import org.envirocar.server.core.event.LeftGroupEvent;
import org.envirocar.server.core.event.UnfriendedUserEvent;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class ActivityListener {
    private final ActivityDao dao;
    private final ActivityFactory fac;

    @Inject
    public ActivityListener(ActivityDao dao, ActivityFactory factory) {
        this.dao = dao;
        this.fac = factory;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onChangedGroupEvent(ChangedGroupEvent e) {
        this.dao.save(this.fac.createGroupActivity(ActivityType.CHANGED_GROUP, e.getUser(), e.getGroup()));
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onChangedProfileEvent(ChangedProfileEvent e) {
        this.dao.save(this.fac.createActivity(ActivityType.CHANGED_PROFILE, e.getUser()));
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onCreatedGroupEvent(CreatedGroupEvent e) {
        this.dao.save(this.fac.createGroupActivity(ActivityType.CREATED_GROUP, e.getUser(), e.getGroup()));
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onCreatedTrackEvent(CreatedTrackEvent e) {
        this.dao.save(this.fac.createTrackActivity(ActivityType.CREATED_TRACK, e.getUser(), e.getTrack()));
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onDeletedGroupEvent(DeletedGroupEvent e) {
        this.dao.save(this.fac.createGroupActivity(ActivityType.DELETED_GROUP, e.getUser(), e.getGroup()));
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onFriendedUserEvent(FriendedUserEvent e) {
        this.dao.save(this.fac.createUserActivity(ActivityType.FRIENDED_USER, e.getUser(), e.getOther()));
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onJoinedGroupEvent(JoinedGroupEvent e) {
        this.dao.save(this.fac.createGroupActivity(ActivityType.JOINED_GROUP, e.getUser(), e.getGroup()));
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onLeftGroupEvent(LeftGroupEvent e) {
        this.dao.save(this.fac.createGroupActivity(ActivityType.LEFT_GROUP, e.getUser(), e.getGroup()));
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onUnfriendedUserEvent(UnfriendedUserEvent e) {
        this.dao.save(this.fac.createUserActivity(ActivityType.UNFRIENDED_USER, e.getUser(), e.getOther()));
    }
}
