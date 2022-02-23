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
package org.envirocar.server.rest.resources;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.envirocar.server.core.entities.Measurements;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.filter.MeasurementFilter;
import org.envirocar.server.core.filter.StatisticsFilter;
import org.envirocar.server.core.statistics.Statistic;
import org.envirocar.server.core.statistics.Statistics;
import org.envirocar.server.rest.MediaTypes;
import org.envirocar.server.rest.InternalServerError;
import org.envirocar.server.rest.util.OSMTileRenderer;
import org.envirocar.server.rest.util.TileRenderer;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ShareResource extends AbstractResource {
    public static final String LOCALE = "{locale}";
    private static final String DETAIL_TIME = "time";
    private static final String DETAIL_MAX_SPEED = "maxspeed";
    private static final String DETAIL_AVG_SPEED = "avgspeed";
    private static final String DETAIL_CALCULATED_MAF = "Calculated MAF";
    private static final String DETAIL_AVG_RPM = "RPM";
    private static final String DETAL_INTAKE_PRESSURE = "IntakePressure";
    private static final String DETAIL_TEMPERATURE = "IntakeTemperature";
    private static final String DETAIL_CONSUMPTION = "Consumption";
    private static final String DETAIL_CO2 = "CO2";
    private static final String PHENOMENON_CONSUMPTION = "Consumption";
    private static final String PHENOMENON_CO2 = "CO2";
    private static final String PHENOMENON_INTAKE_PRESSURE = "Intake Pressure";
    private static final String PHENOMENON_INTAKE_TEMPERATURE = "Intake Temperature";
    private static final String PHENOMENON_RPM = "Rpm";
    private static final String PHENOMENON_CALCULATED_MAF = "Calculated MAF";
    private static final String PHENOMENON_SPEED = "Speed";
    private static final String NA = "N/A";
    private static final String DEFAULT_LOCALE = "EN";
    private static final float ALPHA = 0.7f;
    private static final Font BOLD_FONT = new Font("Segoe UI Bold", Font.BOLD, 26);
    private static final Font FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Color RECTANGLE_COLOR = new Color(4, 138, 191);
    private static final Color FONT_COLOR = Color.white;
    private static final AlphaComposite RECTANGLE_ALPHA_COMPOSITE = AlphaComposite
            .getInstance(AlphaComposite.SRC_OVER, ALPHA);
    private static final AlphaComposite FONT_ALPHA_COMPOSITE = AlphaComposite
            .getInstance(AlphaComposite.SRC_OVER, ALPHA);

    private static final PeriodFormatter PERIOD_FORMATTER = new PeriodFormatterBuilder()
            .appendDays().appendSuffix("d")
            .appendSeparator(" ")
            .appendHours().appendSuffix("h")
            .appendSeparator(" ")
            .appendMinutes().appendSuffix("m")
            .appendSeparator(" ")
            .appendSeconds().appendSuffix("s")
            .toFormatter();

    private final Track track;

    @Inject
    public ShareResource(@Assisted Track track) {
        this.track = track;
    }

    @GET
    @Produces(MediaTypes.PNG)
    public Response getShareImage() {
        return getLocalizedShareImage(DEFAULT_LOCALE);
    }

    @GET
    @Path(LOCALE)
    @Produces(MediaTypes.PNG)
    public Response getLocalizedShareImage(@PathParam("locale") String locale) {
        try {
            BufferedImage image;
            Statistics statistics = getStatistics();
            TileRenderer renderer = new OSMTileRenderer();

            if (renderer.imageExists(track.getIdentifier())) {
                image = renderer.loadImage(track.getIdentifier());
            } else {
                Measurements measurements = getMeasurements();
                image = renderer.createImage(measurements);
                image = renderer.clipImage(image, measurements, 768, 512, 1);
                renderer.saveImage(image, track.getIdentifier());
            }

            Map<String, String> hm = getDetails(track, statistics);
            image = process(image, hm, locale);
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                ImageIO.write(image, "png", os);
                byte[] imageData = os.toByteArray();
                return Response.ok(imageData).build();
            }

        } catch (IOException e) {
            throw new InternalServerError(e);
        }
    }

    /**
     * This method draws the statistics on a bufferedimage when the statistics are passed along with the old image.
     *
     * @param old     The original image.
     * @param details The track details.
     * @param locale  The locale.
     * @return BufferedImage statistics drawn image
     */
    private BufferedImage process(BufferedImage old, Map<String, String> details, String locale) {
        int w = old.getWidth();
        int h = old.getHeight();
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        g2d.drawImage(old, 0, 0, null);

        g2d.setPaint(RECTANGLE_COLOR);
        g2d.setComposite(RECTANGLE_ALPHA_COMPOSITE);
        g2d.fillRect(0, h - (h / 4), w, (h / 4));

        g2d.setFont(BOLD_FONT);
        g2d.setComposite(FONT_ALPHA_COMPOSITE);
        g2d.setPaint(FONT_COLOR);

        ResourceBundle bundle = getBundle(locale);

        FontMetrics fm = g2d.getFontMetrics();

        int x1 = ((w / 3 - fm.stringWidth(bundle.getString(DETAIL_MAX_SPEED))) / 2);
        int x2 = w / 3 + ((w / 3 - fm.stringWidth(bundle.getString(DETAIL_TIME))) / 2);
        int x3 = 2 * w / 3 + ((w / 3 - fm.stringWidth(bundle.getString(PHENOMENON_CONSUMPTION))) / 2);
        int y = h - (h / 4) + ((((h / 4) - fm.getHeight()) / 2) + fm.getAscent()) - fm.getHeight() / 2;
        g2d.drawString(bundle.getString(DETAIL_MAX_SPEED), x1, y);
        g2d.drawString(bundle.getString(DETAIL_TIME), x2, y);
        g2d.drawString(bundle.getString(PHENOMENON_CONSUMPTION), x3, y);

        g2d.setFont(FONT);
        FontMetrics fm2 = g2d.getFontMetrics();
        int maxspeedX = ((w / 3 - fm2.stringWidth(details.get(DETAIL_MAX_SPEED))) / 2);
        int timeX = w / 3 + ((w / 3 - fm2.stringWidth(details.get(DETAIL_TIME))) / 2);
        int consumptionX = 2 * w / 3 + ((w / 3 - fm2.stringWidth(details.get(PHENOMENON_CONSUMPTION))) / 2);
        int textY = h - (h / 4) + ((((h / 4) - fm2.getHeight()) / 2) + fm2.getAscent()) + fm.getHeight() / 2;
        g2d.drawString(details.get(DETAIL_MAX_SPEED), maxspeedX, textY);
        g2d.drawString(details.get(DETAIL_TIME), timeX, textY);
        g2d.drawString(details.get(PHENOMENON_CONSUMPTION), consumptionX, textY);
        g2d.dispose();
        return img;
    }


    /**
     * This method extracts statistics from statistics object into a HashMap
     *
     * @param track      The track
     * @param statistics The statistics
     * @return HashMap which contains statistic name as the key and statistics value as the value
     */
    private Map<String, String> getDetails(Track track, Statistics statistics) {
        Map<String, String> hm = new HashMap<>(9);
        hm.put(DETAIL_TIME, PERIOD_FORMATTER.print(new Period(track.getBegin(), track.getEnd())));
        hm.put(DETAIL_MAX_SPEED, NA);
        hm.put(DETAIL_AVG_SPEED, NA);
        hm.put(DETAIL_CALCULATED_MAF, NA);
        hm.put(DETAIL_AVG_RPM, NA);
        hm.put(DETAL_INTAKE_PRESSURE, NA);
        hm.put(DETAIL_TEMPERATURE, NA);
        hm.put(PHENOMENON_CONSUMPTION, NA);
        hm.put(PHENOMENON_CO2, NA);

        for (Statistic statistic : statistics) {
            String unit = statistic.getPhenomenon().getUnit();
            String avg = String.format("%.2f %s", statistic.getMean(), unit);
            String max = String.format("%.2f %s", statistic.getMax(), unit);

            if (statistic.getPhenomenon().getName().equalsIgnoreCase(PHENOMENON_SPEED)) {
                hm.put(DETAIL_MAX_SPEED, max);
                hm.put(DETAIL_AVG_SPEED, avg);
            } else if (statistic.getPhenomenon().getName().equalsIgnoreCase(PHENOMENON_CALCULATED_MAF)) {
                hm.put(DETAIL_CALCULATED_MAF, avg);
            } else if (statistic.getPhenomenon().getName().equalsIgnoreCase(PHENOMENON_RPM)) {
                hm.put(DETAIL_AVG_RPM, avg);
            } else if (statistic.getPhenomenon().getName().equalsIgnoreCase(PHENOMENON_INTAKE_TEMPERATURE)) {
                hm.put(DETAL_INTAKE_PRESSURE, avg);
            } else if (statistic.getPhenomenon().getName().equalsIgnoreCase(PHENOMENON_INTAKE_PRESSURE)) {
                hm.put(DETAIL_TEMPERATURE, avg);
            } else if (statistic.getPhenomenon().getName().equalsIgnoreCase(PHENOMENON_CONSUMPTION)) {
                hm.put(DETAIL_CONSUMPTION, avg);
            } else if (statistic.getPhenomenon().getName().equalsIgnoreCase(PHENOMENON_CO2)) {
                hm.put(DETAIL_CO2, avg);
            }
        }

        return hm;
    }

    private ResourceBundle getBundle(String locale) {
        if (locale.equalsIgnoreCase("de")) {
            return getBundle(Locale.GERMANY); // localize
        } else {
            return getBundle(Locale.ROOT);
        }
    }

    private ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("ApplicationMessages", locale);

    }

    private Statistics getStatistics() {
        return getStatisticsService().getStatistics(new StatisticsFilter(track));
    }

    private Measurements getMeasurements() {
        return getDataService().getMeasurements(new MeasurementFilter(track));
    }
}
