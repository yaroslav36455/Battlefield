package ua.itea.view.swing;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class FormationTable extends JTable {
	private static final long serialVersionUID = 1L;

	public FormationTable() {
		super(new FormationTableModel());
		setFocusable(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setReorderingAllowed(false);
		setFillsViewportHeight(true);
		
		getColumnModel().getColumn(Column.COLOR.ordinal()).setMinWidth(50);
		getColumnModel().getColumn(Column.COLOR.ordinal()).setMaxWidth(50);

		setDefaultRenderer(Color.class, new TableColorCellRenderer());
		setDefaultRenderer(Integer.class, new TableIntegerCellRenderer());
		setDefaultEditor(Color.class, new TableColorCellEditor());
	}

	public void update() {
		((FormationTableModel) getModel()).update();
	}

	public void add(TableRow tableRow) {
		FormationTableModel tableModel = (FormationTableModel) getModel();

		tableModel.addRow(tableRow);

		if (getSelectedRow() != -1) {
			/* single selection allowed */
			int index = this.getSelectedRows()[0];
			
			removeRowSelectionInterval(index, index);
		}
		setRowSelectionInterval(getLastOrdinaryRowIndex(), getLastOrdinaryRowIndex());
	}

	public TableRow removeRow() {
		return removeRow(getLastOrdinaryRowIndex());
	}

	public TableRow removeRow(int index) {
		FormationTableModel tableModel = (FormationTableModel) getModel();
		TableRow row = tableModel.removeRow(index);

		if (index < getTotalRowIndex()) {
			setRowSelectionInterval(index, index);
		} else {
			if (index > 0) {
				index--;
				setRowSelectionInterval(index, index);
			} else {
				removeRowSelectionInterval(index, index);
			}
		}

		return row;
	}

	public TableRow getRow(int index) {
		FormationTableModel tableModel = (FormationTableModel) getModel();

		return tableModel.getRow(index);
	}

	public boolean isTotalRow(int index) {
		return index == getTotalRowIndex();
	}

	public int getTotalRowIndex() {
		return getRowCount() - 1;
	}

	public int getOrdinaryRowCount() {
		return getRowCount() - 1;
	}

	public boolean isOrdinaryRow(int index) {
		return index >= 0 && index <= getLastOrdinaryRowIndex();
	}

	public int getLastOrdinaryRowIndex() {
		return getTotalRowIndex() - 1;
	}

	public boolean hasOrdinaryRows() {
		return getOrdinaryRowCount() > 0;
	}

	public boolean isSelectedOrdinaryRow() {
		int index = getSelectedRow();
		
		return index != -1 && index != getTotalRowIndex();
	}

	public void clear() {
		((FormationTableModel) getModel()).clear();
	}

	public JPanel makeScrollable() {
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridLayout());
		panel.add(new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
									  	JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		return panel;
	}
}
