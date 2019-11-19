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
package org.envirocar.server.mongo.entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import dev.morphia.mapping.experimental.MorphiaReference;
import org.bson.types.ObjectId;
import org.envirocar.server.core.entities.PasswordReset;
import org.envirocar.server.core.entities.User;
import org.envirocar.server.mongo.util.Ref;
import org.joda.time.DateTime;

@Entity(value = MongoPasswordReset.COLLECTION, noClassnameStored = true)
public class MongoPasswordReset extends MongoEntityBase implements PasswordReset {

    public static final String ID = "_id";
    public static final String EXPIRES = "expires";
    public static final String VERIFICATION_CODE = "code";
    public static final String USER = "user";

    public static final int EXPIRATION_PERIOD_HOURS = 24;
    public static final String COLLECTION = "passwordResetStatus";

    @Id
    private final ObjectId id = new ObjectId();
    @Property(EXPIRES)
    @Indexed(options = @IndexOptions(expireAfterSeconds = EXPIRATION_PERIOD_HOURS * 60 * 60))
    private DateTime expires;
    //@Reference(USER)
    private MorphiaReference<MongoUser> user;
    @Property(VERIFICATION_CODE)
    private String code;

    public MongoPasswordReset() {
        setExpires(new DateTime().plusHours(EXPIRATION_PERIOD_HOURS));
    }

    @Override
    public DateTime getExpires() {
        return expires;
    }

    public void setExpires(DateTime expires) {
        this.expires = expires;
    }

    @Override
    public User getUser() {
        return Ref.unwrap(user);
    }

    public void setUser(User user) {
        this.user = Ref.wrap(user);
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean isExpired() {
        return getExpires().isBefore(new DateTime());
    }

    public ObjectId getId() {
        return id;
    }

}
