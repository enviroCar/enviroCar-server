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
import org.envirocar.server.core.activities.UserActivity;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.mongo.entity.MongoUser;
import org.envirocar.server.mongo.util.Ref;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoUserActivity extends MongoActivity implements UserActivity {
    public static final String OTHER = "other";
    private MorphiaReference<MongoUser> other;

    @AssistedInject
    public MongoUserActivity(@Assisted ActivityType type,
                             @Assisted("user") User user,
                             @Assisted("other") User other) {
        super(user, type);
        this.other = Ref.wrap(other);
    }

    @Inject
    public MongoUserActivity() {
        this(null, null, null);
    }

    @Override
    public MongoUser getOther() {
        return Ref.unwrap(other);
    }

    @Override
    public void setOther(User other) {
        this.other = Ref.wrap(other);
    }

    @Override
    public boolean hasOther() {
        return getOther() != null;
    }

    @Override
    public String toString() {
        return toStringHelper().add(OTHER, other).toString();
    }
}
