package ua.itea.view.swing;

import java.awt.Point;
import java.util.ArrayList;

import ua.itea.model.Color;
import ua.itea.model.util.Position;

public class MonochromePixels extends ArrayList<Position> {
	private Color color;
	
	public MonochromePixels(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}
