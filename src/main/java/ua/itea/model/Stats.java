package ua.itea.model;

public class Stats {
	private float health;
	private int damage;
	private int defence;
	private int velocity;
	
	public Stats(float health, int damage, int defence, int velocity) {
		this.health = health;
		this.damage = damage;
		this.defence = defence;
		this.velocity = velocity;
	}

	public float getHealth() {
		return health;
	}

	public int getDamage() {
		return damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getDefence() {
		return defence;
	}
	
	public void setDefence(int defence) {
		this.defence = defence;
	}
	
	public int getVelocity() {
		return velocity;
	}
	
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
}
