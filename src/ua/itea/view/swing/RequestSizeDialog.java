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
	private Size acceptedSize;
	
	public RequestSizeDialog(Window owner) {
		super(owner, "Field Size", true);
		
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
		
		getRootPane().setDefaultButton(okButton);
		
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
		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					int width = Integer.valueOf(widthTextField.getText());
					int height = Integer.valueOf(heightTextField.getText());
					
					if (width <= 0 || height <= 0) {
						throw new NonPositiveNumberFormatException();
					}
					
					Size tmpSize = new Size(width, height);
					
					sizeConsumer.accept(tmpSize);
					setFieldSize(tmpSize);
					setVisible(false);
					
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(RequestSizeDialog.this,
							"Set positive integer values",
							"Incorrect input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	private void setCancelButtonActionListener() {
		cancelButton.addActionListener(e-> {
			setVisible(false);
			setFieldSize(acceptedSize);
		});
	}
	
	public void setFieldSize(Size size) {
		acceptedSize = size;
		widthTextField.setText(String.valueOf(size.getWidth()));
		heightTextField.setText(String.valueOf(size.getHeight()));
	}
}
