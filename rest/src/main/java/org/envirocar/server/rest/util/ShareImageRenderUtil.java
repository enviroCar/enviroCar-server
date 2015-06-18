package org.envirocar.server.rest.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ShareImageRenderUtil {
	final String FONT_FILE ="segoeuib.ttf";
	final String DETAIL1 = "Speed";
    final String DETAIL2 = "Time";
    final String DETAIL3 = "Consumption";
	
	public BufferedImage process(BufferedImage old,String distance,String time,String consumption) {
    	/*GraphicsEnvironment ge = 
    	         GraphicsEnvironment.getLocalGraphicsEnvironment();
    	    try {
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getClassLoader()
						.getResourceAsStream(FONT_FILE)));
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		int w = old.getWidth();
        int h = old.getHeight();
        BufferedImage img = new BufferedImage(
                w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.drawImage(old, 0, 0, null);
        float alpha = 0.7f;
        g2d.setPaint(new Color(4, 138, 191));
        AlphaComposite alcom = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, alpha);
        AlphaComposite alcom2 = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.7f);
        g2d.setComposite(alcom);
        g2d.fillRect(0, h-(h/4)-25, w, 25+(h/4)); 
        Font font = new Font("Segoe UI Bold",Font.BOLD, 28);
        g2d.setFont(font); 
        g2d.setComposite(alcom2);
        g2d.setPaint(Color.white);
        // FontMetrics fm = g2d.getFontMetrics(); 
        int y = h-(h/4)+ h/8 - 20;  
        int textY = h-(h/4)+ h/8 + 10; 
        g2d.drawString(DETAIL1, (w/6) - 70, y); 
        g2d.drawString(DETAIL2, (3*w/6) - 70, y); 
        g2d.drawString(DETAIL3, (5*w/6) - 120, y);
        g2d.setFont(new Font("Segoe UI",Font.BOLD, 16));
        g2d.drawString(distance, (w/6) - 70, textY); 
        g2d.drawString(time, (3*w/6) - 70, textY); 
        g2d.drawString(consumption, (5*w/6) - 70, textY);
        g2d.dispose(); 
        return img;
    }  
}
