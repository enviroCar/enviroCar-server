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
package org.envirocar.server.rest.mapper;


import org.envirocar.server.rest.UnauthorizedException;

import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;
import java.util.Collections;
import java.util.Map;

@Provider
@Singleton
public class UnauthorizedExceptionMapper extends AbstractExceptionMapper<UnauthorizedException> {

    @Override
    protected Map<String, String> getAdditionalHeaders() {
        return Collections.singletonMap(HttpHeaders.WWW_AUTHENTICATE, "Basic");
    }

    @Override
    protected Response.StatusType getStatus(UnauthorizedException exception) {
        return Status.UNAUTHORIZED;
    }
}
