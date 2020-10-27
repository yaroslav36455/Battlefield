package ua.itea.model;

import ua.itea.model.util.Size;

public class BattleField extends Field<Cell> {

	public BattleField(Size size) {
		super(size);
	}

	@Override
	protected Cell[][] realloc(Size size) {
		Cell[][] field = new Cell[size.getWidth()][size.getHeight()];
		
		for (int r = 0; r < field.length; r++) {
			for (int c = 0; c < field[r].length; c++) {
				field[r][c] = new Cell(null);
			}
		}
		return field;
	}
}
