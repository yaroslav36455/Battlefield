package ua.itea.view.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class TableColorCellRenderer implements TableCellRenderer {
	private JPanel panel;
	private Border selectedBorder;
	private Border unselectedBorder;
	
	public TableColorCellRenderer() {
		panel = new JPanel();
		panel.setOpaque(true);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table,
												   Object value,
												   boolean isSelected,
												   boolean hasFocus,
												   int row, int column) {
		
		boolean totalRow = row == table.getRowCount() - 1;
		
		if (isSelected) {
			if (totalRow) {
				panel.setBackground(table.getSelectionBackground());
				panel.setBorder(null);
			} else {
				if (selectedBorder == null) {
		            selectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
		            		table.getSelectionBackground());
		        }
		        panel.setBorder(selectedBorder);	
			}	
		} else {
			if (totalRow) {
				panel.setBackground(table.getBackground());
				panel.setBorder(null);
			} else {
				if (unselectedBorder == null) {
					unselectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
							table.getBackground());
				}
				panel.setBorder(unselectedBorder);	
			}
		}
		
		if (value != null) {
			Color color = (Color) value;
			panel.setBackground(color);
			panel.setToolTipText("#" + Integer.toHexString(color.getRGB()).substring(2));
		}

		return panel;
	}
}
