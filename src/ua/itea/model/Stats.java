package ua.itea.model;

public class Stats {
	private int damage;
	private int defence;
	private int velocity;
	
	public Stats(int damage, int defence, int velocity) {
		this.damage = damage;
		this.defence = defence;
		this.velocity = velocity;
	}

	public int getDamage() {
		return damage;
	}
	
	public int getDefence() {
		return defence;
	}
	
	public int getVelocity() {
		return velocity;
	}
}
