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
package org.envirocar.server.rest.resources;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.StatisticsService;
import org.envirocar.server.core.TemporalFilter;
import org.envirocar.server.core.TemporalFilterOperator;
import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.core.util.pagination.Pagination;
import org.envirocar.server.rest.pagination.PaginationProvider;

import javax.ws.rs.core.UriInfo;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractResource {
    private Provider<DataService> dataService;
    private Provider<StatisticsService> statisticsService;
    private Provider<UriInfo> uriInfo;
    private Provider<ResourceFactory> resourceFactory;
    private PaginationProvider pagination;

    protected Pagination getPagination() throws BadRequestException {
        return pagination.get();
    }

    protected UriInfo getUriInfo() {
        return uriInfo.get();
    }

    protected DataService getDataService() {
        return dataService.get();
    }

    protected StatisticsService getStatisticsService() {
        return statisticsService.get();
    }

    protected ResourceFactory getResourceFactory() {
        return resourceFactory.get();
    }

    @Inject
    public void setUriInfo(Provider<UriInfo> uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Inject
    public void setResourceFactory(Provider<ResourceFactory> resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    @Inject
    public void setDataService(Provider<DataService> dataService) {
        this.dataService = dataService;
    }

    @Inject
    public void setStatisticsService(Provider<StatisticsService> statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Inject
    public void setPagination(PaginationProvider pagination) {
        this.pagination = pagination;
    }

    protected TemporalFilter parseTemporalFilterForInstant() {
        for (TemporalFilterOperator op : TemporalFilterOperator.values()) {
            String param = getUriInfo().getQueryParameters().getFirst(op.name());
            if (param != null) {
                try {
                    return op.parseFilterForInstant(param);
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException(e);
                }
            }
        }
        return null;
    }

    protected TemporalFilter parseTemporalFilterForInterval() {
        for (TemporalFilterOperator op : TemporalFilterOperator.values()) {
            String param = getUriInfo().getQueryParameters().getFirst(op.name());
            if (param != null) {
                try {
                    return op.parseFilterForInterval(param);
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException(e);
                }
            }
        }
        return null;
    }
}
