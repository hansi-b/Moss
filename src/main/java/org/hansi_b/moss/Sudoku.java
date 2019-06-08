package org.hansi_b.moss;

import java.util.BitSet;

public class Sudoku {

	private static final int DEFAULT_SIZE = 9;

	private final int size;
	private final int sizeSqrt;

	private final Integer[][] cells;

	public Sudoku() {
		this(DEFAULT_SIZE);
	}

	public Sudoku(final int size) {
		final double sqrt = Math.sqrt(size);
		this.sizeSqrt = (int) Math.floor(sqrt);
		if (sizeSqrt != sqrt)
			throw new IllegalArgumentException(
					String.format("Sudoku cannot be initialised with a non-square size (got %d)", size));
		this.size = size;
		this.cells = new Integer[size][size];
	}

	public static Sudoku create(final Integer... values) {
		final int size = Double.valueOf(Math.sqrt(values.length)).intValue();
		final Sudoku su = new Sudoku(size);
		for (int row = 0; row < size; row++)
			System.arraycopy(values, row * size, su.cells[row], 0, size);
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
		return cell(row, col);
	}

	private Integer cell(final int row, final int col) {
		return cells[row - 1][col - 1];
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
		cells[row - 1][col - 1] = newValue;
	}

	private Integer checkValueArg(final Integer newValue) {
		if (newValue != null && (newValue < 1 || newValue > size))
			throw new IllegalArgumentException(
					String.format("Cell value must be null or between one and at most %d (is %d)", size, newValue));
		return newValue;
	}

	private void checkArg(final int arg, final String label) {
		if (arg < 1 || arg > size)
			throw new IllegalArgumentException(
					String.format("%s argument must be positive and at most %d (is %d)", label, size, arg));
	}

	Integer[] getRow(final int row) {
		checkArg(row, "Row");
		final Integer[] vals = new Integer[size];
		for (int i = 0; i < size; i++)
			vals[i] = cell(row, i + 1);
		return vals;
	}

	Integer[] getCol(final int col) {
		checkArg(col, "Column");
		final Integer[] vals = new Integer[size];
		for (int i = 0; i < size; i++)
			vals[i] = cell(i + 1, col);
		return vals;
	}

	Integer[] getBlock(final int block) {
		checkArg(block, "Block");

		final int rowOffset = sizeSqrt * ((block - 1) / sizeSqrt);
		final int colOffset = sizeSqrt * ((block - 1) % sizeSqrt);

		final Integer[] vals = new Integer[size];
		for (int r = 0; r < sizeSqrt; r++)
			for (int c = 0; c < sizeSqrt; c++)
				vals[r * sizeSqrt + c] = cells[r + rowOffset][c + colOffset];
		return vals;
	}

	private boolean isSolved(final Integer[] elements) {
		final BitSet targets = new BitSet(size);
		for (final Integer e : elements)
			if (e != null)
				targets.set(e - 1);
		return targets.cardinality() == size;
	}

	public boolean isSolved() {

		for (int i = 1; i <= size; i++) {
			if (!isSolved(getRow(i)) || !isSolved(getCol(i)) || !isSolved(getBlock(i)))
				return false;
		}
		return true;
	}
}
