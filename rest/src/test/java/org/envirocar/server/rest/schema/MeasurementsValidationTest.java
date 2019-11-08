/*
 * Copyright (C) 2013-2018 The enviroCar project
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
import org.envirocar.server.rest.Schemas;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Scanner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * TODO JavaDoc
 */
@RunWith(GuiceRunner.class)
public class MeasurementsValidationTest {

    @Rule
    public final ValidationRule validate = new ValidationRule();

    @Test
    public void validateValidMeasurementsInstance() {
        assertThat(validate.parse(loadMeasurementsInstance("measurements-instance.json")),
                is(validate.validInstanceOf(Schemas.MEASUREMENTS)));
    }

    @Test
    public void validateValidMeasurementsInstanceWithoutTrackReference() {
        assertThat(validate.parse(loadMeasurementsInstance("measurements-instance-wo-trackref.json")),
                is(validate.validInstanceOf(Schemas.MEASUREMENTS)));
    }

    private String loadMeasurementsInstance(String res) {
        StringBuilder sb = new StringBuilder();

        Scanner sc = new Scanner(getClass().getResourceAsStream(res));
        while (sc.hasNext()) {
            sb.append(sc.nextLine());
        }
        sc.close();

        return sb.toString();
    }
}
