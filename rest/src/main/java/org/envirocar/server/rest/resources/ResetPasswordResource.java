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
package org.envirocar.server.rest.resources;

import org.envirocar.server.core.exception.BadRequestException;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.entity.ResetPasswordRequest;
import org.envirocar.server.rest.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;


public class ResetPasswordResource extends AbstractResource {


    private static final Logger LOG = LoggerFactory.getLogger(ResetPasswordResource.class);

    @POST
    @Schema(request = Schemas.PASSWORD_RESET_REQUEST)
    @Consumes({MediaTypes.JSON})
    public Response get(ResetPasswordRequest resetPassword) throws BadRequestException {
        checkRights(getRights().canAccessPasswordReset());

        getUserService().requestPasswordReset(resetPassword.getUser());
        LOG.info("Successfully processed password reset request for user {}", resetPassword.getUser());

        return Response.noContent().build();
    }

    @PUT
    @Schema(request = Schemas.PASSWORD_RESET_VERIFICATION)
    @Consumes({MediaTypes.JSON})
    public Response resetPassword(ResetPasswordRequest resetPassword) throws BadRequestException {
        checkRights(getRights().canAccessPasswordReset());

        getUserService().resetPassword(resetPassword.getUser(), resetPassword.getCode());
        LOG.info("Password reset for user {}", resetPassword.getUser());

        return Response.noContent().build();
    }

}
