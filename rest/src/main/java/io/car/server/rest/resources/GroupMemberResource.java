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

import javax.ws.rs.DELETE;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.car.server.core.Group;
import io.car.server.core.User;
import io.car.server.core.exception.UserNotFoundException;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class GroupMemberResource extends UserResource {
    private Group group;

    @Inject
    public GroupMemberResource(@Assisted Group group, @Assisted User member) {
        super(member);
        this.group = group;
    }

    @DELETE
    @Override
    public void delete() throws UserNotFoundException {
        if (!canModifyUser(getUser())) {
            throw new WebApplicationException(Status.FORBIDDEN);
        }
        getUserService().removeGroupMember(group, getUser());
    }
}
