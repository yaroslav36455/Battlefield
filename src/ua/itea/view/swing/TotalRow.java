package ua.itea.view.swing;

import java.awt.Color;
import java.util.ArrayList;

public class TotalRow implements TableRow {
	private Boolean show;
	private ArrayList<TableRow> ordinaryRows;
	
	public TotalRow(ArrayList<TableRow> ordinaryRows) {
		this.show = true;
		this.ordinaryRows = ordinaryRows;
	}

	@Override
	public Boolean getShow() {
		return show;
	}

	@Override
	public void setShow(Boolean show) {
		this.show = show;
	}

	@Override
	public void setShow(Object show) {
		this.show = (Boolean) show;
	}

	@Override
	public String getName() {
		return "Total";
	}

	@Override
	public void setName(String name) {
		/* empty */
	}

	@Override
	public void setName(Object name) {
		/* empty */
	}

	@Override
	public Color getColor() {
		return null;
	}

	@Override
	public void setColor(Color color) {
		/* empty */
	}

	@Override
	public void setColor(Object color) {
		/* empty */
	}

	@Override
	public Integer getAlive() {
		int alive = 0;
		for (TableRow tableRow : ordinaryRows) {
			alive += tableRow.getAlive();
		}
		
		return alive;
	}

	@Override
	public Integer getDead() {
		int dead = 0;
		for (TableRow tableRow : ordinaryRows) {
			dead += tableRow.getDead();
		}
		
		return dead;
	}

	@Override
	public Integer getTotal() {
		int total = 0;
		for (TableRow tableRow : ordinaryRows) {
			total += tableRow.getTotal();
		}

		return total;
	}

}
