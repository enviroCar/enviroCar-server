/*
 * Copyright (C) 2013-2021 The enviroCar project
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
package org.envirocar.server.rest.decoding.json;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.envirocar.server.core.entities.User;
import org.envirocar.server.rest.entity.ResetPasswordRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

@Provider
@Singleton
public class ResetPasswordDecoder extends AbstractJSONEntityDecoder<ResetPasswordRequest>{

	private final JSONEntityDecoder<User> userDecoder;

	@Inject
	public ResetPasswordDecoder(JSONEntityDecoder<User> userDec) {
		super(ResetPasswordRequest.class);
		this.userDecoder = userDec;
	}

	@Override
	public ResetPasswordRequest decode(JsonNode node, MediaType mediaType) {
		ResetPasswordRequest result = new ResetPasswordRequest();
		result.setUser(this.userDecoder.decode(node.path("user"), mediaType));
		if (node.has("code")) {
			result.setCode(node.path("code").asText());
		}
		return result;
	}


}
