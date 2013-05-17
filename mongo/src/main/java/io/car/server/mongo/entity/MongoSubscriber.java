/**
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
package io.car.server.mongo.entity;

import java.util.Set;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.google.common.base.Objects;

import io.car.server.core.subscription.Subscriber;
import io.car.server.core.subscription.Subscription;
import io.car.server.core.subscription.Subscriptions;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Entity("subscribers")
public class MongoSubscriber extends MongoBaseEntity<MongoSubscriber> implements Subscriber {
    public static final String NAME = "name";
    public static final String SECRET = "secret";
    public static final String SUBSCRIPTIONS = "subscriptions";
    @Property(NAME)
    private String name;
    @Property(SECRET)
    private String secret;
    @Reference(SUBSCRIPTIONS)
    private Set<MongoSubscription> subscriptions;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Subscriber setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getSecret() {
        return this.secret;
    }

    @Override
    public Subscriber setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    @Override
    public Subscriptions getSubscriptions() {
        return new Subscriptions(subscriptions);
    }

    @Override
    public Subscriber addSubscription(Subscription subscription) {
        this.subscriptions.add((MongoSubscription) subscription);
        return this;
    }

    @Override
    public Subscriber removeSubscription(Subscription subscription) {
        this.subscriptions.remove((MongoSubscription) subscription);
        return this;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .omitNullValues()
                .add(ID, getId())
                .add(NAME, getName())
                .add(SECRET, getSecret())
                .add(SUBSCRIPTIONS, getSubscriptions())
                .toString();
    }

}
