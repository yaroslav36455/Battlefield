package ua.itea.view.swing;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class AddSquadDialog extends AddFormationDialog {
	private JTextField damageTextField;
	private JTextField defenceTextField;
	private JTextField velocityTextField;
	
	public AddSquadDialog(JFrame owner, String title) {
		super(owner, title);
	}
}
