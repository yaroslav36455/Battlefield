package ua.itea.model;

import ua.itea.model.environment.Environment;

public class Cell {
	private Unit unit;
	private Environment env;
	
	public Cell(Environment env, Unit unit) {
		this.env = env;
		this.unit = unit;
	}
	
	public Cell(Environment env) {
		this.env = env;
	}
	
	public boolean isFree() {
		return unit == null;
	}
	
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Environment getEnvironment() {
		return env;
	}
}
