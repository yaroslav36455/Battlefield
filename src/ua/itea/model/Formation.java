package ua.itea.model;

import java.util.Objects;

public abstract class Formation {
	private IdGenerator idGenerator;
	private int id;
	private String name;
	private Color flag;
	private int size;
	
	public Formation(Formation outer) {
		this(outer.generateId());
	}
	
	public Formation(int id) {
		this.idGenerator = new IdGenerator();
		this.id = id;
		this.size = 0;
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

	public Color getFlag() {
		return flag;
	}

	public void setFlag(Color flag) {
		this.flag = flag;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

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
