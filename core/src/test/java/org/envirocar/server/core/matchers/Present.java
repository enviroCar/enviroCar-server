/*
 * Copyright (C) 2013-2018 The enviroCar project
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
package org.envirocar.server.core.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import java.util.Optional;

public class Present<T> extends BaseMatcher<Optional<T>> {
    private final Matcher<T> matcher;

    public Present(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(Object item) {
        if (item instanceof Optional) {
            Optional<?> optional = (Optional) item;
            return optional.isPresent() && (matcher == null || matcher.matches(optional.get()));
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(" present");
        if (matcher != null) {
            description.appendText(" and ");
            matcher.describeTo(description);
        }
    }

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        if (item instanceof Optional) {
            Optional<?> optional = (Optional) item;
            if (optional.isPresent()) {
                matcher.describeMismatch(item, mismatchDescription);
            } else {
                mismatchDescription.appendText(" not present");
            }
        } else {
            mismatchDescription.appendText(" not present");
        }
    }

    @Factory
    public static <T> Matcher<Optional<T>> present() {
        return new Present<>(null);
    }

    @Factory
    public static <T> Matcher<Optional<T>> presentAnd(Matcher<T> matcher) {
        return new Present<>(matcher);
    }
}
