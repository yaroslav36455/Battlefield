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

import ua.itea.model.util.Size;

public class RequestSizeDialog extends JDialog {
	private JTextField widthTextField;
	private JTextField heightTextField;
	private JButton okButton;
	private JButton cancelButton;
	private Consumer<Size> sizeConsumer;
	
	public RequestSizeDialog(Window owner) {
		super(owner, "Set field size", true);
		
		widthTextField = new JTextField();
		heightTextField = new JTextField();
		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		
		setLayout(new GridLayout(3, 2));
		
		add(new JLabel("Width: "));
		add(widthTextField);
		add(new JLabel("Height:"));
		add(heightTextField);
		add(okButton);
		add(cancelButton);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		setButtonActionListeners();
	}
	
	public void setSizeConsumer(Consumer<Size> sizeConsumer) {
		this.sizeConsumer = sizeConsumer;
	}
	
	private void setButtonActionListeners() {
		setOkButtonActionListener();
		setCancelButtonActionListener();
	}
	
	private void setOkButtonActionListener() {
		RequestSizeDialog thisDialog = this;
		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					int width = Integer.valueOf(widthTextField.getText());
					int height = Integer.valueOf(heightTextField.getText());
					
					sizeConsumer.accept(new Size(width, height));
					setVisible(false);
					
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(thisDialog, "Set positive integer values",
							"Incorrect input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	private void setCancelButtonActionListener() {
		cancelButton.addActionListener(e->setVisible(false));
	}

	@Override
	public void setVisible(boolean b) {
		widthTextField.setText("50");
		heightTextField.setText("50");
		super.setVisible(b);
	}
}
