package ua.itea.model;

import java.util.ArrayList;

public class Team {
	private static IdGenerator idGenerator = new IdGenerator();
	
	private int id;
	private String name;
	private Flag flag;
	private ArrayList<Squad> squads;
	
	public Team(String name, ArrayList<Squad> squads) {
		this.id = idGenerator.getNewId();
		this.name = name;
		this.squads = squads;
	}
	
	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getSize() {
		int size = 0;
		for (Squad squad : squads) {
			size += squad.getSize();
		}
		return size;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		} else {
			return id == ((Team) obj).id;
		}
	}
}
