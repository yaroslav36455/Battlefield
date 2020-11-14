package ua.itea.view.swing;

public class NonPositiveNumberFormatException extends NumberFormatException {
	public NonPositiveNumberFormatException() {
		super("Non positive value");
	}
}
