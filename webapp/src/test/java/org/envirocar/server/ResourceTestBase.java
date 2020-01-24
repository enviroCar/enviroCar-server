/*
 * Copyright (C) 2013-2020 The enviroCar project
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
package org.envirocar.server;

import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import org.envirocar.server.mongo.entity.MongoUser;
import org.envirocar.server.rest.JSONConstants;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.resources.RootResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;

import javax.ws.rs.core.Response;

import static org.envirocar.server.matchers.JerseyMatchers.hasStatus;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class ResourceTestBase {
    @Inject
    private DB db;
    @Inject
    private JsonNodeCreator nodeFactory;
    @ClassRule
    public static EnviroCarServer server = new EnviroCarServer();

    protected JsonNodeCreator getNodeFactory() {
        return nodeFactory;
    }

    @Before
    public void inject() {
        server.getInjector().injectMembers(this);
    }

    protected WebResource resource() {
        return server.resource();
    }

    protected Client client() {
        return server.client();
    }

    protected String getBaseURL() {
        return server.getBaseURL();
    }

    protected String getBasicAuthHeader(String username, String password) {
        return String.format("Basic %s", new String(Base64.encode(String.format("%s:%s", username, password))));
    }

    protected void createUser(String name, String pass, String mail) {
        final ObjectNode user = getNodeFactory().objectNode()
                                                .put(JSONConstants.NAME_KEY, name)
                                                .put(JSONConstants.MAIL_KEY, mail)
                                                .put(JSONConstants.TOKEN_KEY, pass)
                                                .put(JSONConstants.ACCEPTED_PRIVACY_STATEMENT, true)
                                                .put(JSONConstants.ACCEPTED_TERMS_OF_USE, true);
        assertThat(resource().path("/").path(RootResource.USERS)
                             .entity(user, MediaTypes.JSON_TYPE)
                             .post(ClientResponse.class),
                   hasStatus(Response.Status.CREATED));

        DBObject mongoUser = db.getCollection(MongoUser.COLLECTION).findOne(new BasicDBObject(MongoUser.NAME, name));

        Assert.assertThat(mongoUser, is(not(nullValue())));
        String confirmationCode = (String) mongoUser.get(MongoUser.CONFIRMATION_CODE);
        Assert.assertThat(resource().path("/").path(RootResource.CONFIRM).path(confirmationCode)
                                    .get(ClientResponse.class).getStatus(),
                          allOf(is(greaterThanOrEqualTo(Response.Status.OK.getStatusCode())),
                                is(lessThan(Response.Status.BAD_REQUEST.getStatusCode()))));
    }

}
