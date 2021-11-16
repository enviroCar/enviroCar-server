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
package org.envirocar.server.rest.resources;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.envirocar.server.core.entities.Terms;
import org.envirocar.server.core.entities.TermsOfUseInstance;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.rights.AllowOutdatedTerms;
import org.envirocar.server.rest.schema.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.util.Locale;

/**
 * @author matthes rieke
 */
public class TermsOfUseInstanceResource extends TermsResource {
    private final TermsOfUseInstance entity;

    @Inject
    public TermsOfUseInstanceResource(@Assisted TermsOfUseInstance t) {
        this.entity = t;
    }

    @GET
    @AllowOutdatedTerms
    @Schema(response = Schemas.TERMS_OF_USE_INSTANCE)
    @Produces({MediaTypes.JSON})
    public TermsOfUseInstance get() {
        return setContents(entity);
    }

}
