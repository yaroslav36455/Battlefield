package ua.itea.view.swing;

import java.awt.Color;

public class TableRow {
	private Boolean show;
	private String name;
	private Color color;
	private Integer alive;
	private Integer dead;
	
	public TableRow(Boolean show, String name, Color color, Integer alive, Integer dead) {
		this.show = show;
		this.name = name;
		this.color = color;
		this.alive = alive;
		this.dead = dead;
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
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setName(Object name) {
		this.name = (String) name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setColor(Object color) {
		this.color = (Color) color;
	}

	public Integer getAlive() {
		return alive;
	}

	public void setAlive(Integer alive) {
		this.alive = alive;
	}
	
	public void setAlive(Object alive) {
		this.alive = (Integer) alive;
	}

	public Integer getDead() {
		return dead;
	}

	public void setDead(Integer dead) {
		this.dead = dead;
	}
	
	public void setDead(Object dead) {
		this.dead = (Integer) dead;
	}
	
	public Integer getTotal() {
		return getAlive() + getDead();
	}
}
