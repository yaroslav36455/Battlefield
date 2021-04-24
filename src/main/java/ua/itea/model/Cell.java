package ua.itea.model;

import ua.itea.model.Team.Squad.Unit;

public class Cell {
	private Unit unit;
	
	public Cell(Unit unit) {
		this.unit = unit;
	}
	
	public boolean hasUnit() {
		return unit != null;
	}
	
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
}
