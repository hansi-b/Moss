package org.hansi_b.moss;

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

	public void setValue(final Integer newValue) {
		sudoku.set(rowIdx, colIdx, newValue);
	}
}