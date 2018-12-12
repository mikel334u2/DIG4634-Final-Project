import javafx.scene.paint.Color;

class Conversions {
	
	public int extractColor(Bitmap bitmap, int radius){
		
		// Gets center coordinates
		int cx = bitmap.getWidth() / 2;
		int cy = bitmap.getHeight() / 2;
		
		// if the radius is 1, return the color int of the center pixel
		if (radius <= 1)
			return bitmap.getPixel(cx,cy);
		
		// Get the area of the reticle, no. of pixels to count is the area
		double area = Math.PI * radius * radius;
		int[][] rgb = new int[Math.ceil(area)][3];
		
		// Loop through a square and add these specific pixels to rgb array
		int i = 0;
		for (int x = cx - radius; x < cx + radius && i < rgb.length; x++){
			for (int y = cy - radius; y < cy + radius && i < rgb.length; y++){
				if (Math.hypot(cx - x, cy - y) >= radius)
					continue;
				int pixel = bitmap.getPixel(x,y);
				rgb[i][0] = Color.red(pixel);
				rgb[i][1] = Color.blue(pixel);
				rgb[i][2] = Color.green(pixel);
				i++;
			}
		}
		
		// Take the average of the colors in the array and return it
		int redAvg = 0;
		int blueAvg = 0;
		int greenAvg = 0;
		for (i = 0; i < rgb.length; i++){
			redAvg += rgb[i][0];
			blueAvg += rgb[i][1];
			greenAvg += rgb[i][2];
		}
		redAvg /= rgb.length;
		blueAvg /= rgb.length;
		greenAvg /= rgb.length;
		
		return Color.rgb(redAvg, greenAvg, blueAvg);
	}
	
	private double[] RGBtoHSL(int rgb){
		
		double h = 0;
		double s = 0;
		double l = 0;

		double r = Color.red(rgb) / 255;
		double g = Color.green(rgb) / 255;
		double b = Color.blue(rgb) / 255;
		
		double min = Math.min(r, Math.min(g, b));
		double max = Math.max(r, Math.max(g, b));
		double delta = max - min;

		l = (max + min) / 2;

		if (delta == 0)
			h = s = 0;
		else {
	        s = delta / (1 - Math.abs(2 * l - 1));
	        if (max == r)
	            h = ((g - b) / delta) % 6;
	        else if (max == g)
	        	h = (b - r) / delta + 2;
	        else if (max == b)
	        	h = (r - g) / delta + 4;
		}
		
		double[] hsl = {h * 60, s * 100, l * 100};
		return hsl;
	}
	
	private int HSLtoRGB(double[] hsl){
		
		double r = 0;
		double g = 0;
		double b = 0;
		double h = hsl[0];
		double s = hsl[1] / 100;
		double l = hsl[2] / 100;
		
		double delta = (1 - Math.abs(2 * l - 1)) * s;
		double x = delta * (1 - Math.abs((h / 60.0) % 2 - 1));
		double m = l - delta / 2.0;
		
		if (h < 60){
		    r = delta;
		    g = x;
		    b = 0;
		}
		else if (h < 120){
		    r = x;
		    g = delta;
		    b = 0;
		}
		else if (h < 180){
		    r = 0;
		    g = delta;
		    b = x;
		}
		else if (h < 240){
		    r = 0;
		    g = x;
		    b = delta;
		}
		else if (h < 300){
		    r = x;
		    g = 0;
		    b = delta;
		}
		else {
		    r = delta;
		    g = 0;
		    b = x;
		}
		    
		r = Math.round((r + m) * 255);
		g = Math.round((g + m) * 255);
		b = Math.round((b + m) * 255);
		
		return Color.rgb((int) r, (int) g, (int) b);
	}

	public int complementary(int rgb){
		
		double[] hsl = RGBtoHSL(rgb);
		hsl[0] = (hsl[0] + 180) % 360;
		return HSLtoRGB(hsl);
	}
}