package ua.itea.model.util;

public class MutablePosition extends Position {
	
	public MutablePosition(Position position) {
		super(position.x, position.y);
	}
	
	public MutablePosition() {
		super(0, 0);
	}

	public MutablePosition(int x, int y) {
		super(x, y);
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void move(CardinalPoints direction) {
		switch (direction) {
		case NORTH:
			y++;
			break;
			
		case SOUTH:
			y--;
			break;
			
		case WEST:
			x--;
			break;
			
		case EAST:
			x++;
			break;

		default:
			throw new RuntimeException("Missed statement in switch");
		}
	}
}
