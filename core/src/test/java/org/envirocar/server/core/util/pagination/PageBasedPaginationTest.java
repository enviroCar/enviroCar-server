/*
 * Copyright (C) 2013-2022 The enviroCar project
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

import java.util.Optional;


public class PageBasedPaginationTest {


    private static final long PAGE_SIZE = 10;
    private static final long SIZE = 100;

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    protected <T> void assertThat(T value, Matcher<T> matcher) {
        errors.checkThat(value, matcher);
    }

    @Test
    public void testFirst() {
        assertThat(page(1).first(0), isNotPresent());
        assertThat(page(1).first(SIZE), isNotPresent());
        assertThat(page(2).first(SIZE), isPresentAndPage(1));
        assertThat(page(3).first(SIZE), isPresentAndPage(1));
        assertThat(page(4).first(SIZE), isPresentAndPage(1));
        assertThat(page(5).first(SIZE), isPresentAndPage(1));
        assertThat(page(6).first(SIZE), isPresentAndPage(1));
        assertThat(page(7).first(SIZE), isPresentAndPage(1));
        assertThat(page(8).first(SIZE), isPresentAndPage(1));
        assertThat(page(9).first(SIZE), isPresentAndPage(1));
        assertThat(page(10).first(SIZE), isPresentAndPage(1));
        assertThat(page(11).first(SIZE), isNotPresent());
    }

    @Test
    public void testNext() {
        assertThat(page(1).next(0), isNotPresent());
        assertThat(page(1).next(SIZE), isPresentAndPage(2));
        assertThat(page(2).next(SIZE), isPresentAndPage(3));
        assertThat(page(3).next(SIZE), isPresentAndPage(4));
        assertThat(page(4).next(SIZE), isPresentAndPage(5));
        assertThat(page(5).next(SIZE), isPresentAndPage(6));
        assertThat(page(6).next(SIZE), isPresentAndPage(7));
        assertThat(page(7).next(SIZE), isPresentAndPage(8));
        assertThat(page(8).next(SIZE), isPresentAndPage(9));
        assertThat(page(9).next(SIZE), isNotPresent());
        assertThat(page(10).next(SIZE), isNotPresent());
        assertThat(page(11).next(SIZE), isNotPresent());
    }

    @Test
    public void testLast() {
        assertThat(page(1).last(0), isNotPresent());
        assertThat(page(1).last(SIZE), isPresentAndPage(10));
        assertThat(page(2).last(SIZE), isPresentAndPage(10));
        assertThat(page(3).last(SIZE), isPresentAndPage(10));
        assertThat(page(4).last(SIZE), isPresentAndPage(10));
        assertThat(page(5).last(SIZE), isPresentAndPage(10));
        assertThat(page(6).last(SIZE), isPresentAndPage(10));
        assertThat(page(7).last(SIZE), isPresentAndPage(10));
        assertThat(page(8).last(SIZE), isPresentAndPage(10));
        assertThat(page(9).last(SIZE), isPresentAndPage(10));
        assertThat(page(10).last(SIZE), isNotPresent());
        assertThat(page(11).last(SIZE), isPresentAndPage(10));
    }

    @Test
    public void testPrev() {
        assertThat(page(1).previous(0), isNotPresent());
        assertThat(page(1).previous(SIZE), isNotPresent());
        assertThat(page(2).previous(SIZE), isNotPresent());
        assertThat(page(3).previous(SIZE), isPresentAndPage(2));
        assertThat(page(4).previous(SIZE), isPresentAndPage(3));
        assertThat(page(5).previous(SIZE), isPresentAndPage(4));
        assertThat(page(6).previous(SIZE), isPresentAndPage(5));
        assertThat(page(7).previous(SIZE), isPresentAndPage(6));
        assertThat(page(8).previous(SIZE), isPresentAndPage(7));
        assertThat(page(9).previous(SIZE), isPresentAndPage(8));
        assertThat(page(10).previous(SIZE), isPresentAndPage(9));
        assertThat(page(11).previous(SIZE), isNotPresent());
    }
    
    @Test
    public void testMaxSize() {
    	long configuredMax = Pagination.MAX_PAGE_SIZE;
    	PageBasedPagination pbp = new PageBasedPagination(configuredMax + 10, 1);
    	assertThat(pbp.getLimit(), Matchers.is(configuredMax));
    	assertThat(pbp.getBegin(), Matchers.is(0L));
    	assertThat(pbp.getEnd(), Matchers.is(configuredMax));
    	assertThat(pbp.getPage(), Matchers.is(1L));
    }

    protected static Pagination page(int page) {
        return new PageBasedPagination(PAGE_SIZE, page);
    }

    protected static Matcher<Optional<Pagination>> isNotPresent() {
        return Matchers.is(Matchers.not(Present.present()));
    }

    protected static Matcher<Optional<Pagination>> isPresentAndPage(int page) {
        return Matchers.is(Present.presentAnd(Matchers.is(page(page))));
    }
}
