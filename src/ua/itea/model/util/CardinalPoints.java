package ua.itea.model.util;

public enum CardinalPoints {
	NORTH, SOUTH, WEST, EAST;
	
	public static CardinalPoints random() {
		CardinalPoints[] dirs = CardinalPoints.values();
		return dirs[(int) (Math.random() * dirs.length)];
	}
	
	public CardinalPoints oposite() {
		switch (this) {
		case NORTH:
			return SOUTH;

		case SOUTH:
			return NORTH;
			
		case WEST:
			return EAST;
			
		case EAST:
			return WEST;
			
		default:
			throw new RuntimeException("Missed statement in switch");
		}
	}
}
