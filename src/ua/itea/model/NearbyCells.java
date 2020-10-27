package ua.itea.model;

import java.util.function.BiPredicate;

import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.Position;

public class NearbyCells {
	private Field<Cell> field;
	private Cell[] neighbours;
	private NearbyPositions nearbyPositions;
	
	public NearbyCells(Field<Cell> field) {
		this.field = field;
		neighbours = new Cell[CardinalPoints.values().length];
		nearbyPositions = new NearbyPositions();
	}
	
	public void setPosition(Position position) {		
		nearbyPositions.setPosition(position);
		
		for (CardinalPoints direction : CardinalPoints.values()) {
			Position nearbyPos = nearbyPositions.getPosition(direction);
			
			neighbours[direction.ordinal()] = field.isWithin(nearbyPos)
											  ? field.get(nearbyPos)
											  : null;
		}
	}
	
	public Cell getCell(CardinalPoints direction) {
		return neighbours[direction.ordinal()];
	}
}
