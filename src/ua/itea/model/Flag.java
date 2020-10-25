package ua.itea.model;

public class Flag {
	private int color;	
	
	public Flag(int color) {
		this.color = color;
	}
	
	public int get() {
		return color;
	}

	public float red() {
		return ((color >> 16) & 255) / 255.f; 
	}
	
	public float green() {
		return ((color >> 8) & 255) / 255.f;
	}
	
	public float blue() {
		return (color & 255) / 255.f;
	}
}
