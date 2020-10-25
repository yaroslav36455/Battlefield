package ua.itea.model;

import java.util.function.Predicate;

import ua.itea.model.util.Position;
import ua.itea.model.util.Size;

public class DisplacementField extends Field<DisplacementCell> {
	private Predicate<Position> movementAllowed;
	private long currenId;
	
	public DisplacementField(int width, int height) {
		super(new Size(width, height));
	}
	
	public void setMovementAllowedPredicate(Predicate<Position> movementAllowed) {
		this.movementAllowed = movementAllowed;
	}
	
	public boolean isMovementAllowed(Position position) {
		return isWithin(position)
				&& !isVisited(position)
				&& movementAllowed.test(position);
	}

	public DisplacementField(Size size) {
		super(size);
	}
	
	public void refresh() {
		++currenId;
	}
	
	private boolean isVisited(Position position) {
		return get(position.getX(), position.getY()).isVisited(currenId);
	}
	
	public void visit(Position position) {
		get(position.getX(), position.getY()).visit(currenId);
	}

	@Override
	protected DisplacementCell[][] realloc(Size size) {
		DisplacementCell[][] field = new DisplacementCell[size.getWidth()][size.getHeight()];
		
		refresh();
		for (int r = 0; r < field.length; r++) {
			for (int c = 0; c < field[r].length; c++) {
				field[r][c] = new DisplacementCell(new Position(r, c), currenId);
			}
		}
		return field;
	}
}
