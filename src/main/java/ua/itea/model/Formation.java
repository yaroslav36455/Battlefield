package ua.itea.model;

import java.awt.Color;

public abstract class Formation {
	private IdGenerator idGenerator;
	private int id;
	private String name;
	private Color color;
	
	public Formation(Formation outer) {
		this(outer.generateId());
	}
	
	public Formation(int id) {
		this.idGenerator = new IdGenerator();
		this.id = id;
	}
	
	protected int generateId() {
		return idGenerator.getNewId();
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public abstract int getSize();
	
	boolean equalTo(Formation formation) {
		return id == formation.id;
	}
}
