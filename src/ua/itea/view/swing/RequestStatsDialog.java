package ua.itea.view.swing;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ua.itea.model.Stats;

public class RequestStatsDialog extends JDialog {
	private Consumer<Stats> statsConsumer;
	
	private JTextField healthTextField;
	private JTextField damageTextField;
	private JTextField defenceTextField;
	private JTextField velocityTextField;
	
	private JButton okButton;
	private JButton cancelButton;
	
	public RequestStatsDialog(Window owner) {
		super(owner, "Stats", true);
		
		healthTextField = new JTextField();
		damageTextField = new JTextField();
		defenceTextField = new JTextField();
		velocityTextField = new JTextField();
		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		
		setLayout(new GridLayout(5, 2));
		
		add(new JLabel("Init Health:"));
		add(healthTextField);
		
		add(new JLabel("Damage:"));
		add(damageTextField);
		
		add(new JLabel("Defence:"));
		add(defenceTextField);
		
		add(new JLabel("Velocity:"));
		add(velocityTextField);
		
		add(okButton);
		add(cancelButton);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		setButtonActionListeners();
	}
	
	public void setStatsConsumer(Consumer<Stats> statsConsumer) {
		this.statsConsumer = statsConsumer;
	}
	
	private void setButtonActionListeners() {
		setOkButtonActionListener();
		setCancelButtonActionListener();
	}

	private void setOkButtonActionListener() {
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					float health = Float.valueOf(healthTextField.getText());
					int damage = Integer.valueOf(damageTextField.getText());
					int defence = Integer.valueOf(defenceTextField.getText());
					int velocity = Integer.valueOf(velocityTextField.getText());
					
					if (health <= 0 || damage <= 0 || defence <= 0 || velocity <= 0) {
						throw new NonPositiveNumberFormatException();
					}
					
					statsConsumer.accept(new Stats(health, damage, defence, velocity));
					setVisible(false);
					
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(RequestStatsDialog.this,
							"Set positive integer values",
							"Incorrect input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	private void setCancelButtonActionListener() {
		cancelButton.addActionListener(e->setVisible(false));
	}
	
	public void setStats(Stats stats) {
		healthTextField.setText(String.valueOf(stats.getHealth()));
		damageTextField.setText(String.valueOf(stats.getDamage()));
		defenceTextField.setText(String.valueOf(stats.getDefence()));
		velocityTextField.setText(String.valueOf(stats.getVelocity()));
	}
}
