package ua.itea.model;

import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.MutablePosition;
import ua.itea.model.util.Position;

public class Placement {
	private MutablePosition position;
	private CardinalPoints direction;
	
	public Placement(MutablePosition position) {
		this(position, CardinalPoints.random());
	}
	
	public Placement(MutablePosition position, CardinalPoints direction) {
		this.position = position;
		this.direction = direction;
	}
	
	public MutablePosition getPosition() {
		return position;
	}
	
	public CardinalPoints getDirection() {
		return direction;
	}
	
	public void setDirection(CardinalPoints direction) {
		this.direction = direction;
	}
}
