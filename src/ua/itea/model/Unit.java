package ua.itea.model;

public class Unit {
	private int id;
	private Squad squad;
	private float health;
	
	public Unit(Squad squad, float health) {
		this.id = squad.generateId();
		this.squad = squad;
		this.health = health;
	}
	
	public int getId() {
		return id;
	}
	
	public Squad getSquad() {
		return squad;
	}
	
	public void setSquad(Squad squad) {
		this.squad = squad;
	}
	
	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}
	
	public Unit copy() {
		return new Unit(squad, health);
	}
}
