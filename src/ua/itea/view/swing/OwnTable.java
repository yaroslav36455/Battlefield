package ua.itea.view.swing;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class OwnTable extends JTable {
	
	public OwnTable() {
		super(new OwnTableModel());
		setFocusable(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setReorderingAllowed(false);
		setFillsViewportHeight(true);
		getColumnModel().getColumn(0).setHeaderRenderer(new OwnTableCellHeaderRenderer());
		//getSelectionModel().addListSelectionListener(new TeamListSelectionListener());
		
		//setPreferredScrollableViewportSize(new Dimension(0, table.getRowHeight() * table.getRowCount()));
		getColumnModel().getColumn(Column.SHOW_OR_HIDE.ordinal()).setMinWidth(16);
		getColumnModel().getColumn(Column.SHOW_OR_HIDE.ordinal()).setMaxWidth(16);
		getColumnModel().getColumn(Column.COLOR.ordinal()).setMinWidth(50);
		getColumnModel().getColumn(Column.COLOR.ordinal()).setMaxWidth(50);
		
		setDefaultRenderer(Color.class, new TableColorCellRenderer());			
		setDefaultRenderer(Integer.class, new TableIntegerCellRenderer());
		setDefaultEditor(Color.class, new TableColorCellEditor());
	}
	
	public void add() {
		OwnTableModel tableModel = (OwnTableModel) getModel();
		
		boolean show = (int) (Math.random() * 2) == 0 ? false : true;
		String name = "Test Name";
		Color color = new Color((int) (Math.random() * 256),
								(int) (Math.random() * 256),
								(int) (Math.random() * 256));
		int alive = (int) (Math.random() * 5000);
		int dead = (int) (Math.random() * 5000);
		
		tableModel.addRow(new TableRow(show, name, color, alive, dead));
//		table.setPreferredScrollableViewportSize(new Dimension(0, table.getRowHeight() * table.getRowCount()));
		
		if (getSelectedRow() != -1) {
			/* single selection allowed */
			int index = this.getSelectedRows()[0];
			removeRowSelectionInterval(index, index);
		}
		setRowSelectionInterval(getLastOrdinaryRowIndex(), getLastOrdinaryRowIndex());
	}
	
	public void removeRow() {
		removeRow(getLastOrdinaryRowIndex());
	}
	
	public void removeRow(int index) {
		OwnTableModel tableModel = (OwnTableModel) getModel();
		
		tableModel.removeRow(index);
		
		if (index < getTotalRowIndex()) {
			setRowSelectionInterval(index, index);	
		} else if (index > 0) {
			index--;
			setRowSelectionInterval(index, index);
		}
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
	
	public JPanel makeScrollable() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.ipady = 200;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		
		panel.add(new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
										JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),
										gbc);
		panel.setBackground(Color.RED);
		return panel;
	}
}