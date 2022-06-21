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
package org.envirocar.server.rest.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.rest.encoding.ShapefileTrackEncoder;
import org.envirocar.server.rest.encoding.shapefile.TrackShapefileEncoder;

/**
 * TODO JavaDoc
 *
 * @author Benjamin Pross
 */
public class JerseyShapefileEncoderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TrackShapefileEncoder.class);
        bind(new TypeLiteral<ShapefileTrackEncoder<Track>>() {}).to(TrackShapefileEncoder.class);
    }
}
