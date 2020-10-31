package ua.itea.view.swing;

import java.awt.Color;

import ua.itea.model.Team;
import ua.itea.model.Team.Squad;

public class TeamTableRow implements TableRow {
	private Boolean show;
	private Team team;
	
	public TeamTableRow(Team team) {
		this.show = true;
		this.team = team;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}
	
	public void setShow(Object show) {
		this.show = (Boolean) show;
	}

	public String getName() {
		return team.getName();
	}

	public void setName(String name) {
		team.setName(name);
	}
	
	public void setName(Object name) {
		team.setName((String) name);
	}

	public Color getColor() {
		return team.getColor();
	}

	public void setColor(Color color) {
		team.setColor(color);
	}
	
	public void setColor(Object color) {
		team.setColor((Color) color);
	}

	public Integer getAlive() {
		int alive = 0;
		for (Squad squad : team) {
			alive += squad.getSize();
		}
		
		return alive;
	}

	public Integer getDead() {
		int dead = 0;
		
		for (Squad squad : team) {
			dead += squad.getTotal() - squad.getSize();
		}
		
		return dead;
	}
	
	public Integer getTotal() {
		return getAlive() + getDead();
	}
}
