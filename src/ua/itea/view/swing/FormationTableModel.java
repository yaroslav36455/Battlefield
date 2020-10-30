package ua.itea.view.swing;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class FormationTableModel extends AbstractTableModel {
	private Object[] header = { new Boolean(false), "Name", "Color", "Alive", "Dead", "Total" };
	//private Object[] header = { true, false, true, false, true, true };
	//private Object[] header = { true, "Name", Color.BLUE, 435, 34, 1234 };
	private ArrayList<TableRow> data;
	
	public FormationTableModel() {
		data = new ArrayList<>();
		data.add(new TableRow(null, "Total", null, 0, 0));
		
//		addRow(new TableRow(true,  "A", Color.blue,  234, 4563));
//		addRow(new TableRow(true,  "B", Color.red,    12,   76));
//		addRow(new TableRow(false, "C", Color.green, 456,    7));
	}
	
	public void addRow(TableRow tableRow) {
		addRow(data.size() - 1, tableRow);
	}
	
	public void addRow(int rowIndex, TableRow tableRow) {
		int lastRowIndex = data.size() - 1;
		
		TableRow totalRow = data.get(lastRowIndex);
		totalRow.setAlive(totalRow.getAlive() + tableRow.getAlive());
		totalRow.setDead(totalRow.getDead() + tableRow.getDead());
		
		data.add(rowIndex, tableRow);
		fireTableRowsInserted(rowIndex, lastRowIndex);
	}
	
	public void removeRow() {
		removeRow(data.size() - 2);
	}
	
	public void removeRow(int rowIndex) {
		int lastRowIndex = data.size() - 1;
		TableRow toRemoveRow = data.get(rowIndex);
		TableRow totalRow = data.get(lastRowIndex);
		
		totalRow.setAlive(totalRow.getAlive() - toRemoveRow.getAlive());
		totalRow.setDead(totalRow.getDead() - toRemoveRow.getDead());
		
		data.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, lastRowIndex);
	}
	
	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return header.length;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return header[columnIndex].toString();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TableRow tableRow = data.get(rowIndex);
		
		switch (Column.values()[columnIndex]) {
		case SHOW_OR_HIDE:
			return tableRow.getShow();
		case NAME:
			return tableRow.getName();
		case COLOR:
			return tableRow.getColor();
		case ALIVE:
			return tableRow.getAlive();
		case DEAD:
			return tableRow.getDead();
		case TOTAL:
			return tableRow.getTotal();
		}
		
		throw new RuntimeException("Missed column in switch");
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Column.values()[columnIndex].getColumnClass();
	}
	
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
    	TableRow tableRow = data.get(rowIndex);
    	
		switch (Column.values()[columnIndex]) {
		case SHOW_OR_HIDE:
			tableRow.setShow(value);
			break;
		case NAME:
			tableRow.setName(value);
			break;
		case COLOR:
			tableRow.setColor(value);
			break;
		case ALIVE:
			tableRow.setAlive(value);
			break;
		case DEAD:
			tableRow.setDead(value);
			break;
		case TOTAL:
			/* total computed automotically from alive and dead */
			break;
			
		default:
			throw new RuntimeException("Missed column in switch");
		}
    
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
    	return col < 3 && row < data.size() - 1 ||
    			col == 0 && row == data.size() - 1;
    }
}
