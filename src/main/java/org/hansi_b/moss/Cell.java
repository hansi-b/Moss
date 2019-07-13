package org.hansi_b.moss;

/**
 * A single field in the Sudoku. Has a row and a column (at least in the square
 * version). Does not maintain its value, but lets the Sudoku do that. It is
 * itself actually immutable.
 */
public class Cell {

	private final Sudoku sudoku;
	private final int rowIdx;
	private final int colIdx;

	Cell(final Sudoku sudoku, final int rowIdx, final int colIdx) {
		this.sudoku = sudoku;
		this.rowIdx = rowIdx;
		this.colIdx = colIdx;
	}

	public Integer getValue() {
		return sudoku.getValue(rowIdx, colIdx);
	}

	public int getRow() {
		return rowIdx;
	}

	public int getCol() {
		return colIdx;
	}

	public void setValue(final Integer newValue) {
		sudoku.set(rowIdx, colIdx, newValue);
	}

	@Override
	public String toString() {
		return String.format("Cell(%d,%d)", rowIdx, colIdx);
	}
}