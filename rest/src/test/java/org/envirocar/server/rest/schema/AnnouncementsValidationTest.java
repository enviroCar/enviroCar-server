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
public class AnnouncementsValidationTest {
    private static final String ANNOUNCMENT
            = "{\"id\":\"5270f4b2fce1af4c7464cf2c\",\"versions\":\"[0, 1.8.0]" +
            "\",\"category\":\"app\",\"priority\":\"medium\",\"content\":{\"d" +
            "e\":\"<html><body><h2>teste das announcements system</h2><p>Lore" +
            "m ipsum dolor sit amet, consetetur sadipscing elitr, sed diam no" +
            "numy eirmod tempor invidunt ut labore et dolore magna aliquyam e" +
            "rat, sed diam voluptua. At vero eos et accusam et justo duo dolo" +
            "res et ea rebum. Stet clita kasd gubergren, no sea takimata sanc" +
            "tus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, " +
            "consetetur sadipscing elitr, sed diam nonumy eirmod tempor invid" +
            "unt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
            "At vero eos et accusam et justo duo dolores et ea rebum. Stet cl" +
            "ita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolo" +
            "r sit amet.</p><p>ich habe fertig.</p></body></html>\",\"en\":\"" +
            "<html><body><h2>testing the announcement systems</h2><p>Lorem ip" +
            "sum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy" +
            " eirmod tempor invidunt ut labore et dolore magna aliquyam erat," +
            " sed diam voluptua. At vero eos et accusam et justo duo dolores " +
            "et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus " +
            "est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, cons" +
            "etetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt " +
            "ut labore et dolore magna aliquyam erat, sed diam voluptua. At v" +
            "ero eos et accusam et justo duo dolores et ea rebum. Stet clita " +
            "kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor si" +
            "t amet.</p><p>i'm done.</p></body></html>\"}}";
    private static final String ANNOUNCEMNTS
            = "{\"announcements\":[" + ANNOUNCMENT + "]}";
    @Rule
    public final ValidationRule validate = new ValidationRule();

    @Test
    public void validateList() {
        assertThat(validate.parse(ANNOUNCEMNTS),
                   is(validate.validInstanceOf(MediaTypes.ANNOUNCEMENTS_TYPE)));
    }

    @Test
    public void validateInstance() {
        assertThat(validate.parse(ANNOUNCMENT),
                   is(validate.validInstanceOf(MediaTypes.ANNOUNCEMENT_TYPE)));
    }
}
