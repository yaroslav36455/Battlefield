package ua.itea.view.swing;

import java.awt.Color;

public enum Column {
	SHOW_OR_HIDE(Boolean.class),
	NAME(String.class),
	COLOR(Color.class),
	ALIVE(Integer.class),
	DEAD(Integer.class),
	TOTAL(Integer.class);
	
	private Class<?> clazz;
	
	private Column(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Class<?> getColumnClass() {
		return clazz;
	}
}
