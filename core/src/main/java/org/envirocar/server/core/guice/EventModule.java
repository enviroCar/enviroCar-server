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
package org.envirocar.server.core.guice;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.envirocar.server.core.util.GroupedAndNamedThreadFactory;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class EventModule extends AbstractModule {
    @Override
    protected void configure() {
        EventBus eventBus = eventBus();
        bind(EventBus.class).toInstance(eventBus);
        bindListener(Matchers.any(), new EventBusTypeListener(eventBus));
    }

    protected EventBus eventBus() {
        GroupedAndNamedThreadFactory f =
                new GroupedAndNamedThreadFactory("eventbus");
        Executor e = Executors.newCachedThreadPool(f);
        return new AsyncEventBus("eventbus", e);
    }

    private class EventBusTypeListener implements TypeListener {
        private final EventBus eventBus;

        EventBusTypeListener(EventBus eventBus) {
            this.eventBus = eventBus;
        }

        @Override
        public <I> void hear(TypeLiteral<I> typeLiteral,
                             TypeEncounter<I> typeEncounter) {
            typeEncounter.register(new EventBusInjectionListener<I>(eventBus));
        }
    }

    private class EventBusInjectionListener<I> implements InjectionListener<I> {
        private final EventBus eventBus;

        EventBusInjectionListener(EventBus eventBus) {
            this.eventBus = eventBus;
        }

        @Override
        public void afterInjection(I i) {
            eventBus.register(i);
        }
    }
}
