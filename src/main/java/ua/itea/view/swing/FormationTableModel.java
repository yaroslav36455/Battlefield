package ua.itea.view.swing;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class FormationTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private Object[] header = { "Name", "Color", "Alive", "Dead", "Total" };
	private ArrayList<TableRow> ordinaryRows;
	private TableRow totalRow;
	
	public FormationTableModel() {
		ordinaryRows = new ArrayList<>();
		totalRow = new TotalRow(ordinaryRows);
	}
	
	public TableRow getRow(int index) {
		return ordinaryRows.get(index);
	}
	
	public void update() {
		fireTableRowsUpdated(0, ordinaryRows.size() + 1);
	}
	
	public void addRow(TableRow tableRow) {
		ordinaryRows.add(tableRow);
		update();
	}
	
	public void addRow(int rowIndex, TableRow tableRow) {
		ordinaryRows.add(rowIndex, tableRow);
		update();
	}
	
	public TableRow removeRow() {
		return removeRow(ordinaryRows.size() - 1);
	}
	
	public TableRow removeRow(int rowIndex) {
		TableRow row = ordinaryRows.remove(rowIndex);
		
		update();
		return row;
	}
	
	public void clear() {
		ordinaryRows.clear();
		update();
	}
	
	@Override
	public int getRowCount() {
		return ordinaryRows.size() + 1;
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
		TableRow tableRow = rowIndex != ordinaryRows.size()
							? ordinaryRows.get(rowIndex)
							: totalRow;
		
		switch (Column.values()[columnIndex]) {
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
    	TableRow tableRow = rowIndex != ordinaryRows.size()
							? ordinaryRows.get(rowIndex)
							: totalRow;
    	
		switch (Column.values()[columnIndex]) {
		case NAME:
			tableRow.setName(value);
			break;
		case COLOR:
			tableRow.setColor(value);
			break;
		case ALIVE:
		case DEAD:
			/* installation of alive and dead is not allowed */
			
		case TOTAL:
			/* total computed automotically from alive and dead */
			
		default:
			throw new RuntimeException("Missed column in switch");
		}
    
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
    	return col < 3 && row < ordinaryRows.size() ||
    			col == 0 && row == ordinaryRows.size();
    }
}
