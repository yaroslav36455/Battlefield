package ua.itea.model;

import java.util.ArrayList;

import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.Position;

public class CombatUnit {
	private Team team;
	private Squad squad;
	private Unit unit;
	private Position position;
	private CardinalPoints direction;
	
	public void set(Unit unit, ArrayList<Unit> opponents) {
		this.unit = unit;
	}
}
