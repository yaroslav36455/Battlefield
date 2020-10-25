package ua.itea.model;

import java.util.ArrayList;

public class Squad {
	private static IdGenerator idGenerator = new IdGenerator();
	
	private int id;
	private String name;
	private Flag flag;
	private int damage;
	private int defense;
	private ArrayList<Unit> units;
	
	public Squad(String name, int damage, int defense) {
		this.id = idGenerator.getNewId();
		this.name = name;
		this.damage = damage;
		this.defense = defense;
	}
	
	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return units.size();
	}
}
