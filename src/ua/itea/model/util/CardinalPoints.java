package ua.itea.model.util;

public enum CardinalPoints {
	NORTH, SOUTH, WEST, EAST;
	
	private static CardinalPoints[][] permuted = {
			{ CardinalPoints.NORTH, CardinalPoints.EAST,  CardinalPoints.SOUTH, CardinalPoints.WEST },
			{ CardinalPoints.NORTH, CardinalPoints.EAST,  CardinalPoints.WEST,  CardinalPoints.SOUTH },
			{ CardinalPoints.NORTH, CardinalPoints.SOUTH, CardinalPoints.EAST,  CardinalPoints.WEST },
			{ CardinalPoints.NORTH, CardinalPoints.SOUTH, CardinalPoints.WEST,  CardinalPoints.EAST },
			{ CardinalPoints.NORTH, CardinalPoints.WEST,  CardinalPoints.SOUTH, CardinalPoints.EAST },
			{ CardinalPoints.NORTH, CardinalPoints.WEST,  CardinalPoints.EAST,  CardinalPoints.SOUTH },
			
			{ CardinalPoints.EAST, CardinalPoints.NORTH, CardinalPoints.SOUTH, CardinalPoints.WEST },
			{ CardinalPoints.EAST, CardinalPoints.NORTH, CardinalPoints.WEST,  CardinalPoints.SOUTH },
			{ CardinalPoints.EAST, CardinalPoints.SOUTH, CardinalPoints.NORTH, CardinalPoints.WEST },
			{ CardinalPoints.EAST, CardinalPoints.SOUTH, CardinalPoints.WEST,  CardinalPoints.NORTH },
			{ CardinalPoints.EAST, CardinalPoints.WEST,  CardinalPoints.NORTH, CardinalPoints.SOUTH },
			{ CardinalPoints.EAST, CardinalPoints.WEST,  CardinalPoints.SOUTH, CardinalPoints.NORTH },
			
			{ CardinalPoints.SOUTH, CardinalPoints.NORTH, CardinalPoints.WEST,  CardinalPoints.EAST },
			{ CardinalPoints.SOUTH, CardinalPoints.NORTH, CardinalPoints.EAST,  CardinalPoints.WEST },
			{ CardinalPoints.SOUTH, CardinalPoints.EAST,  CardinalPoints.NORTH, CardinalPoints.WEST },
			{ CardinalPoints.SOUTH, CardinalPoints.EAST,  CardinalPoints.WEST,  CardinalPoints.NORTH },
			{ CardinalPoints.SOUTH, CardinalPoints.WEST,  CardinalPoints.NORTH, CardinalPoints.EAST },
			{ CardinalPoints.SOUTH, CardinalPoints.WEST,  CardinalPoints.EAST,  CardinalPoints.NORTH },
			
			{ CardinalPoints.WEST, CardinalPoints.NORTH, CardinalPoints.EAST,  CardinalPoints.SOUTH },
			{ CardinalPoints.WEST, CardinalPoints.NORTH, CardinalPoints.SOUTH, CardinalPoints.EAST },
			{ CardinalPoints.WEST, CardinalPoints.EAST,  CardinalPoints.NORTH, CardinalPoints.SOUTH },
			{ CardinalPoints.WEST, CardinalPoints.EAST,  CardinalPoints.SOUTH, CardinalPoints.NORTH },
			{ CardinalPoints.WEST, CardinalPoints.SOUTH, CardinalPoints.NORTH, CardinalPoints.EAST },
			{ CardinalPoints.WEST, CardinalPoints.SOUTH, CardinalPoints.EAST,  CardinalPoints.NORTH },
		};
	
	public static CardinalPoints random() {
		CardinalPoints[] dirs = CardinalPoints.values();
		return dirs[(int) (Math.random() * dirs.length)];
	}
	
	public static CardinalPoints[] randomArray() {
		return permuted[(int) (Math.random() * permuted.length)];
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
