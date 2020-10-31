package ua.itea.view.swing;

import java.awt.Color;

public interface TableRow {

	public Boolean getShow();

	public void setShow(Boolean show);
	public void setShow(Object show);

	public String getName();
	public void setName(String name);
	public void setName(Object name);

	public Color getColor();
	public void setColor(Color color);
	public void setColor(Object color);

	public Integer getAlive();
	public Integer getDead();
	public Integer getTotal();
}
