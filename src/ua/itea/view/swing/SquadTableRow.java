package ua.itea.view.swing;

import java.awt.Color;

import ua.itea.model.Team.Squad;

public class SquadTableRow implements TableRow {
	private Boolean show;
	private Squad squad;
	
	public SquadTableRow(Squad squad) {
		this.show = true;
		this.squad = squad;
	}
	
	public Squad getSquad() {
		return squad;
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
		return squad.getName();
	}

	public void setName(String name) {
		squad.setName(name);
	}
	
	public void setName(Object name) {
		squad.setName((String) name);
	}

	public Color getColor() {
		return squad.getColor();
	}

	public void setColor(Color color) {
		squad.setColor(color);
	}
	
	public void setColor(Object color) {
		squad.setColor((Color) color);
	}

	public Integer getAlive() {
		return squad.getSize();
	}

	public Integer getDead() {
		return squad.getTotal() - squad.getSize();
	}
	
	public Integer getTotal() {
		return getAlive() + getDead();
	}
}
