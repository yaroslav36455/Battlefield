package ua.itea.model.util;

public class Rectangle {
	private Position pos;
	private Size size;
	
	public Rectangle(Position pos, Size size) {
		this.pos = pos;
		this.size = size;
	}
	
	public boolean isWithin(Position point) {
		return !(isOutOfWidth(point.getX()) || isOutOfHeight(point.getY()));
	}
	
	public Position getPosition() {
		return pos;
	}
	
	public Size getSize() {
		return size;
	}
	
	private boolean isOutOfWidth(int x) {
		return pos.getX() > x || pos.getX() + size.getWidth() < x;
	}
	
	private boolean isOutOfHeight(int y) {
		return pos.getY() > y || pos.getY() + size.getHeight() < y;
	}
}
