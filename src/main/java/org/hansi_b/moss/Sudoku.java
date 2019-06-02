package org.hansi_b.moss;

import java.util.Arrays;

public class Sudoku {

	private static final int DEFAULT_SIZE = 9;

	private final int size;
	private final Integer[] cells;

	public Sudoku() {
		this(DEFAULT_SIZE);
	}

	public Sudoku(final int size) {
		final double sqrt = Math.sqrt(size);
		if (Math.floor(sqrt) != sqrt)
			throw new IllegalArgumentException(
					String.format("Sudoku cannot be initialised with a non-square size (got %d)", size));
		this.size = size;
		this.cells = new Integer[size * size];
	}

	public static Sudoku create(final Integer... values) {
		final int size = Double.valueOf(Math.sqrt(values.length)).intValue();
		final Sudoku su = new Sudoku(size);
		Arrays.setAll(su.cells, i -> su.checkValueArg(values[i]));
		return su;
	}

	/**
	 * @param row the 1-based row in the Sudoku (i.e., maximum is equal to the
	 *            Sudoku's size)
	 * @param col the 1-based column in the Sudoku (i.e., the maximum allowed value
	 *            is the Sudoku's size)
	 * @return either an Integer with the cell's value, a number between one and the
	 *         Sudoku's size; or null to indicate that the field is empty
	 */
	public Integer get(final int row, final int col) {
		checkArg(row, "Row");
		checkArg(col, "Column");
		return cells[rowCol2Index(row, col)];
	}

	/**
	 * @param row      the 1-based row in the Sudoku (i.e., maximum is equal to the
	 *                 Sudoku's size)
	 * @param col      the 1-based column in the Sudoku (i.e., the maximum allowed
	 *                 value is the Sudoku's size)
	 * @param newValue either a value between one and the Sudoku's size (inclusive);
	 *                 or null to indicate the field should be empty
	 */
	public void set(final int row, final int col, final Integer newValue) {
		checkArg(row, "Row");
		checkArg(col, "Column");
		checkValueArg(newValue);
		cells[rowCol2Index(row, col)] = newValue;
	}

	private Integer checkValueArg(final Integer newValue) {
		if (newValue != null && (newValue < 1 || newValue > size))
			throw new IllegalArgumentException(
					String.format("Cell value must be null or between one and at most %d (is %d)", size, newValue));
		return newValue;
	}

	private int rowCol2Index(final int row, final int col) {
		return size * (row - 1) + col - 1;
	}

	private void checkArg(final int arg, final String label) {
		if (arg < 1 || arg > size)
			throw new IllegalArgumentException(
					String.format("%s argument must be positive and at most %d (is %d)", label, size, arg));
	}
}
