/*
 * Copyright (C) 2013-2020 The enviroCar project
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
package org.envirocar.server.rest.util;

import org.envirocar.server.core.entities.Measurements;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface TileRenderer {


    /**
     * This methos checks whether an image for a given track exists.
     *
     * @param trackId The track id.
     * @return If the image already exists.
     * @throws IOException If the check fails.
     */
    boolean imageExists(String trackId) throws IOException;

    /**
     * This method outputs a rendered image when the image measurements are given
     *
     * @param measurements The measurements.
     * @return The generated image.
     * @throws IOException If the image creation fails.
     */
    BufferedImage createImage(Measurements measurements) throws IOException;

    /**
     * This method returns a clipped image which has a given resolution when a image of a higher size is given
     *
     * @param image        The image
     * @param measurements The measurements.
     * @param width        The required width.
     * @param height       The required height
     * @param padding      The padding.
     * @return The clipped image.
     */
    BufferedImage clipImage(BufferedImage image, Measurements measurements, int width, int height, int padding);

    /**
     * This method saves a {@link BufferedImage} to disk.
     *
     * @param image   The image.
     * @param trackId The track identifier
     * @throws IOException If the operation fails.
     */
    void saveImage(BufferedImage image, String trackId) throws IOException;

    /**
     * This method reads an image from disk and return it as a BufferedImage.
     *
     * @param trackId The track id.
     * @return The loaded image.
     * @throws IOException If the operation fails.
     */
    BufferedImage loadImage(String trackId) throws IOException;
}
