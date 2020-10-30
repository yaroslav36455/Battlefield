package ua.itea.model;

import java.awt.Color;
import java.util.Objects;

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
	
	public int generateId() {
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Formation other = (Formation) obj;
		return id == other.id;
	}
	
	
}
