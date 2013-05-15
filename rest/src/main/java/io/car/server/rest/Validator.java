/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package io.car.server.rest;

import javax.ws.rs.core.MediaType;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public interface Validator<T> {
    void validate(T t, MediaType mt) throws ValidationException;

    public static void main(String[] args) throws IOException, ProcessingException {
        JsonSchema userSchema = jsonSchemaFactory.getJsonSchema("http://envirocar.org/schema/user.json");
        String user =
               "{\"name\":\"server\",\"mail\":\"server@autermann.org\",\"creasted\":\"2013-05-14T17:27:31+02:00\",\"modified\":\"2013-05-14T17:27:31+02:00\"}";
        ProcessingReport report = userSchema.validate(JsonLoader.fromString(user));
        printReport(report);
    }

    protected static void printReport(final ProcessingReport report) {
        final boolean success = report.isSuccess();
        System.out.println("Validation " + (success ? "succeeded" : "failed"));

        if (!success) {
            System.out.println("---- BEGIN REPORT ----");
            for (final ProcessingMessage message : report) {
                System.out.println(message);
            }
            System.out.println("---- END REPORT ----");
        }
    }
}
