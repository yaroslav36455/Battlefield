package ua.itea.view.swing;

import java.awt.Color;

import ua.itea.model.Team.Squad;

public class SquadTableRow implements TableRow {
	private Squad squad;
	
	public SquadTableRow(Squad squad) {
		this.squad = squad;
	}
	
	public Squad getSquad() {
		return squad;
	}

	@Override
	public String getName() {
		return squad.getName();
	}

	@Override
	public void setName(String name) {
		squad.setName(name);
	}
	
	@Override
	public void setName(Object name) {
		squad.setName((String) name);
	}

	public Color getColor() {
		return squad.getColor();
	}

	@Override
	public void setColor(Color color) {
		squad.setColor(color);
	}
	
	@Override
	public void setColor(Object color) {
		squad.setColor((Color) color);
	}

	@Override
	public Integer getAlive() {
		return squad.getSize();
	}

	@Override
	public Integer getDead() {
		return squad.getTotal() - squad.getSize();
	}
	
	@Override
	public Integer getTotal() {
		return getAlive() + getDead();
	}
}
