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
package org.envirocar.server.rest.schema;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.envirocar.server.rest.MediaTypes;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@RunWith(GuiceRunner.class)
public class ActivitiesValidationTest {
    private static final String VALID_ACTIVITIES =
            "{\"activities\":[{\"time\":\"2013-06-13T20:00:33Z\",\"type\":\"FRIENDED_USER\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"other\":{\"name\":\"dennis2\"}},{\"time\":\"2013-06-08T18:27:55Z\",\"type\":\"LEFT_GROUP\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"group\":{\"name\":\"dennis gruppe\",\"description\":\"meine erste gruppe\",\"owner\":{\"name\":\"upload\"}}},{\"time\":\"2013-06-08T18:27:41Z\",\"type\":\"UNFRIENDED_USER\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"other\":{\"name\":\"dennis2\"}},{\"time\":\"2013-06-08T18:27:38Z\",\"type\":\"FRIENDED_USER\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"other\":{\"name\":\"dennis2\"}},{\"time\":\"2013-06-08T18:27:19Z\",\"type\":\"JOINED_GROUP\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"group\":{\"name\":\"dennis gruppe\",\"description\":\"meine erste gruppe\",\"owner\":{\"name\":\"upload\"}}},{\"time\":\"2013-06-08T12:14:05Z\",\"type\":\"CREATED_TRACK\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"track\":{\"id\":\"51b3200ce4b01748637ece27\",\"modified\":\"2013-06-08T12:14:06Z\"}},{\"time\":\"2013-06-08T12:14:03Z\",\"type\":\"CREATED_TRACK\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"track\":{\"id\":\"51b3200ae4b01748637ecb7b\",\"modified\":\"2013-06-08T12:14:03Z\"}},{\"time\":\"2013-06-08T11:11:25Z\",\"type\":\"CREATED_TRACK\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"track\":{\"id\":\"51b3115de4b01748637eb674\",\"modified\":\"2013-06-08T11:11:26Z\"}},{\"time\":\"2013-06-08T05:13:24Z\",\"type\":\"CREATED_TRACK\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"track\":{\"id\":\"51b2bd74e4b01748637ea9d0\",\"modified\":\"2013-06-08T05:13:24Z\",\"name\":\"This is the Name of the track\"}},{\"time\":\"2013-06-08T05:13:24Z\",\"type\":\"CREATED_TRACK\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"track\":{\"id\":\"51b2bd74e4b01748637ea997\",\"modified\":\"2013-06-08T05:13:24Z\"}},{\"time\":\"2013-06-07T21:05:14Z\",\"type\":\"CREATED_TRACK\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"track\":{\"id\":\"51b24b0ae4b01748637ea507\",\"modified\":\"2013-06-07T21:05:14Z\",\"name\":\"This is the Name of the track\"}},{\"time\":\"2013-06-07T21:05:14Z\",\"type\":\"CREATED_TRACK\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"track\":{\"id\":\"51b24b0ae4b01748637ea500\",\"modified\":\"2013-06-07T21:05:14Z\",\"name\":\"This is the Name of the track\"}},{\"time\":\"2013-06-07T21:03:08Z\",\"type\":\"CREATED_TRACK\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"track\":{\"id\":\"51b24a8ce4b01748637ea4cc\",\"modified\":\"2013-06-07T21:03:08Z\",\"name\":\"This is the Name of the track\"}},{\"time\":\"2013-06-07T21:03:08Z\",\"type\":\"CREATED_TRACK\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"track\":{\"id\":\"51b24a8ce4b01748637ea4c6\",\"modified\":\"2013-06-07T21:03:08Z\",\"name\":\"This is the Name of the track\"}},{\"time\":\"2013-06-07T21:01:53Z\",\"type\":\"CREATED_TRACK\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"track\":{\"id\":\"51b24a41e4b01748637ea4a9\",\"modified\":\"2013-06-07T21:01:53Z\",\"name\":\"This is the Name of the track\"}},{\"time\":\"2013-06-07T21:00:40Z\",\"type\":\"CREATED_TRACK\",\"user\":{\"name\":\"jakob2\",\"mail\":\"tet@tdg.gtz\",\"created\":\"2013-06-07T20:57:34Z\",\"modified\":\"2013-06-13T20:00:33Z\"},\"track\":{\"id\":\"51b249f8e4b01748637ea428\",\"modified\":\"2013-06-07T21:00:40Z\",\"name\":\"This is the Name of the track\"}}]}";
    public static final String MISSING_USER =
            "{\"activities\":[{\"type\":\"CHANGED_PROFILE\",\"time\":\"2013-01-01T01:01:01Z\"}]}";
    public static final String ADDITIONAL_KEY =
            "{\"activities\":[], \"a\": true }";
    public static final String EMPTY_ACTIVITIES = "{\"activities\":[]}";
    public static final String EMPTY = "{}";
    @Rule
    public final ValidationRule validate = new ValidationRule();

    @Test
    public void validateActivities() {
        assertThat(validate.parse(VALID_ACTIVITIES),
                   is(validate.validInstanceOf(MediaTypes.ACTIVITIES_TYPE)));
    }

    @Test
    public void validateEmptyObject() {
        assertThat(validate.parse(EMPTY),
                   is(not(validate.validInstanceOf(MediaTypes.ACTIVITIES_TYPE))));
    }

    @Test
    public void validateEmptyActivities() {
        assertThat(validate.parse(EMPTY_ACTIVITIES),
                   is(validate.validInstanceOf(MediaTypes.ACTIVITIES_TYPE)));
    }

    @Test
    public void validateAdditionalKey() {
        assertThat(validate.parse(ADDITIONAL_KEY),
                   is(not(validate.validInstanceOf(MediaTypes.ACTIVITIES_TYPE))));
    }

    @Test
    public void missingUser() {
        assertThat(validate.parse(MISSING_USER),
                   is(not(validate.validInstanceOf(MediaTypes.ACTIVITIES_TYPE))));
    }
}
