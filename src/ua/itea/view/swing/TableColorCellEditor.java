package ua.itea.view.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class TableColorCellEditor extends AbstractCellEditor
								  implements TableCellEditor {
	private Color currentColor = Color.WHITE;
	private JColorChooser colorChooser;
	private JDialog dialog;
	private JButton button;
	
	public TableColorCellEditor() {
		button = new JButton();
		button.setBorderPainted(false);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				button.setBackground(currentColor);
				colorChooser.setColor(currentColor);
				dialog.setVisible(true);
				
				fireEditingStopped();
			}
		});
		
		ActionListener okPressed = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				currentColor = colorChooser.getColor();
			}
		};
		
		colorChooser = new JColorChooser();
		dialog = JColorChooser.createDialog(button,
											"Pick a Color",
											true,
											colorChooser,
											okPressed,
											null);
	}
	
	@Override
	public Object getCellEditorValue() {
		return currentColor;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table,
											     Object value,
											     boolean isSelected,
											     int row, int column) {
		currentColor = (Color) value;
		return button;
	}

}
