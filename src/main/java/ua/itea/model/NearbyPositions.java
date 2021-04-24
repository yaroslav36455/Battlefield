package ua.itea.model;

import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.MutablePosition;
import ua.itea.model.util.Position;

public class NearbyPositions {
	private MutablePosition[] neighbours;
	
	public NearbyPositions() {
		neighbours = new MutablePosition[CardinalPoints.values().length];
		for (int i = 0; i < neighbours.length; i++) {
			neighbours[i] = new MutablePosition();
		}
	}
	
	public void setPosition(Position position) {
		neighbours[CardinalPoints.NORTH.ordinal()].setX(position.getX());
		neighbours[CardinalPoints.NORTH.ordinal()].setY(position.getY() + 1);
		
		neighbours[CardinalPoints.WEST.ordinal()].setX(position.getX() - 1);
		neighbours[CardinalPoints.WEST.ordinal()].setY(position.getY());
		
		neighbours[CardinalPoints.EAST.ordinal()].setX(position.getX() + 1);
		neighbours[CardinalPoints.EAST.ordinal()].setY(position.getY());
		
		neighbours[CardinalPoints.SOUTH.ordinal()].setX(position.getX());
		neighbours[CardinalPoints.SOUTH.ordinal()].setY(position.getY() - 1);
	}
	
	public Position getPosition(CardinalPoints direction) {
		return neighbours[direction.ordinal()];
	}
}
