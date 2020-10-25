package ua.itea.view.swing;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class TableIntegerCellRenderer implements TableCellRenderer {
	
	private JLabel label;
	private Font bold;
	private Font regular;

	public TableIntegerCellRenderer() {
		label = new JLabel();
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setOpaque(true);
		
		Font defaultFont = label.getFont();
		regular = new Font(defaultFont.getName(), Font.PLAIN, defaultFont.getSize());
		bold = new Font(defaultFont.getName(), Font.BOLD, defaultFont.getSize());
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table,
												   Object value,
												   boolean isSelected,
												   boolean hasFocus,
												   int row, int column) {
		
		
		if (row == table.getRowCount() - 1) {
			label.setFont(bold);
		} else {
			label.setFont(regular);
		}
		
		if (isSelected) {
			label.setBackground(table.getSelectionBackground());	
		} else {
			label.setBackground(table.getBackground());
		}
		
		setValue(value);
		
		return label;
	}
	
	private void setValue(Object value) {
		label.setText((value == null) ? "" : value.toString());
	}
}
