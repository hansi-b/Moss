package org.hansi_b.moss;

/**
 * A single field in the Sudoku. Has a row and a column (at least in the square
 * version). Does not maintain its value, but lets the Sudoku do that. It is
 * itself actually immutable.
 */
public class Cell {

	private final Sudoku sudoku;
	private final Pos pos;

	Cell(final Sudoku sudoku, final Pos pos) {
		this.sudoku = sudoku;
		this.pos = pos;
	}

	public Integer getValue() {
		return sudoku.getValue(pos.row, pos.col);
	}

	public int getRow() {
		return pos.row;
	}

	public int getCol() {
		return pos.col;
	}

	public CellGroup getGroup(final CellGroup.Type groupType) {
		return sudoku.getGroup(groupType, pos);
	}

	public void setValue(final Integer newValue) {
		sudoku.set(pos.row, pos.col, newValue);
	}

	@Override
	public String toString() {
		return String.format("Cell%s", pos);
	}
}