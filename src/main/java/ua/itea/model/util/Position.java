package ua.itea.model.util;

public class Position {
	protected int x;
	protected int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public boolean equalTo(Position other) {
		return x == other.x && y == other.y;
	}
}
