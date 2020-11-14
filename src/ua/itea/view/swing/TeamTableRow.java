package ua.itea.view.swing;

import java.awt.Color;

import ua.itea.model.Team;
import ua.itea.model.Team.Squad;

public class TeamTableRow implements TableRow {
	private Team team;
	
	public TeamTableRow(Team team) {
		this.team = team;
	}
	
	public Team getTeam() {
		return team;
	}

	@Override
	public String getName() {
		return team.getName();
	}

	@Override
	public void setName(String name) {
		team.setName(name);
	}
	
	@Override
	public void setName(Object name) {
		team.setName((String) name);
	}

	@Override
	public Color getColor() {
		return team.getColor();
	}

	@Override
	public void setColor(Color color) {
		team.setColor(color);
	}
	
	@Override
	public void setColor(Object color) {
		team.setColor((Color) color);
	}

	@Override
	public Integer getAlive() {
		int alive = 0;
		for (Squad squad : team) {
			alive += squad.getSize();
		}
		
		return alive;
	}

	@Override
	public Integer getDead() {
		int dead = 0;
		
		for (Squad squad : team) {
			dead += squad.getTotal() - squad.getSize();
		}
		
		return dead;
	}
	
	@Override
	public Integer getTotal() {
		return getAlive() + getDead();
	}
}
