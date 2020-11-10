package ua.itea.view.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

public class Menu extends JMenu {
	private JMenuItem createField;
	private JMenuItem removeField;
	private JRadioButtonMenuItem useTeamColors;
	private JRadioButtonMenuItem useSquadColors;
	private JMenuItem exit;
	
	public Menu() {
		super("Menu");
		
		createField = new JMenuItem("Create field...");
		removeField = new JMenuItem("Remove field");
		useTeamColors = new JRadioButtonMenuItem("Use team colors");
		useSquadColors = new JRadioButtonMenuItem("Use squad colors");
		exit = new JMenuItem("Exit");
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(useTeamColors);
		buttonGroup.add(useSquadColors);
		
		add(createField);
		add(removeField);
		add(new JSeparator());
		add(useTeamColors);
		add(useSquadColors);
		add(new JSeparator());
		add(exit);
		
		setState();
		setListeners();
		setMnemonics();
	}
	
	private void setState() {
		createField.setEnabled(true);
		removeField.setEnabled(false);
		useTeamColors.setEnabled(false);
		useSquadColors.setEnabled(false);
		
		useTeamColors.setSelected(true);
	}
	
	private void setListeners() {
		createField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				useTeamColors.setEnabled(true);
				useSquadColors.setEnabled(true);
			}
		});
		
		removeField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				useTeamColors.setEnabled(false);
				useSquadColors.setEnabled(false);
				
			}
		});
	}

	private void setMnemonics() {
		setMnemonic(KeyEvent.VK_M);
		createField.setMnemonic(KeyEvent.VK_C);
		removeField.setMnemonic(KeyEvent.VK_R);
		exit.setMnemonic(KeyEvent.VK_X);
	}
	
	public void addCreateFieldListener(ActionListener newListener) {
		createField.addActionListener(newListener);
	}
	
	public void addRemoveFieldListener(ActionListener newListener) {
		removeField.addActionListener(newListener);
	}
	
	public void addUseTeamColorsListener(ActionListener newListener) {
		useTeamColors.addActionListener(newListener);
	}
	
	public void addUseSquadColorsListener(ActionListener newListener) {
		useSquadColors.addActionListener(newListener);
	}
	
	public void addExitListeners(ActionListener newListener) {
		exit.addActionListener(newListener);
	}
	
	public void setCreatingSuccess() {
		createField.setEnabled(false);
		removeField.setEnabled(true);
	}
	
	public void setRemovingSuccess() {
		createField.setEnabled(true);
		removeField.setEnabled(false);
	}
	
	public boolean isTeamColors() {
		return useTeamColors.isSelected();
	}
}
