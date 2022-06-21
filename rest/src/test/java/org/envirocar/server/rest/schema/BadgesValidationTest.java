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
package org.envirocar.server.rest.schema;

import org.envirocar.server.rest.GuiceRunner;
import org.envirocar.server.rest.Modules;
import org.envirocar.server.rest.Schemas;
import org.envirocar.server.rest.guice.JacksonModule;
import org.envirocar.server.rest.guice.JsonSchemaModule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * TODO JavaDoc
 *
 * @author matthes rieke
 */
@RunWith(GuiceRunner.class)
@Modules({JacksonModule.class, JsonSchemaModule.class})
public class BadgesValidationTest {

    private static final String BADGE
            = "{\"name\":\"contributor\","
            + "\"displayName\": {\"en\": \"Contributor\",\"de\": \"Unterstützer\"},"
            + "\"description\": {\"en\": \"an enviroCar contributor\","
            + "\"de\": \"ein enviroCar unterstützer\"}}";
    private static final String BADGES
            = "{\"badges\":[" + BADGE + "]}";
    @Rule
    public final ValidationRule validate = new ValidationRule();

    @Test
    public void validateList() {
        assertThat(validate.parse(BADGES), is(validate.validInstanceOf(Schemas.BADGES)));
    }

    @Test
    public void validateInstance() {
        assertThat(validate.parse(BADGE), is(validate.validInstanceOf(Schemas.BADGE)));
    }
}
