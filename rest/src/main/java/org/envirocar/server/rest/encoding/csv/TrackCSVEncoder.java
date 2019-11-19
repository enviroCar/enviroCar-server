/*
 * Copyright (C) 2013-2019 The enviroCar project
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
package org.envirocar.server.rest.encoding.csv;

import com.google.inject.Inject;
import org.locationtech.jts.geom.Point;
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.entities.*;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.rest.InternalServerError;
import org.envirocar.server.rest.ForbiddenException;
import org.envirocar.server.rest.rights.AccessRights;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO: Javadoc
 *
 * @author Benjamin Pross
 */
@Provider
@Singleton
public class TrackCSVEncoder extends AbstractCSVTrackEncoder<Track> {

    private final DataService dataService;
    private static final String delimiter = "; ";

    @Inject
    public TrackCSVEncoder(DataService dataService) {
        super(Track.class);
        this.dataService = dataService;
    }

    @Override
    public InputStream encodeCSV(Track t, AccessRights rights, MediaType mediaType) {
        try {
            if (rights.canSeeMeasurementsOf(t)) {
                return convert(dataService.getMeasurements(new MeasurementFilter(t)));
            } else {
                throw new ForbiddenException();
            }
        } catch (Exception e) {
            throw new InternalServerError(e);
        }
    }

    public InputStream convert(Measurements measurements) throws IOException {

        CharSequence header = null;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));

        Set<String> properties = gatherPropertiesForHeader(measurements);

        for (Measurement measurement : measurements) {

            if (header == null) {

                List<String> spaceTimeProperties = new ArrayList<>();
                spaceTimeProperties.add("longitude");
                spaceTimeProperties.add("latitude");
                spaceTimeProperties.add("time");

                header = createCSVHeader(properties, spaceTimeProperties);

                bw.append(header);
                bw.newLine();

            }

            bw.append(measurement.getIdentifier());
            bw.append(delimiter);

            for (String key : properties) {

                Object value = getValue(key, measurement.getValues());

                bw.append(value != null ? value.toString() : Double.toString(Double.NaN));
                bw.append(delimiter);
            }

            Point coord = (Point) measurement.getGeometry();
            bw.append(Double.toString(coord.getCoordinate().x));
            bw.append(delimiter);
            bw.append(Double.toString(coord.getCoordinate().y));
            bw.append(delimiter);
            bw.append(getDateTimeFormat().print(measurement.getTime()));

            bw.newLine();
        }

        bw.flush();
        bw.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    private String createCompundPropertyName(String propertyName, String unit) {

        return propertyName + "(" + unit + ")";
    }

    private String[] dissolvePropertyName(String propertyName) {

        return propertyName.replace(")", "").split("\\(");
    }

    private CharSequence createCSVHeader(Set<String> properties,
                                         List<String> spaceTimeproperties) {
        StringBuilder sb = new StringBuilder();

        sb.append("id");
        sb.append(delimiter);

        for (String key : properties) {
            sb.append(key);
            sb.append(delimiter);
        }

        for (String key : spaceTimeproperties) {
            sb.append(key);
            sb.append(delimiter);
        }

        return sb.delete(sb.length() - delimiter.length(), sb.length());
    }

    private String getValue(String phenomenonName, MeasurementValues values) {

        String[] nameAndUnit = dissolvePropertyName(phenomenonName);

        for (MeasurementValue measurementValue : values) {

            Phenomenon phenomenon = measurementValue.getPhenomenon();

            if (phenomenon.getName().equals(nameAndUnit[0])) {
                return String.valueOf(measurementValue.getValue());
            }
        }

        return "";
    }


    private Set<String> gatherPropertiesForHeader(Measurements measurements) {
        Set<String> distinctPhenomenonNames = new HashSet<>();
        for (Measurement measurement : measurements) {
            MeasurementValues values = measurement.getValues();
            for (MeasurementValue measurementValue : values) {
                Phenomenon phenomenon = measurementValue.getPhenomenon();
                String unit = phenomenon.getUnit();

                /*
                 * create property name
                 */
                String propertyName = createCompundPropertyName(phenomenon.getName(), unit);
                distinctPhenomenonNames.add(propertyName);
            }
        }
        return distinctPhenomenonNames;
    }

}
