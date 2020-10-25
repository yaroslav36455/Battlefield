package ua.itea.model;

import ua.itea.model.util.Position;
import ua.itea.model.util.Size;

public abstract class Field<T> {
	private Size size;
	private T[][] field;
	
	public Field(Size size) {
		setSize(size);
	}
	
	public boolean isWithin(Position position) {
		int x = position.getX();
		int y = position.getY();
		
		return x >= 0 && x < size.getWidth()
				&& y >= 0 && y < size.getHeight();
	}
	
	public Size getSize() {
		return size;
	}
	
	public void resize(Size size) {
		if (!size.equals(size)) {
			setSize(size);
		}
	}
	
	private void setSize(Size size) {
		this.size = size;
		field = realloc(size);
	}
	
	protected abstract T[][] realloc(Size size);
	
	public T get(Position pos) {
		return get(pos.getX(), pos.getY());
	}
	
	public T get(int x, int y) {
		return field[x][y];
	}
}
