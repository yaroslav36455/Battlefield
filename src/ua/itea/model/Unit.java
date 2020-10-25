package ua.itea.model;

import ua.itea.model.util.CardinalPoints;

public class Unit implements Cloneable {
	private DogTag dogTag;
	private int health;
	private CardinalPoints direction;

	public Unit(DogTag dogTag, int health) {
		this(dogTag, health, CardinalPoints.random());
	}
	
	public Unit(DogTag dogTag, int health, CardinalPoints direction) {
		this.dogTag = dogTag;
		this.health = health;
		this.direction = direction;
	}
	
	public DogTag getDogTag() {
		return dogTag;
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public CardinalPoints getDirection() {
		return direction;
	}

	public void setDirection(CardinalPoints direction) {
		this.direction = direction;
	}
}
