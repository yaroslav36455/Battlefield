package ua.itea.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class AddFormationDialog extends JDialog {
	
	private JTextField teamNameTextField;
	private JButton colorPickerButton;
	private JButton okButton;
	private JButton cancelButton;
	
	private JColorChooser colorChooser;
	private JDialog colorChooserDialog;
	
	public AddFormationDialog(JFrame owner, String title) {
		super(owner, title, true);
		
		setResizable(false);
		
		teamNameTextField = createTeamNameTextField();
		colorPickerButton = createColorPickerButton();
		colorChooserDialog = createColorChooserDialog();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		
		JPanel mainPanel = new JPanel();
		
		mainPanel.setLayout(new GridBagLayout());
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		mainPanel.add(new JLabel("Name:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		mainPanel.add(teamNameTextField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		mainPanel.add(new JLabel("Color:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		mainPanel.add(colorPickerButton, gbc);
		
		okButton = createOkButton();
		cancelButton = createCancelButton();
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPanel);
		getContentPane().add(buttonsPanel, BorderLayout.PAGE_END);
		
		pack();
	}

	public void refresh(String nameTextField, Color color) {
		teamNameTextField.setText(nameTextField);
		teamNameTextField.selectAll();
		colorPickerButton.setBackground(color);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JTextField createTeamNameTextField() {
		JTextField teamNameTextField = new JTextField();
		
		Dimension size = teamNameTextField.getPreferredSize();
		size.width = 200;
		teamNameTextField.setPreferredSize(size);
		
		return teamNameTextField;
	}
	
	private JButton createColorPickerButton() {
		JButton button = new JButton();
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("ColorPickerButton not implemented yet");
				colorChooser.setColor(colorPickerButton.getBackground());
				colorChooserDialog.setVisible(true);
			}
		});
		
		return button;
	}
	
	private JDialog createColorChooserDialog() {
		colorChooser = new JColorChooser();
		return JColorChooser.createDialog(colorPickerButton,
										  "Select Color",
										  true,
										  colorChooser,
										  null,
										  null);
	}

	private JButton createOkButton() {
		JButton button = new JButton("Ok");
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button Ok not implemented yet");
			}
		});
		
		return button;
	}

	private JButton createCancelButton() {
		JButton button = new JButton("Cancel");
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button Cancel not implemented yet");
				dispose();
				colorChooserDialog.dispose();
			}
		});
		
		return button;
	}
}
