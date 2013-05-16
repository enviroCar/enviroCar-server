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
package io.car.server.rest.coding;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.format.DateTimeFormatter;

import com.google.inject.Inject;

import io.car.server.core.EntityFactory;
import io.car.server.core.entities.Phenomenon;
import io.car.server.rest.EntityDecoder;
import io.car.server.rest.EntityEncoder;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.resources.PhenomenonsResource;
import io.car.server.rest.resources.RootResource;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class PhenomenonCoder implements EntityEncoder<Phenomenon>, EntityDecoder<Phenomenon> {
    private final DateTimeFormatter formatter;
    private final EntityFactory factory;
    private final UriInfo uriInfo;

    @Inject
    public PhenomenonCoder(DateTimeFormatter formatter, EntityFactory factory, UriInfo uriInfo) {
        this.formatter = formatter;
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @Override
    public Phenomenon decode(JSONObject j, MediaType mediaType) throws JSONException {
        return factory.createPhenomenon()
                .setName(j.optString(JSONConstants.NAME_KEY, null));
    }

    @Override
    public JSONObject encode(Phenomenon t, MediaType mediaType) throws JSONException {
        JSONObject j = new JSONObject().put(JSONConstants.NAME_KEY, t.getName());
        if (mediaType.equals(MediaTypes.PHENOMENON_TYPE)) {
            j.put(JSONConstants.CREATED_KEY, formatter.print(t.getCreationDate()));
            j.put(JSONConstants.MODIFIED_KEY, formatter.print(t.getLastModificationDate()));
        } else {
            URI href = uriInfo.getBaseUriBuilder()
                    .path(RootResource.class)
                    .path(RootResource.PHENOMENONS_PATH)
                    .path(PhenomenonsResource.PHENOMENON_PATH)
                    .build(t.getName());
            j.put(JSONConstants.HREF_KEY, href);
        }
        return j;
    }
}
