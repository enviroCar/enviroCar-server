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
package io.car.server.mongo.activities;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.activities.ActivityType;
import io.car.server.core.activities.GroupActivity;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.GroupBase;
import io.car.server.core.entities.User;
import io.car.server.mongo.entity.MongoGroup;
import io.car.server.mongo.entity.MongoGroupBase;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoGroupActivity extends MongoActivity implements GroupActivity {
    @Embedded(GROUP)
    private MongoGroupBase group;

    @Inject
    public MongoGroupActivity(@Assisted ActivityType type,
                              @Assisted User user,
                              @Assisted Group group) {
        super(user, type);
        this.group = (MongoGroup) group;
    }

    public MongoGroupActivity() {
        this(null, null, null);
    }


    @Override
    public MongoGroupBase getGroup() {
        return this.group;
    }

    @Override
    public void setGroup(GroupBase group) {
        this.group = (MongoGroupBase) group;
    }
}
