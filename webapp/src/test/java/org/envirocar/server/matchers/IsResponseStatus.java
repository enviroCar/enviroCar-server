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
package org.envirocar.server.matchers;

import javax.ws.rs.core.Response.StatusType;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.sun.jersey.api.client.ClientResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class IsResponseStatus extends BaseMatcher<ClientResponse> {
    private final StatusType status;

    public IsResponseStatus(StatusType status) {
        this.status = status;
    }

    @Override
    public boolean matches(Object item) {
        if (item == null || !(item instanceof ClientResponse)) {
            return false;
        }
        return ((ClientResponse) item).getStatus() == status.getStatusCode();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("has status ")
                .appendValue(status.getStatusCode())
                .appendText(" ").appendValue(status.getReasonPhrase());
    }
}
