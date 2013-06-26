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
package org.envirocar.server.rest.encoding.rdf;

import java.util.Set;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.envirocar.server.core.activities.Activities;
import org.envirocar.server.core.activities.Activity;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@javax.ws.rs.ext.Provider
public class ActivitiesRDFEncoder extends AbstractCollectionRDFEntityEncoder<Activity, Activities> {
    private final Provider<UriInfo> uriInfo;

    @Inject
    public ActivitiesRDFEncoder(Set<RDFLinker<Activity>> linkers,
                                Provider<UriInfo> uriInfo) {
        super(Activities.class, linkers);
        this.uriInfo = uriInfo;
    }

    @Override
    protected String getURI(Activity t, Provider<UriBuilder> uri) {
        return ActivityRDFEncoder.getURI(t, uriInfo.get().getMatchedResources()
                .get(0), uri);
    }
}
