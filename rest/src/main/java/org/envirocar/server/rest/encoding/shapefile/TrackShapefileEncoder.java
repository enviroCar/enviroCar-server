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
package org.envirocar.server.rest.encoding.shapefile;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.io.Closeables;
import com.google.inject.Inject;
import org.envirocar.server.core.DataService;
import org.envirocar.server.core.entities.Measurement;
import org.envirocar.server.core.entities.MeasurementValues;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.rest.rights.AccessRights;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDumper;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.joda.time.format.DateTimeFormatter;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.stream.Collectors.toList;

/**
 * TODO: Javadoc
 *
 * @author Benjamin Pross
 */
@Provider
@Singleton
public class TrackShapefileEncoder extends AbstractShapefileTrackEncoder {
    private static final Logger LOG = LoggerFactory.getLogger(TrackShapefileEncoder.class);
    private static final String ID_ATTRIBUTE_NAME = "id";
    private static final String GEOMETRY_ATTRIBUTE_NAME = "the_geom";
    private static final String TIME_ATTRIBUTE_NAME = "time";
    private static final String PROPERTIES_PATH = "/export.properties";
    private static final String DEFAULT_PROPERTIES_PATH = "/export.default.properties";
    private static final Properties PROPERTIES = getProperties();
    @VisibleForTesting
    static final int shapeFileExportThreshold = Optional.ofNullable(PROPERTIES
                                                                            .getProperty("shapefile.export.measurement.threshold"))
                                                        .map(Integer::parseInt).orElse(1000);
    private final DataService dataService;
    private CoordinateReferenceSystem crs;
    private final DateTimeFormatter dateTimeFormat;

    private static Properties getProperties() {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = TrackShapefileEncoder.class.getResourceAsStream(PROPERTIES_PATH);
            if (in == null) {
                LOG.info("No {} found, loading {}.", PROPERTIES_PATH, DEFAULT_PROPERTIES_PATH);
                in = TrackShapefileEncoder.class.getResourceAsStream(DEFAULT_PROPERTIES_PATH);
                if (in == null) {
                    LOG.warn("No {} found!", DEFAULT_PROPERTIES_PATH);
                }
            }
            if (in != null) {
                properties.load(in);
            }

            return properties;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } finally {
            Closeables.closeQuietly(in);
        }
    }

    @Inject
    public TrackShapefileEncoder(DataService dataService, DateTimeFormatter dateTimeFormat) {
        this.dataService = Objects.requireNonNull(dataService);
        this.dateTimeFormat = Objects.requireNonNull(dateTimeFormat);
    }

    @Override
    public Path encodeShapefile(Track track, AccessRights rights, MediaType mediaType) throws IOException {
        Path shapeDirectory = null;
        try {
            Measurements measurements = this.dataService.getMeasurements(new MeasurementFilter(track));
            shapeDirectory = createShapeFile(track.getIdentifier(), createFeatureCollection(track, measurements));
            return zip(shapeDirectory);
        } finally {
            delete(shapeDirectory);
        }
    }

    private SimpleFeatureCollection createFeatureCollection(Track track, Measurements measurements) {
        SimpleFeatureType sft = createFeatureType(track, measurements);
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
        List<SimpleFeature> simpleFeatureList = measurements.stream().map(m -> {
            sfb.set(ID_ATTRIBUTE_NAME, m.getIdentifier());
            sfb.set(TIME_ATTRIBUTE_NAME, this.dateTimeFormat.print(m.getTime()));
            sfb.set(GEOMETRY_ATTRIBUTE_NAME, m.getGeometry());
            m.getValues().forEach(mv -> sfb.set(mv.getPhenomenon().getName(), mv.getValue().toString()));
            return sfb.buildFeature(m.getIdentifier());
        }).collect(toList());

        return new ListFeatureCollection(sft, simpleFeatureList);
    }

    private SimpleFeatureType createFeatureType(Track track, Measurements measurements) {
        SimpleFeatureTypeBuilder sftb = new SimpleFeatureTypeBuilder();
        sftb.setCRS(getCRS());
        sftb.setNamespaceURI("https://enviroCar.org/api/stable/tracks");
        sftb.setName(track.getIdentifier());
        sftb.add(GEOMETRY_ATTRIBUTE_NAME, Point.class);
        sftb.add(ID_ATTRIBUTE_NAME, String.class);
        sftb.add(TIME_ATTRIBUTE_NAME, String.class);
        // TODO support double, boolean, String types
        measurements.stream().map(Measurement::getValues)
                    .flatMap(MeasurementValues::stream)
                    .map(mv -> mv.getPhenomenon().getName())
                    .distinct()
                    .forEach(name -> sftb.add(name, String.class));
        return sftb.buildFeatureType();
    }

    private Path createShapeFile(String name, SimpleFeatureCollection collection)
            throws IOException {
        Path tempDirectory = Files.createTempDirectory("enviroCarShapeExport-");
        try {
            ShapefileDumper dumper = new ShapefileDumper(tempDirectory.toFile());
            dumper.setCharset(StandardCharsets.UTF_8);
            dumper.setEmptyShapefileAllowed(true);
            dumper.dump(collection);
            return tempDirectory;
        } catch (Exception e) {
            try {
                delete(tempDirectory);
            } catch (IOException ex) {
                e.addSuppressed(ex);
            }
            Throwables.propagateIfPossible(e, IOException.class);
            throw new IOException(e);
        }
    }

    private Path zip(Path directory) throws IOException {
        Path zipFile = Files.createTempFile("enviroCarShapeExport-", ".zip");
        try (OutputStream outputStream = Files.newOutputStream(zipFile);
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            Files.walk(directory)
                 .filter(path -> !Files.isDirectory(path))
                 .forEach(path -> {
                     ZipEntry zipEntry = new ZipEntry(directory.relativize(path).toString());
                     try {
                         zipOutputStream.putNextEntry(zipEntry);
                         Files.copy(path, zipOutputStream);
                         zipOutputStream.closeEntry();
                     } catch (IOException e) {
                         throw new UncheckedIOException(e);
                     }
                 });
            return zipFile;
        }
    }

    private CoordinateReferenceSystem getCRS() {
        if (this.crs == null) {
            try {
                CRSAuthorityFactory authorityFactory = CRS.getAuthorityFactory(false);
                try {
                    this.crs = authorityFactory.createCoordinateReferenceSystem("EPSG:4326");
                } finally {
                    if (authorityFactory instanceof AbstractAuthorityFactory) {
                        ((AbstractAuthorityFactory) authorityFactory).dispose();
                    }
                }
            } catch (FactoryException e) {
                LOG.debug(e.getMessage());
            }
        }
        return this.crs;
    }

}
