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
package org.envirocar.server.core.mail;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class MailConfigurationException extends Exception {

    private static final long serialVersionUID = -1432647028738818536L;

    public MailConfigurationException(String message) {
        super(message);
    }

    public MailConfigurationException(String format, Object... params) {
        this(String.format(format, params));
    }

    public MailConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailConfigurationException(Throwable cause) {
        super(cause);
    }

}
