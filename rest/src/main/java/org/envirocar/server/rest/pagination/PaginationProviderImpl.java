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
package org.envirocar.server.rest.pagination;

import com.google.inject.Inject;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.util.pagination.PageBasedPagination;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.core.util.pagination.RangeBasedPagination;
import org.envirocar.server.rest.PrefixedUriInfo;
import org.envirocar.server.rest.RESTConstants;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class PaginationProviderImpl implements PaginationProvider {
    private static final int DECIMAL_RADIX = 10;
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_LIMIT = 100;
    private static final String RANGE_HEADER = "Range";
    private final HttpHeaders headers;
    private final UriInfo uriInfo;

    @Inject
    public PaginationProviderImpl(HttpHeaders headers, UriInfo uriInfo) {
        this.headers = headers;
        this.uriInfo = uriInfo;
    }

    @Override
    public Pagination get() throws BadRequestException {
        return or(getPaginationFromRangeHeader(), getPaginationFromQueryParam()).orElse(getDefaultPagination());
    }

    private Optional<Pagination> getPaginationFromRangeHeader() throws BadRequestException {
        List<String> headers = this.headers.getRequestHeader(RANGE_HEADER);
        if (headers != null) {
            for (String header : headers) {
                if (header != null) {
                    Optional<Pagination> range = parseRangeHeader(header);
                    if (range.isPresent()) {
                        return range;
                    }
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Pagination> parseRangeHeader(String rangeHeader) throws BadRequestException {
        String trimmed = rangeHeader.trim();
        if (!trimmed.startsWith("items=")) {
            return Optional.empty();
        }
        trimmed = trimmed.substring("items=".length());
        String[] ranges = trimmed.split(",");
        if (ranges.length > 1) {
            throw newInvalidRangeException();
        }
        String[] range = ranges[0].split("-");
        if (range.length != 2) {
            throw newInvalidRangeException();
        }
        if (range[0].isEmpty()) {
            throw newInvalidRangeException();
        }
        try {
            int begin = Integer.parseInt(range[0], DECIMAL_RADIX);
            int end = Integer.parseInt(range[1], DECIMAL_RADIX);
            if (begin > end) {
                throw newInvalidRangeException();
            }
            return Optional.of(new RangeBasedPagination(begin, end));
        } catch (IllegalArgumentException e) {
            throw newInvalidRangeException();
        }
    }


    private static <T> Optional<T> or(Optional<T>... alternatives) {
        return Arrays.stream(alternatives).filter(Optional::isPresent).map(Optional::get).findFirst();
    }

    private Optional<Pagination> getPaginationFromQueryParam()
            throws BadRequestException {
        Optional<Integer> limit = or(getQueryParameterValue(RESTConstants.LIMIT), getQueryParameterValue("l"));
        Optional<Integer> page = or(getQueryParameterValue(RESTConstants.PAGE), getQueryParameterValue("p"));
        if (limit.isPresent() || page.isPresent()) {
            return Optional.of(new PageBasedPagination(limit.orElse(DEFAULT_LIMIT), page.orElse(DEFAULT_PAGE)));
        }
        return Optional.empty();
    }

    private Optional<Integer> getQueryParameterValue(String parameter) {
        List<String> list = this.uriInfo.getQueryParameters().get(parameter);
        if (list != null && !list.isEmpty()) {
            try {
                return Optional.of(Integer.valueOf(list.get(0), DECIMAL_RADIX));
            } catch (NumberFormatException ignored) {
            }
        }
        return Optional.empty();
    }

    private Pagination getDefaultPagination() {
        return new PageBasedPagination(DEFAULT_LIMIT, DEFAULT_PAGE);
    }

    private static BadRequestException newInvalidRangeException() {
        return new BadRequestException("Invalid Range");
    }

}
