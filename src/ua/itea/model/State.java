package ua.itea.model;

import java.util.ArrayList;

public class State {
	private Field<Cell> field;
	private ArrayList<Team> teams;
	private boolean hasBeenUpdated;
	
	public State(Field<Cell> field, ArrayList<Team> teams) {
		this.field = field;
		this.teams = teams;
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
