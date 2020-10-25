package ua.itea.view.swing;

public class IntegerSelector implements Cloneable {
	private boolean selected;
	private int value;
	
	public void unselect() {
		selected = false;
	}
	
	public void select(int value) {
		selected = true;
		this.value = value;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public int getValue() {
		if (!isSelected()) {
			throw new RuntimeException("Returning unselected value");
		}
		
		return value;
	}
	
	@Override
	protected IntegerSelector clone() {
		IntegerSelector clone = new IntegerSelector();
		if (isSelected()) {
			clone.select(getValue());
		}
		
		return clone;
	}

	@Override
	public String toString() {
		return "IntegerSelector [selected=" + selected + ", value=" + value + "]";
	}
}
