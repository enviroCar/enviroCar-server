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
package io.car.server.mongo;

import java.util.Set;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.google.common.base.Objects;

import io.car.server.core.subscription.Subscriber;
import io.car.server.core.subscription.Subscription;
import io.car.server.core.subscription.SubscriptionFilterParameter;
import io.car.server.core.subscription.SubscriptionFilterParameters;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
@Entity("subscriptions")
public class MongoSubscription extends MongoBaseEntity<MongoSubscription> implements Subscription {
    public static final String SUBSCRIBER = "subscriber";
    public static final String SUBSCRIPTION_FILTERS = "filters";
    @Reference(value = SUBSCRIBER, lazy = true)
    private MongoSubscriber subscriber;
    @Embedded
    private Set<MongoSubscriptionFilter> subscriptionFilters;

    @Override
    public Subscriber getSubscriber() {
        return subscriber;
    }

    @Override
    public Subscription setSubscriber(Subscriber subscriber) {
        this.subscriber = (MongoSubscriber) subscriber;
        return this;
    }

    @Override
    public SubscriptionFilterParameters getFilters() {
        return new SubscriptionFilterParameters(this.subscriptionFilters);
    }

    @Override
    public Subscription addFilter(SubscriptionFilterParameter filter) {
        this.subscriptionFilters.add((MongoSubscriptionFilter) filter);
        return this;
    }

    @Override
    public Subscription removeFilter(SubscriptionFilterParameter filter) {
        this.subscriptionFilters.remove((MongoSubscriptionFilter) filter);
        return this;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add(ID, getId())
                .add(SUBSCRIBER, getSubscriber())
                .add(SUBSCRIPTION_FILTERS, getFilters())
                .toString();
    }
}
