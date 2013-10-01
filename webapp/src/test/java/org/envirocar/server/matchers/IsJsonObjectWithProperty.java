/*
 * Copyright (C) 2013 The enviroCar project
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
package org.envirocar.server.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class IsJsonObjectWithProperty extends BaseMatcher<JsonNode> {
    private String key;

    public IsJsonObjectWithProperty(String key) {
        this.key = key;
    }

    @Override
    public boolean matches(Object item) {
        if (item instanceof JsonNode) {
            JsonNode json = (JsonNode) item;
            return !json.path(key).isMissingNode();
        } else {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("has property ").appendValue(this.key);
    }
}
