package ua.itea.view.swing;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import sun.swing.DefaultLookup;
import sun.swing.table.DefaultTableCellHeaderRenderer;

public class FormationTableCellHeaderRenderer extends JCheckBox implements TableCellRenderer {
	
	//private JCheckBox checkBox;
	
	public FormationTableCellHeaderRenderer() {
		setEnabled(true);
	}
	
    public void setHorizontalTextPosition(int textPosition) {
        switch (textPosition) {
		case SwingConstants.LEFT:
			System.out.println("LEFT");
			break;
		case SwingConstants.CENTER:
			System.out.println("CENTER");
			break;
		case SwingConstants.RIGHT:
			System.out.println("RIGHT");
			break;
		case SwingConstants.LEADING:
			System.out.println("LEADING");
			break;
		case SwingConstants.TRAILING:
			System.out.println("TRAILING");
			break;

		default:
			System.out.println("<UNKNOWN>");
			break;
		}
        super.setHorizontalTextPosition(textPosition);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
    	
    	if (table == null) {
    		return this;
    	}
    	
//    	checkBox.setEnabled(checkBox.isEnabled() ? false : true);
//    	return checkBox;
    	//return super.getTableCellRendererComponent(table, checkBox, isSelected, hasFocus, row, column);
    	
        Icon sortIcon = null;

        boolean isPaintingForPrint = false;

        if (table != null) {
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                Color fgColor = null;
                Color bgColor = null;
                if (hasFocus) {
                    fgColor = DefaultLookup.getColor(this, ui, "TableHeader.focusCellForeground");
                    bgColor = DefaultLookup.getColor(this, ui, "TableHeader.focusCellBackground");
                }
                if (fgColor == null) {
                    fgColor = header.getForeground();
                }
                if (bgColor == null) {
                    bgColor = header.getBackground();
                }
                setForeground(fgColor);
                setBackground(bgColor);

                setFont(header.getFont());

                isPaintingForPrint = header.isPaintingForPrint();
            }
            
            if (!isPaintingForPrint && table.getRowSorter() != null) {
//                if (!horizontalTextPositionSet) {
//                    // There is a row sorter, and the developer hasn't
//                    // set a text position, change to leading.
//                    setHorizontalTextPosition(JLabel.LEADING);
//                }
            	//setHorizontalTextPosition(JLabel.CENTER);
            	setHorizontalAlignment(SwingConstants.CENTER);
                SortOrder sortOrder = getColumnSortOrder(table, column);
                if (sortOrder != null) {
                    switch(sortOrder) {
                    case ASCENDING:
                        sortIcon = DefaultLookup.getIcon(
                            this, ui, "Table.ascendingSortIcon");
                        break;
                    case DESCENDING:
                        sortIcon = DefaultLookup.getIcon(
                            this, ui, "Table.descendingSortIcon");
                        break;
                    case UNSORTED:
                        sortIcon = DefaultLookup.getIcon(
                            this, ui, "Table.naturalSortIcon");
                        break;
                    }
                }
            }
        }
        //setHorizontalTextPosition(JLabel.CENTER);
    	setHorizontalAlignment(SwingConstants.CENTER);
     
        //setText(value == null ? "" : value.toString());
        setIcon(sortIcon);

        Border border = null;
        if (hasFocus) {
            border = DefaultLookup.getBorder(this, ui, "TableHeader.focusCellBorder");
            //System.out.println("first " + border);
        }
        if (border == null) {
            border = DefaultLookup.getBorder(this, ui, "TableHeader.cellBorder");
            //System.out.println("second: " + border);
        }
//        border = table.getTableHeader().getBorder();
//        System.out.println("third: " + border);
        
        //setBorder(border);
        
        JPanel panel = new JPanel();
        GridLayout bl = new GridLayout(1, 1);
//        bl.setHgap(1);
//        bl.setVgap(1);
        panel.setLayout(bl);
        
        //panel.setBorder(table.getTableHeader().getBorder());
        panel.setBorder(border);
        panel.setPreferredSize(this.getSize());
        
        
        panel.add(this);

//        panel.setBackground(Color.RED);
//        this.setBackground(Color.green);
        
        //table.getTableHeader().setBorder(border);
        //table.getColumnModel().getColumn(0).
        
        JLabel l = new JLabel();
        l.setBorder(border);
        l.add(panel);
        
        return this;
    }
    
//    class MyItemListener implements ItemListener
//    {
//      public void itemStateChanged(ItemEvent e) {
//        Object source = e.getSource();
//        if (source instanceof AbstractButton == false) return;
//        boolean checked = e.getStateChange() == ItemEvent.SELECTED;
//        for(int x = 0, y = table.getRowCount(); x < y; x++)
//        {
//          table.setValueAt(new Boolean(checked),x,0);
//        }
//      }
//    }
    
    public static SortOrder getColumnSortOrder(JTable table, int column) {
        SortOrder rv = null;
        if (table == null || table.getRowSorter() == null) {
            return rv;
        }
        java.util.List<? extends RowSorter.SortKey> sortKeys =
            table.getRowSorter().getSortKeys();
        if (sortKeys.size() > 0 && sortKeys.get(0).getColumn() ==
            table.convertColumnIndexToModel(column)) {
            rv = sortKeys.get(0).getSortOrder();
        }
        return rv;
    }
	
//	public Component getTableCellRendererComponent(JTable table,
//												   Object value,
//												   boolean isSelected,
//												   boolean hasFocus,
//												   int row, int column) {
//		if (table == null) {
//			return this;
//		}
//
//		Color fg = null;
//		Color bg = null;
//
//		JTable.DropLocation dropLocation = table.getDropLocation();
//		if (dropLocation != null && !dropLocation.isInsertRow() && !dropLocation.isInsertColumn()
//				&& dropLocation.getRow() == row && dropLocation.getColumn() == column) {
//
//			fg = DefaultLookup.getColor(this, ui, "Table.dropCellForeground");
//			bg = DefaultLookup.getColor(this, ui, "Table.dropCellBackground");
//
//			isSelected = true;
//		}
//
//		if (isSelected) {
//			super.setForeground(fg == null ? table.getSelectionForeground() : fg);
//			super.setBackground(bg == null ? table.getSelectionBackground() : bg);
//		} else {
////			Color background = unselectedBackground != null ? unselectedBackground : table.getBackground();
////			if (background == null || background instanceof javax.swing.plaf.UIResource) {
////				Color alternateColor = DefaultLookup.getColor(this, ui, "Table.alternateRowColor");
////				if (alternateColor != null && row % 2 != 0) {
////					background = alternateColor;
////				}
////			}
////			super.setForeground(unselectedForeground != null ? unselectedForeground : table.getForeground());
////			super.setBackground(background);
//		}
//
//		setFont(table.getFont());
//
//		if (hasFocus) {
//			Border border = null;
//			if (isSelected) {
//				border = DefaultLookup.getBorder(this, ui, "Table.focusSelectedCellHighlightBorder");
//			}
//			if (border == null) {
//				border = DefaultLookup.getBorder(this, ui, "Table.focusCellHighlightBorder");
//			}
//			setBorder(border);
//
//			if (!isSelected && table.isCellEditable(row, column)) {
//				Color col;
//				col = DefaultLookup.getColor(this, ui, "Table.focusCellForeground");
//				if (col != null) {
//					super.setForeground(col);
//				}
//				col = DefaultLookup.getColor(this, ui, "Table.focusCellBackground");
//				if (col != null) {
//					super.setBackground(col);
//				}
//			}
//		} else {
//			//setBorder(this.getNoFocusBorder());
//		}
//
//		//setValue(value);
//
//		return this;
//	}
	
//    protected void setValue(Object value) {
//        setText((value == null) ? "" : value.toString());
//    }
}
