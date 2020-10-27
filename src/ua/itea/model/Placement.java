package ua.itea.model;

import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.MutablePosition;
import ua.itea.model.util.Position;

public class Placement<T> {
	private T value;
	private MutablePosition position;
	private CardinalPoints direction;
	
	public Placement(T value, MutablePosition position) {
		this(value, position, CardinalPoints.random());
	}
	
	public Placement(T value, MutablePosition position, CardinalPoints direction) {
		this.value = value;
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
	
	public T get() {
		return value;
	}
}
