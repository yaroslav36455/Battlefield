package ua.itea.model;

import java.util.ArrayList;

public class State {
	private Field<Cell> field;
	private ArrayList<Team> teams;
	private ArrayList<Placement<Unit>> units;
	private boolean hasBeenUpdated;
	
	public State(Field<Cell> field, ArrayList<Placement<Unit>> units) {
		this.field = field;
		this.units = units;
	}
	
	public boolean hasBeenUpdated() {
		boolean tmp = hasBeenUpdated;
		hasBeenUpdated = false;
		return tmp;
	}
	
	public void add(Team team, Squad ...squads) {
		//TODO
	}
	
	public void add(int teamId, Squad squad, Squad ...squads) {
		//TODO		
	}
	
	public Field<Cell> getField() {
		return field;
	}
	
	public ArrayList<Placement<Unit>> getUnits() {
		return units;
	}
	
	public void setUnits(ArrayList<Placement<Unit>> units) {
		this.units = units;
	}
	
//	public static State load() {
//		//TODO
//	}
	
	public void save() {
		//TODO
	}
	
	public int numberOfTeams() {
		return teams.size();
	}
}
