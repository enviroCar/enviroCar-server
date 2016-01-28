package org.envirocar.server.rest.rights;

import org.envirocar.server.core.FriendService;
import org.envirocar.server.core.GroupService;
import org.envirocar.server.core.entities.Fueling;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.Phenomenon;
import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.User;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ReadOnlyRights extends AccessRightsImpl {
    public ReadOnlyRights() {
    }

    public ReadOnlyRights(User user,
                          GroupService groupService,
                          FriendService friendService) {
        super(user, groupService, friendService);
    }
    @Override
    public boolean canDelete(Fueling f) {
        return false;
    }

    @Override
    public boolean canDelete(Group group) {
        return false;
    }

    @Override
    public boolean canDelete(Measurement measurement) {
        return false;
    }

    @Override
    public boolean canDelete(Phenomenon phenomenon) {
        return false;
    }

    @Override
    public boolean canDelete(Sensor sensor) {
        return false;
    }

    @Override
    public boolean canDelete(Track track) {
        return false;
    }

    @Override
    public boolean canDelete(User user) {
        return false;
    }

    @Override
    public boolean canModify(Group group) {
        return false;
    }

    @Override
    public boolean canModify(Measurement measurement) {
        return false;
    }

    @Override
    public boolean canModify(Phenomenon phenomenon) {
        return false;
    }

    @Override
    public boolean canModify(Sensor sensor) {
        return false;
    }

    @Override
    public boolean canModify(Track track) {
        return false;
    }

    @Override
    public boolean canModify(User user) {
        return false;
    }

    @Override
    public boolean canFriend(User user, User friend) {
        return false;
    }

    @Override
    public boolean canJoinGroup(Group group) {
        return false;
    }

    @Override
    public boolean canLeaveGroup(Group group) {
        return false;
    }

    @Override
    public boolean canUnfriend(User user, User friend) {
        return false;
    }

}
