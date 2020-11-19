package ua.itea.view.swing;

public class NonPositiveNumberFormatException extends NumberFormatException {
	private static final long serialVersionUID = 1L;

	public NonPositiveNumberFormatException() {
		super("Non positive value");
	}
}
