/**
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.rest;

import javax.ws.rs.core.MediaType;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public interface MediaTypes {
    String USERS = "application/json; schema=\"users.json\"";
    MediaType USERS_TYPE = MediaType.valueOf(USERS);
    String USER = "application/json; schema=\"user.json\"";
    MediaType USER_TYPE = MediaType.valueOf(USER);
    String USER_MODIFY = "application/json; schema=\"user.modify.json\"";
    MediaType USER_MODIFY_TYPE = MediaType.valueOf(USER_MODIFY);
    String USER_CREATE = "application/json; schema=\"user.create.json\"";
    MediaType USER_CREATE_TYPE = MediaType.valueOf(USER_CREATE);
    String USER_REF = "application/json; schema=\"user.ref.json\"";
    MediaType USER_REF_TYPE = MediaType.valueOf(USER_REF);
}
