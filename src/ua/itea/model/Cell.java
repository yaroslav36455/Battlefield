package ua.itea.model;

import ua.itea.model.Team.Squad.Unit;
import ua.itea.model.environment.Environment;

public class Cell {
	private Placement<Unit> unit;
	private Environment env;
	
	public Cell(Environment env, Placement<Unit> unit) {
		this.env = env;
		this.unit = unit;
	}
	
	public Cell(Environment env) {
		this.env = env;
	}
	
	public boolean hasUnit() {
		return unit != null;
	}
	
	public Placement<Unit> getUnit() {
		return unit;
	}

	public void setUnit(Placement<Unit> unit) {
		this.unit = unit;
	}

	public Environment getEnvironment() {
		return env;
	}
}
