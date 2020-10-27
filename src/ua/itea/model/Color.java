package ua.itea.model;

public class Color {
	private float red;
	private float green;
	private float blue;
	
	public static Color random() {
		return new Color((int) (Math.random() * Integer.MAX_VALUE));
	}
	
	public Color(int color) {
		red = ((color >> 16) & 255) / 255.f;
		green = ((color >> 8) & 255) / 255.f;
		blue = (color & 255) / 255.f;
	}
	
	public Color(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int get() {
		int color = 0;
		
		color |= ((int) (red * 255.f)) << 16;
		color |= ((int) (green * 255.f)) << 8;
		color |= ((int) (blue * 255.f));
		return color;
	}

	public float red() {
		return red; 
	}
	
	public float green() {
		return green;
	}
	
	public float blue() {
		return blue;
	}

	@Override
	public String toString() {
		return "Color [get()=" + Integer.toString(get(), 16) + ", red()=" + red() + ", green()=" + green() + ", blue()=" + blue() + "]";
	}
	
	
}
