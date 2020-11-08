package ua.itea.view.swing;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar {
	private JMenu mainMenu;
	
	public MenuBar() {
		mainMenu = new Menu();
		add(mainMenu);
	}
}
