package ua.itea.model;

import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.Position;

public class DisplacementCell {
	private Position position;
	private CardinalPoints direction;
	private long visitId;
	
	public DisplacementCell(Position position, long visitId) {
		this.position = position;
		this.visitId = visitId;
		this.direction = CardinalPoints.NORTH;
	}
	
	public CardinalPoints getDirection() {
		return direction;
	}

	public void setDirection(CardinalPoints direction) {
		this.direction = direction;
	}

	public Position getPosition() {
		return position;
	}
	
	public boolean isVisited(long visitId) {
		return this.visitId == visitId;
	}
	
	public void visit(long visitId) {
		this.visitId = visitId;
	}
}
