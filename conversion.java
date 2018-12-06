class Conversions {
	
	float[] RGBtoHSV(float[] rgb){

		float[] hsv = new float[3];
		float h, s, v;

		float r = rgb[0];
		float g = rgb[1];
		float b = rgb[2];
		
		float min, max, delta;
		min = MIN(r, g, b);
		max = MAX(r, g, b);
		delta = max - min;

		v = (max + min) / 2;

		if (delta == 0)
			h = s = 0;
		else {
			if (v < .5)
				s = delta / (max + min);
			else
				s = delta / (2 - max - min);

			float delta_r = (((max - r) / 6) + delta / 2) / delta;
			float delta_g = (((max - g) / 6) + delta / 2) / delta;
			float delta_b = (((max - b) / 6) + delta / 2) / delta;

			if (r == max)
				h = delta_b - delta_g;
			else if (g == max)
				h = (1.0 / 3.0) + delta_g - delta_r;
			else if (b == max)
				h = (2.0 / 3.0) + delta_g - delta_r;
		}

		hsv[0] = (h % 1) * 360;
		hsv[1] = s * 100;
		hsv[2] = v * 100;

		return hsv;
	}

	float complementary(float[] rgb){
		float[] hsv = rgb;
		return (hsv[0] + .5) % 1;
	}
}