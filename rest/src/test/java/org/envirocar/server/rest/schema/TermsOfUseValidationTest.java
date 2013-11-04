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
public class TermsOfUseValidationTest {
    private static final String TERMS_OF_USE
            = "{\"created\":\"2013-10-01T12:00:00Z\",\"issuedDate\":\"2013-10-0" +
              "1\",\"contents\":\"<p style=\\\"text-align: justify\\\">\\nWhen " +
              "you accept our ,Terms of Use, you agree to comply with the follow" +
              "ing regulations.\\n<br><br>\\n<strong>1. What happens to User Da" +
              "ta</strong> \\n<br>\\nWhen using this website, your IP address w" +
              "ill be stored  in our server’s access log and error log file.\\n" +
              "<br><br>\\nWhen you register for or use the enviroCar services, " +
              "the following information will be collected and stored. This inf" +
              "ormation is deleted when you delete your account.\\n</p>\\n<ul>\\" +
              "n<li>The user name you chose</li>\\n<li>The email address you pr" +
              "ovided</li>\\n<li>The password you provided</li>\\n<li>The user " +
              "name of persons who added your user name as a friend</li>\\n<li>" +
              "The user name of the person you added as a friend</li>\\n<li>The" +
              " name of the groups you joined</li>\\n<li>Other optional informa" +
              "tion (if provided):\\n<ul>\\n<li>First Name, Last Name</li>\\n<l" +
              "i>The country and city in which you live</li>\\n<li>Your website" +
              "</li>\\n<li>Your gender</li>\\n<li>The language of preference fo" +
              "r the enviroCar website</li>\\n</ul>\\nAny other information you" +
              " provided in a free text form</li>\\n</ul>\\n<p style=\\\"text-a" +
              "lign: justify\\\">\\nYou can delete (there is no way back!) your" +
              " account and the data associated with it (see above) at anytime." +
              " Tracks that have been uploaded as Open Data and published to th" +
              "e public are NOT deleted. If you would like to delete your track" +
              "s as well, you must do this before you delete your account. Once" +
              " your account is gone, it is not possible to find out which trac" +
              "ks were yours. Please read the FAQ <link> for more information a" +
              "bout how to delete your data and/or your account.\\n</p>       \\" +
              "n<br>\\n<strong>2. Saving data during a drive</strong> \\n<br>\\" +
              "n<p style=\\\"text-align: justify\\\">\\nDuring your test drive " +
              "(after the START button has been pushed until the STOP button ha" +
              "s been pushed), the following data is collected and saved:\\n</p" +
              ">\\n<ul>\\n<li>Location of measurement</li>\\n<li>Time and date " +
              "of measurement</li>\\n<li>Data from the engine control system, f" +
              "or example speed, revolutions per minute, throttle pedal positio" +
              "n</li>\\n<li>Derived data, such as fuel consumption, CO2 emissio" +
              "n</li>\\n</ul>\\n<p style=\\\"text-align: justify\\\">\\nThis da" +
              "ta is linked via an encrypted key to your user account, which en" +
              "ables you to view your own data in the enviroCar App and on the " +
              "website. Communication between the enviroCar App, the enviroCar " +
              "server and the enviroCar website is encrypted so that no third p" +
              "arty can access your data.\\n</p>\\n<br>\\n<strong>3. Upload dat" +
              "a to enviroCar server</strong> \\n<br>\\n<p style=\\\"text-align" +
              ": justify\\\">\\nWhen you upload your data to the enviroCar serv" +
              "er (option: “Upload Track as Open Data“), you agree to publish t" +
              "he measured data as anonymous Open Data and allow third parties " +
              "to use the data in accordance with the Open Database License (OD" +
              "bL).\\nData which is downloaded from the enviroCar server contai" +
              "ns no user information. The enviroCar App has an option in “Sett" +
              "ings” with which you can cut off the beginning and end of your t" +
              "racks so that the point of departure and the destination are obs" +
              "cured.\\n</p>\\n<br>\\n<strong>4. Licensing</strong> \\n<br>\\n<" +
              "p style=\\\"text-align: justify\\\">\\nData available via the en" +
              "viroCar API can be used by third parties in accordance with the " +
              "Open Database License <a target=\\\"_blank\\\" href=\\\"http://o" +
              "pendatacommons.org/licenses/odbl/1.0/\\\">ODbL</a>. This means\\" +
              "n</p>\\n<ul>\\n<li style=\\\"list-style-type: none;\\\">\\na) yo" +
              "u are allowed to copy and redistribute this database, create wor" +
              "ks from this database and modify copies of this database\\n</li>" +
              "\\n<li style=\\\"list-style-type: none;\\\">\\nb) as long as you" +
              " mention the source of the data (use “Source: 52°North enviroCar" +
              " Server”), share copies or modified copies of this database in a" +
              "ccordance with the <a target=\\\"_blank\\\" href=\\\"http://open" +
              "datacommons.org/licenses/odbl/1.0/\\\">ODbL</a> and keep your da" +
              "tabase open and accessible (you are not allowed to protect it wi" +
              "th Digital Rights Managements (DRM) measures if it is not simult" +
              "aneously available as a DRM free version).\\n</li>\\n</ul>\\n<br" +
              ">\\n<strong>5. Use of cookies</strong> \\n<br>\\n<p style=\\\"te" +
              "xt-align: justify\\\">\\nWhen you use this website, you agree to" +
              " the use of cookies. The cookies will be used strictly for the p" +
              "urpose of enabling you to log in to our services and select the " +
              "language of preference for our website. The cookie stores a comb" +
              "ination of numbers and characters to identify you during a brows" +
              "er session. Our cookies are valid for one (1) browser session. " +
              "\\n</p>\",\"id\":\"524e939cfcba4bf4d2d4fa10\"}";
    private static final String TERMS_OF_USE_LIST
            = "{\"termsOfUse\":[{\"id\":\"524e939cfcba4bf4d2d4fa10\",\"issuedDa" +
              "te\":\"2013-10-01\"}]}";
    @Rule
    public final ValidationRule validate = new ValidationRule();

    @Test
    public void validateList() {
        assertThat(validate.parse(TERMS_OF_USE_LIST),
                   is(validate.validInstanceOf(MediaTypes.TERMS_OF_USE_TYPE)));
    }

    @Test
    public void validateInstance() {
        assertThat(validate.parse(TERMS_OF_USE),
                   is(validate
                .validInstanceOf(MediaTypes.TERMS_OF_USE_INSTANCE_TYPE)));
    }
}
