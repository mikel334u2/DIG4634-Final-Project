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
	
	private float[] RGBtoHSL(int rgb){
		
		float h, s, l;

		float r = Color.red(rgb) / 255;
		float g = Color.green(rgb) / 255;
		float b = Color.blue(rgb) / 255;
		
		float min = Math.min(r, Math.min(g, b));
		float max = Math.max(r, Math.max(g, b));
		float delta = max - min;

		l = (max + min) / 2;

		if (delta == 0)
			h = s = 0;
		else {
	        s = delta / (1 - Math.abs(2 * l - 1));
	        switch(max){
	        case r:
	        	int temp = (g - b) / delta;
	        	h = temp + (temp < 0) ? 6 : 0;
	        	break;
	        case g:
	        	h = (b - r) / delta + 2;
	        	break;
	        case b:
	        	h = (r - g) / delta + 4;
	        	break;
	        }
		}
		return {h * 60, s * 100, l * 100};
	}
	
	private int HSLtoRGB(float[] hsl){
		
		float r = 0;
		float g = 0;
		float b = 0;
		float h = hsl[0];
		float s = hsl[1];
		float l = hsl[2];
		
		if (s == 0)
			r = g = b = l;
		else {
	        float q = (l < 0.5) ? l * (1 + s) : l + s - l * s;
	        float p = 2 * l - q;
	        r = hueToRGB(p, q, h + 1/3);
	        g = hueToRGB(p, q, h);
	        b = hueToRGB(p, q, h - 1/3);
		}
		
		return Color.rgb(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
	}
	
	private float hueToRGB(float p, float q, float t){
		if (t < 0) t += 1;
		if (t > 1) t -= 1;
		if (t < 1/6) return p + (q - p) * 6 * t;
		if (t < 1/2) return q;
		if (t < 2/3) return p + (q - p) * (2 - 3 * t) * 2;
		return p;
	}

	public int complementary(int rgb){
		
		float[] hsl = RGBtoHSL(rgb);
		hsl[0] = (hsl[0] + 180) % 360;
		return HSLtoRGB(hsl);
	}
}