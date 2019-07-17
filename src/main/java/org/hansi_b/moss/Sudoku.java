package org.hansi_b.moss;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.hansi_b.moss.CellGroup.Block;
import org.hansi_b.moss.CellGroup.Col;
import org.hansi_b.moss.CellGroup.Row;
import org.hansi_b.moss.CellGroup.Type;

public class Sudoku {

	public static class Factory {

		private static final int DEFAULT_SIZE = 9;

		private Sudoku sudoku;

		public Sudoku create() {
			return create(DEFAULT_SIZE);
		}

		public Sudoku create(final Integer... values) {
			final int size = Double.valueOf(Math.sqrt(values.length)).intValue();
			final Sudoku su = create(size);
			for (int row = 0; row < size; row++)
				System.arraycopy(values, row * size, su.values[row], 0, size);
			return su;
		}

		public Sudoku create(final int size) {
			final double sqrt = Math.sqrt(size);
			final int sizeSqrt = (int) Math.floor(sqrt);
			if (sizeSqrt != sqrt)
				throw new IllegalArgumentException(
						String.format("Sudoku cannot be initialised with a non-square size (got %d)", size));

			sudoku = new Sudoku(size);
			initCells();
			initGroups(Type.Row, Row::new);
			initGroups(Type.Col, Col::new);
			initGroups(Type.Block, Block::new);
			return sudoku;
		}

		private void initCells() {
			IntStream.range(0, sudoku.size).forEach(rowIdx -> {
				IntStream.range(0, sudoku.size).forEach(colIdx -> {
					sudoku.cells[rowIdx][colIdx] = new Cell(sudoku, Pos.at(rowIdx, colIdx));
				});
			});
		}

		private <T extends CellGroup> void initGroups(final Type cellGroupType, final Function<List<Cell>, T> newCall) {

			final List<CellGroup> groups = new ArrayList<CellGroup>();
			sudoku.cellGroupsByType.put(cellGroupType, groups);
			IntStream.range(0, sudoku.size).forEach(idx -> {
				final Stream<Pos> posStream = cellGroupType.getPos(idx, sudoku.size);
				final List<Cell> cells = posStream.map(p -> sudoku.cells[p.row][p.col]).collect(Collectors.toList());
				groups.add(newCall.apply(cells));
			});
		}
	}

	private final int size;

	private final EnumMap<Type, List<CellGroup>> cellGroupsByType;

	private final Cell[][] cells;
	private final Integer[][] values;

	private Sudoku(final int size) {
		this.size = size;

		cellGroupsByType = new EnumMap<>(Type.class);

		this.cells = new Cell[size][size];
		this.values = new Integer[size][size];
	}

	/**
	 * @return the number of cells per row, column, and block
	 */
	public int size() {
		return size;
	}

	/**
	 * @param row the 0-based row in the Sudoku (i.e., maximum is equal to the
	 *            Sudoku's size)
	 * @param col the 0-based column in the Sudoku (i.e., the maximum allowed value
	 *            is the Sudoku's size)
	 * @return either an Integer with the cell's value, i.e., a number between one
	 *         and the Sudoku's size; or null to indicate that the field is empty
	 */
	public Integer getValue(final int row, final int col) {
		checkArg(row, "Row");
		checkArg(col, "Column");
		return valueAt(row, col);
	}

	private Integer valueAt(final int row, final int col) {
		return values[row][col];
	}

	/**
	 * @param row      the 0-based row in the Sudoku (i.e., maximum is equal to the
	 *                 Sudoku's size)
	 * @param col      the 0-based column in the Sudoku (i.e., the maximum allowed
	 *                 value is the Sudoku's size)
	 * @param newValue either a value between one and the Sudoku's size (inclusive);
	 *                 or null to indicate the field should be empty
	 */
	public void set(final int row, final int col, final Integer newValue) {
		checkArg(row, "Row");
		checkArg(col, "Column");
		checkValueArg(newValue);
		values[row][col] = newValue;
	}

	private Integer checkValueArg(final Integer newValue) {
		if (newValue != null && (newValue < 1 || newValue > size))
			throw new IllegalArgumentException(
					String.format("Cell value must be null or between one and at most %d (is %d)", size, newValue));
		return newValue;
	}

	private void checkArg(final int arg, final String label) {
		if (arg < 0 || arg >= size)
			throw new IllegalArgumentException(
					String.format("%s argument must not be negative and at most %d (is %d)", label, size - 1, arg));
	}

	public CellGroup getGroup(final Type groupType, final int groupIdx) {
		checkArg(groupIdx, groupType.toString());
		return cellGroupsByType.get(groupType).get(groupIdx);
	}

	public boolean isSolved() {
		for (int i = 0; i < size; i++) {
			for (final Type groupType : Type.values())
				if (!getGroup(groupType, i).isSolved())
					return false;
		}
		return true;
	}
}
