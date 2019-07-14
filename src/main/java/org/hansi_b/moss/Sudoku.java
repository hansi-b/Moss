package org.hansi_b.moss;

import java.util.BitSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.hansi_b.moss.CellGroup.Block;
import org.hansi_b.moss.CellGroup.Col;
import org.hansi_b.moss.CellGroup.Row;

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
			initGroups(sudoku.rows, Factory::createRow);
			initGroups(sudoku.cols, Factory::createCol);
			initGroups(sudoku.blocks, Factory::createBlock);
			return sudoku;
		}

		private void initCells() {
			IntStream.range(0, sudoku.size).forEach(rowIdx -> {
				IntStream.range(0, sudoku.size).forEach(colIdx -> {
					sudoku.cells[rowIdx][colIdx] = new Cell(sudoku, rowIdx, colIdx);
				});
			});
		}

		private <T extends CellGroup> void initGroups(final T[] group, final BiFunction<Integer, Cell[][], T> grouper) {
			for (int i = 0; i < group.length; i++)
				group[i] = grouper.apply(i, sudoku.cells);
		}

		private static Block createBlock(final int blockIdx, final Cell[][] cells) {
			return new Block(mapPosToCells(blockIdx, cells, Factory::streamBlockPos));
		}

		private static Row createRow(final int rowIdx, final Cell[][] cells) {
			return new Row(mapPosToCells(rowIdx, cells, Factory::streamRowPos));
		}

		private static Col createCol(final int colIdx, final Cell[][] cells) {
			return new Col(mapPosToCells(colIdx, cells, Factory::streamColPos));
		}

		private static List<Cell> mapPosToCells(final int groupIdx, final Cell[][] cells,
				final BiFunction<Integer, Integer, Stream<Pos>> groupPosStream) {
			return groupPosStream.apply(groupIdx, cells.length).map(p -> cells[p.row][p.col])
					.collect(Collectors.toList());
		}

		private static Stream<Pos> streamBlockPos(final int blockIdx, final int size) {
			final int sizeSqrt = (int) Math.sqrt(size);
			// integer cutoff for the row offset:
			final int rowOffset = sizeSqrt * (blockIdx / sizeSqrt);
			final int colOffset = sizeSqrt * (blockIdx % sizeSqrt);

			return IntStream.range(0, sizeSqrt).boxed().flatMap(rowIdx -> IntStream.range(0, sizeSqrt)
					.mapToObj(colIdx -> Pos.at(rowOffset + rowIdx, colOffset + colIdx)));
		}

		private static Stream<Pos> streamRowPos(final int rowIdx, final int size) {
			return IntStream.range(0, size).mapToObj(colIdx -> Pos.at(rowIdx, colIdx));
		}

		private static Stream<Pos> streamColPos(final int colIdx, final int size) {
			return IntStream.range(0, size).mapToObj(rowIdx -> Pos.at(rowIdx, colIdx));
		}
	}

	private final int size;

	private final Cell[][] cells;
	private final Integer[][] values;

	private final Row[] rows;
	private final Col[] cols;
	private final Block[] blocks;

	private Sudoku(final int size) {
		this.size = size;

		this.cells = new Cell[size][size];
		this.rows = new Row[size];
		this.cols = new Col[size];
		this.blocks = new Block[size];

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

	public Row getRow(final int row) {
		checkArg(row, "Row");
		return rows[row];
	}

	public Col getCol(final int col) {
		checkArg(col, "Column");
		return cols[col];
	}

	public Block getBlock(final int block) {
		checkArg(block, "Block");
		return blocks[block];
	}

	private boolean isSolved(final Iterable<Cell> elements) {
		final BitSet targets = new BitSet(size);
		for (final Cell e : elements) {
			final Integer v = e.getValue();
			if (v != null)
				targets.set(v - 1);
		}

		return targets.cardinality() == size;
	}

	public boolean isSolved() {

		for (int i = 0; i < size; i++) {
			if (!isSolved(getRow(i)) || !isSolved(getCol(i)) || !isSolved(getBlock(i)))
				return false;
		}
		return true;
	}
}
