/*
 * Copyright (C) 2013-2019 The enviroCar project
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
package org.envirocar.server.mongo.activities;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import dev.morphia.mapping.experimental.MorphiaReference;
import org.envirocar.server.core.activities.ActivityType;
import org.envirocar.server.core.activities.GroupActivity;
import org.envirocar.server.core.entities.Group;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.mongo.entity.MongoGroup;
import org.envirocar.server.mongo.util.Ref;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoGroupActivity extends MongoActivity implements GroupActivity {
    public static final String GROUP = "group";
    private MorphiaReference<MongoGroup> group;

    @AssistedInject
    public MongoGroupActivity(@Assisted ActivityType type,
                              @Assisted User user,
                              @Assisted Group group) {
        super(user, type);
        this.group = Ref.wrap(group);
    }

    @Inject
    public MongoGroupActivity() {
        this(null, null, null);
    }

    @Override
    public MongoGroup getGroup() {
        return Ref.unwrap(group);
    }

    @Override
    public void setGroup(Group group) {
        this.group = Ref.wrap(group);
    }

    @Override
    public boolean hasGroup() {
        return getGroup() != null;
    }

    @Override
    public String toString() {
        return toStringHelper().add(GROUP, group).toString();
    }
}
