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

import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Polymorphic;
import com.github.jmkgreen.morphia.annotations.Property;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.activities.Activity;
import io.car.server.core.activities.ActivityType;
import io.car.server.core.entities.User;
import io.car.server.core.entities.UserBase;
import io.car.server.mongo.entity.MongoEntity;
import io.car.server.mongo.entity.MongoUser;
import io.car.server.mongo.entity.MongoUserBase;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Polymorphic
@Entity("activities")
public class MongoActivity extends MongoEntity implements Activity {
    @Indexed
    @Property(TIME)
    private DateTime time = new DateTime();
    @Indexed
    @Embedded(USER)
    private MongoUserBase user;
    @Indexed
    @Property(TYPE)
    private ActivityType type;

    @Inject
    public MongoActivity(@Assisted User user,
                         @Assisted ActivityType type) {
        this.user = (MongoUser) user;
        this.type = type;
    }

    public MongoActivity() {
        this(null, null);
    }

    @Override
    public MongoUserBase getUser() {
        return this.user;
    }

    @Override
    public void setUser(UserBase user) {
        this.user = (MongoUserBase) user;
    }

    @Override
    public ActivityType getType() {
        return this.type;
    }

    @Override
    public void setType(ActivityType type) {
        this.type = type;
    }

    @Override
    public DateTime getTime() {
        return this.time;
    }
}
