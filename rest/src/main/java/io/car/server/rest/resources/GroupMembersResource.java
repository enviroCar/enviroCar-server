/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package io.car.server.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.Group;
import io.car.server.core.User;
import io.car.server.core.Users;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.rest.AbstractResource;
import io.car.server.rest.MediaTypes;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class GroupMembersResource extends AbstractResource {
    private Group group;

    @Inject
    public GroupMembersResource(@Assisted Group group) {
        this.group = group;
    }

    @GET
    public Users get() {
        return group.getMembers();
    }

    @POST
    @Consumes(MediaTypes.USER_REF)
    public void add(User user) throws UserNotFoundException {
        if (!canModifyUser(user)) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        getUserService().addGroupMember(group, user);
    }

    @Path("{member}")
    public GroupMemberResource friend(@PathParam("member") String username) throws UserNotFoundException {
        return getResourceFactory().createGroupMemberResource(group, getUserService().getUser(username));
    }
}
