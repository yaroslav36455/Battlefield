package ua.itea.model;

import java.util.ArrayList;

import ua.itea.model.Team.Squad;
import ua.itea.model.Team.Squad.Unit;

public class State {
	private int iteration;
	private Field<Cell> field;
	private ArrayList<Team> teams;
	private boolean hasBeenUpdated;
	
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
	
	public boolean hasBeenUpdated() {
		boolean tmp = hasBeenUpdated;
		hasBeenUpdated = false;
		return tmp;
	}
	
	public void addTeam(Team team) {
		teams.add(team);
	}
	
	public ArrayList<Team> getTeams() {
		return teams;
	}
	
	public void removeTeam(Team team) {
		for (Squad squad : team) {
			removeSquad(squad);
		}
		
		team.removeAllSquads();
	}
	
	public void addSquad(Squad squad) {
		//TODO		
	}
	
	public void removeSquad(Squad squad) {
//		ArrayList<Placement<Unit>> newArray = new ArrayList<>(units.size());
		
//		for (Unit unit : squad) {
//			
//		} 
//		
//		for (Placement<Unit> placementUnit : units) {
//			Squad currentSquad = placementUnit.get().getSquad();
//			
//			if (currentSquad.getId() == squad.getId()) {
//				field.get(placementUnit.getPosition()).setUnit(null);
//				currentSquad.setSize(currentSquad.getSize() - 1);
//			} else {
//				newArray.add(placementUnit);
//			}
//		}
//
//		units = newArray;
	}
	
	public Field<Cell> getField() {
		return field;
	}
	
//	public static State load() {
//		//TODO
//	}
	
//	public void save() {
//		//TODO
//	}
	
	public int numberOfTeams() {
		return teams.size();
	}
}
