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
package org.envirocar.server.rest.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ShareImageRenderUtil {
	final String FONT_FILE = "segoeuib.ttf";
	final String DETAIL1 = "Max Speed";
	final String DETAIL2 = "Time";
	final String DETAIL3 = "Consumption";

	public synchronized BufferedImage process(BufferedImage old, String distance,
			String time, String consumption) {
		/*
		 * GraphicsEnvironment ge =
		 * GraphicsEnvironment.getLocalGraphicsEnvironment(); try {
		 * ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
		 * this.getClass().getClassLoader() .getResourceAsStream(FONT_FILE))); }
		 * catch (FontFormatException e) { e.printStackTrace(); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		int w = old.getWidth();
		int h = old.getHeight();
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();

		g2d.drawImage(old, 0, 0, null);
		float alpha = 0.7f;
		g2d.setPaint(new Color(4, 138, 191));
		AlphaComposite alcom = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, alpha);
		AlphaComposite alcom2 = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.7f);
		g2d.setComposite(alcom);
		g2d.fillRect(0, h - (h / 4), w,(h / 4));
		Font font = new Font("Segoe UI Bold", Font.BOLD, 28);
		g2d.setFont(font);
		g2d.setComposite(alcom2);
		g2d.setPaint(Color.white);
	    FontMetrics fm = g2d.getFontMetrics();
		//int y = h - (h / 4) + h / 8 - 20;
		
		int x1 = 0 + ((w/3 - fm.stringWidth(DETAIL1)) / 2);
		int x2 = w/3 + ((w/3 - fm.stringWidth(DETAIL2)) / 2);
		int x3 = 2*w/3 + ((w/3 - fm.stringWidth(DETAIL3)) / 2);
        int y = h - (h / 4) + ((((h / 4) - fm.getHeight()) / 2) + fm.getAscent()) - fm.getHeight()/2;

		//int y = (((3*h/4)-(25 + (h / 4))- fm.getHeight()) / 2) + fm.getAscent(); 
		g2d.drawString(DETAIL1, x1, y);
		g2d.drawString(DETAIL2, x2, y);
		g2d.drawString(DETAIL3, x3, y);
		g2d.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		FontMetrics fm2 = g2d.getFontMetrics();
		int distanceX = 0 + ((w/3 - fm2.stringWidth(distance)) / 2);
		int timeX = w/3 + ((w/3 - fm2.stringWidth(time)) / 2);
		int consumptionX = 2*w/3 + ((w/3 - fm2.stringWidth(consumption)) / 2);
        int textY = h - (h / 4) + ((((h / 4) - fm2.getHeight()) / 2) + fm2.getAscent()) + fm.getHeight()/2;
        g2d.drawString(distance, distanceX, textY);
		g2d.drawString(time, timeX, textY);
		g2d.drawString(consumption, consumptionX, textY);
		g2d.dispose();
		return img;
	}
}
