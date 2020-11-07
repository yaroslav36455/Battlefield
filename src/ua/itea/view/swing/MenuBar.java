package ua.itea.view.swing;

import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

public class MenuBar extends JMenuBar {
	public MenuBar() {
		JMenu mainMenu = new JMenu("Menu");
		
		JMenuItem createField = new JMenuItem("Create field...");
		JMenuItem removeField = new JMenuItem("Remove field");
		JRadioButtonMenuItem useTeamColors = new JRadioButtonMenuItem("Use team colors");
		JRadioButtonMenuItem useSquadColors = new JRadioButtonMenuItem("Use squad colors");
		JMenuItem exit = new JMenuItem("Exit");
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(useTeamColors);
		buttonGroup.add(useSquadColors);
		
		removeField.setEnabled(false);
		useTeamColors.setEnabled(false);
		useSquadColors.setEnabled(false);
		
		mainMenu.setMnemonic(KeyEvent.VK_M);
		createField.setMnemonic(KeyEvent.VK_C);
		removeField.setMnemonic(KeyEvent.VK_R);
		exit.setMnemonic(KeyEvent.VK_X);
		
		mainMenu.add(createField);
		mainMenu.add(removeField);
		mainMenu.add(new JSeparator());
		mainMenu.add(useTeamColors);
		mainMenu.add(useSquadColors);
		mainMenu.add(new JSeparator());
		mainMenu.add(exit);
		
		add(mainMenu);
	}
}
