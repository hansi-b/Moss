package org.hansi_b.moss;

import java.util.Comparator;

/**
 * A single field in the Sudoku. Has a row and a column (at least in the square
 * version). Does not maintain its value, but lets the Sudoku do that. It is
 * itself actually immutable.
 */
public class Cell {

	public final static Comparator<Cell> positionComparator = new Comparator<Cell>() {
		@Override
		public int compare(final Cell c1, final Cell c2) {
			final int rowDiff = c1.rowIdx - c2.rowIdx;
			return rowDiff != 0 ? rowDiff : c1.colIdx - c2.colIdx;
		}
	};

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
}