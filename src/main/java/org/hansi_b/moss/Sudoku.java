package org.hansi_b.moss;

public class Sudoku {

	private static final int DEFAULT_SIZE = 9;

	private final int size;
	private final int[] cells;

	public Sudoku() {
		this(DEFAULT_SIZE);
	}

	public Sudoku(final int size) {
		final double sqrt = Math.sqrt(size);
		if (Math.floor(sqrt) != sqrt)
			throw new IllegalArgumentException(
					String.format("Sudoku cannot be initialised with a non-square size (got %d)", size));
		this.size = size;
		this.cells = new int[size * size];
	}

	/**
	 *
	 * @param row the 1-based row in the Sudoku (i.e., maximum is equal to the
	 *            Sudoku's size)
	 * @param col the 1-based column in the Sudoku (i.e., the maximum allowed value
	 *            is the Sudoku's size)
	 * @return the cell's value, a number between zero and the Sudoku's size; zero
	 *         indicates that the field is empty
	 */
	public int get(final int row, final int col) {
		checkRow(row);
		checkCol(col);
		return cells[rowCol2Index(row, col)];
	}

	/**
	 *
	 * @param row      the 1-based row in the Sudoku (i.e., maximum is equal to the
	 *                 Sudoku's size)
	 * @param col      the 1-based column in the Sudoku (i.e., the maximum allowed
	 *                 value is the Sudoku's size)
	 * @param newValue a number between one and the Sudoku's size (inclusive)
	 */
	public void set(final int row, final int col, final int newValue) {
		checkRow(row);
		checkCol(col);
		checkValue(newValue);
		cells[rowCol2Index(row, col)] = newValue;
	}

	public void unset(final int row, final int col) {
		checkRow(row);
		checkCol(col);
		cells[rowCol2Index(row, col)] = 0;
	}

	private int rowCol2Index(final int row, final int col) {
		return size * (row - 1) + col - 1;
	}

	private void checkRow(final int row) {
		checkArg(row, "Row");
	}

	private void checkCol(final int col) {
		checkArg(col, "Column");
	}

	private void checkValue(final int val) {
		checkArg(val, "Cell value");
	}

	private void checkArg(final int arg, final String label) {
		if (arg < 1 || arg > size)
			throw new IllegalArgumentException(
					String.format("%s argument must be positive and at most %d (is %d)", label, size, arg));
	}
}
