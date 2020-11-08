package ua.itea.view.swing;

import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar {
	private Menu menu;
	
	public MenuBar() {
		menu = new Menu();
		add(menu);
	}
	
	public Menu getMenu() {
		return menu;
	}
}
