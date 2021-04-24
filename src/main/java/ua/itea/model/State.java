package ua.itea.model;

import java.util.ArrayList;

public class State {
	private int iteration;
	private Field<Cell> field;
	private ArrayList<Team> teams;
	
	public State(Field<Cell> field, ArrayList<Team> teams) {
		this.field = field;
		this.teams = teams;
	}
	
	public void iterate() {
		++iteration;
	}
	
	public int getIteration() {
		return iteration;
	}
	
	public ArrayList<Team> getTeams() {
		return teams;
	}
	
	public Field<Cell> getField() {
		return field;
	}
}
