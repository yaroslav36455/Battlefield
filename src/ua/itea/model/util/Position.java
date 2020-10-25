package ua.itea.model.util;

import java.util.Objects;

public class Position {
	protected int x;
	protected int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isNeighbour(Position other) {
		int xDiff = Math.abs(x - other.x);
		int yDiff = Math.abs(y - other.y);
		
		return xDiff == 1 && yDiff == 0
				|| xDiff == 0 && yDiff == 1;
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

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Position other = (Position) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "Position [getX()=" + getX() + ", getY()=" + getY() + "]";
	}
}
