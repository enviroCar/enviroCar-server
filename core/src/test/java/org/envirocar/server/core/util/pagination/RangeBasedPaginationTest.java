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
package org.envirocar.server.core.util.pagination;

import org.envirocar.server.core.matchers.Present;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import com.google.common.base.Optional;

public class RangeBasedPaginationTest {

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    protected <T> void assertThat(T value, Matcher<T> matcher) {
        errors.checkThat(value, matcher);
    }

    @Test
    public void testFirst() {
        assertThat(range( 0,  9).first(100), isNotPresent());
        assertThat(range(10, 19).first(100), isPresentAndRange(0, 9));
        assertThat(range(10, 13).first(100), isNotPresent());
        assertThat(range(20, 29).first(100), isPresentAndRange(0, 9));
        assertThat(range(80, 89).first(100), isPresentAndRange(0, 9));
        assertThat(range(90, 99).first(100), isPresentAndRange(0, 9));
    }

    @Test
    public void testNext() {
        assertThat(range( 0,  9).next(100), isPresentAndRange(10, 19));
        assertThat(range(10, 19).next(100), isPresentAndRange(20, 29));
        assertThat(range(10, 13).next(100), isNotPresent());
        assertThat(range(20, 29).next(100), isPresentAndRange(30, 39));
        assertThat(range(80, 89).next(100), isNotPresent());
        assertThat(range(90, 99).next(100), isNotPresent());
    }

    @Test
    public void testLast() {
        assertThat(range( 0,  9).last(100), isPresentAndRange(90, 99));
        assertThat(range(10, 19).last(100), isPresentAndRange(90, 99));
        assertThat(range(10, 13).last(100), isNotPresent());
        assertThat(range(20, 29).last(100), isPresentAndRange(90, 99));
        assertThat(range(80, 89).last(100), isPresentAndRange(90, 99));
        assertThat(range(90, 99).last(100), isNotPresent());
        assertThat(range( 0,  9).last( 95), isPresentAndRange(90, 94));
    }

    @Test
    public void testPrev() {
        assertThat(range( 0,  9).previous(100), isNotPresent());
        assertThat(range(10, 19).previous(100), isNotPresent());
        assertThat(range(10, 13).previous(100), isNotPresent());
        assertThat(range(20, 29).previous(100), isPresentAndRange(10, 19));
        assertThat(range(80, 89).previous(100), isPresentAndRange(70, 79));
        assertThat(range(90, 99).previous(100), isPresentAndRange(80, 89));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMaxSize() {
    	long configuredMax = Pagination.MAX_PAGE_SIZE;
    	new RangeBasedPagination(configuredMax, configuredMax*2 + 100);
    }

    protected static Pagination range(int begin, int end) {
        return new RangeBasedPagination(begin, end);
    }

    protected static Matcher<Optional<?>> isNotPresent() {
        return Matchers.is(Matchers.not(Present.present()));
    }

    protected static Matcher<Optional<Pagination>> isPresentAndRange(int begin, int end) {
        return Matchers.is(Present.presentAnd(Matchers.is(range(begin, end))));
    }
}
