package ua.itea.view.swing;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class MonochromePixels extends ArrayList<Point> {
	private Color color;
	
	public MonochromePixels(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}
