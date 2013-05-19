/*
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
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
package io.car.server.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.inject.Singleton;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
@Singleton
public class SchemaServlet extends HttpServlet {
    private static final long serialVersionUID = -8109532675882820381L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String file = req.getRequestURI().replace(req.getContextPath(), "")
                .replace(req.getServletPath(), "");
        InputStream is = null;
        try {
            is = SchemaServlet.class.getResourceAsStream("/schema" + file);
            if (is == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                resp
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
                ByteStreams.copy(is, resp.getOutputStream());
            }
        } finally {
            Closeables.closeQuietly(is);
        }
    }
}
