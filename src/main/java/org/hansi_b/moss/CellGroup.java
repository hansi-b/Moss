package org.hansi_b.moss;

import java.util.BitSet;
import java.util.List;
import java.util.SortedSet;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CellGroup {

	public enum Type {

		Row(Type::rowPos), //
		Col(Type::colPos), //
		Block(Type::blockPos);

		private final BiFunction<Integer, Integer, Stream<Pos>> posStreamer;

		Type(final BiFunction<Integer, Integer, Stream<Pos>> posStreamer) {
			this.posStreamer = posStreamer;
		}

		/**
		 * @param groupIdx   the index of this group in the Sudoku; by definition, there
		 *                   should be - per group type - one group for each index
		 *                   between zero and the Sudoku size minus one
		 * @param sudokuSize the size of the Sudoku
		 * @return a Stream of the positions of this group's cells
		 */
		public Stream<Pos> getPos(final int groupIdx, final int sudokuSize) {
			return posStreamer.apply(groupIdx, sudokuSize);
		}

		private static Stream<Pos> rowPos(final int groupIdx, final int sudokuSize) {
			return IntStream.range(0, sudokuSize).mapToObj(colIdx -> Pos.at(groupIdx, colIdx));
		}

		private static Stream<Pos> colPos(final int groupIdx, final int sudokuSize) {
			return IntStream.range(0, sudokuSize).mapToObj(rowIdx -> Pos.at(rowIdx, groupIdx));
		}

		private static Stream<Pos> blockPos(final int groupIdx, final int sudokuSize) {
			final int sizeSqrt = (int) Math.sqrt(sudokuSize);
			// integer cutoff for the row offset:
			final int rowOffset = sizeSqrt * (groupIdx / sizeSqrt);
			final int colOffset = sizeSqrt * (groupIdx % sizeSqrt);

			return IntStream.range(0, sizeSqrt).boxed().flatMap( //
					rowIdx -> IntStream.range(0, sizeSqrt)
							.mapToObj(colIdx -> Pos.at(rowOffset + rowIdx, colOffset + colIdx)));
		}
	}

	public static class Row extends CellGroup {
		Row(final Sudoku sudoku, final List<Cell> cells) {
			super(sudoku, Type.Row, cells);
		}

		@Override
		public String toString() {
			return String.format("%s %d", Type.Row, firstRow());
		}
	}

	public static class Col extends CellGroup {
		Col(final Sudoku sudoku, final List<Cell> cells) {
			super(sudoku, Type.Col, cells);
		}

		@Override
		public String toString() {
			return String.format("%s %d", Type.Col, firstCol());
		}
	}

	public static class Block extends CellGroup {
		Block(final Sudoku sudoku, final List<Cell> cells) {
			super(sudoku, Type.Block, cells);
		}

		@Override
		public String toString() {
			return String.format("%s %d/%d", Type.Block, firstRow(), firstCol());
		}
	}

	private final Sudoku sudoku;
	private final CellGroup.Type type;
	private final List<Cell> cells;

	CellGroup(final Sudoku sudoku, final CellGroup.Type type, final List<Cell> cells) {
		this.sudoku = sudoku;
		this.type = type;
		this.cells = cells;
	}

	public int size() {
		return cells.size();
	}

	public CellGroup.Type type() {
		return type;
	}

	/**
	 * The values of the cells in this group. May contain duplicates and empty,
	 * i.e., null values.
	 *
	 * @return a fresh, mutable List of the values in this group, in order of cell
	 *         iteration
	 */
	public List<Integer> values() {
		return streamAllCells().map(Cell::getValue).collect(Collectors.toList());
	}

	/**
	 * The possible values for the Sudoku not in any cell of this group.
	 *
	 * @return a fresh sorted set on numbers missing in this group
	 */
	public SortedSet<Integer> missing() {

		final SortedSet<Integer> possibleValues = sudoku.possibleValues();
		streamFilledCells().forEach(c -> possibleValues.remove(c.getValue()));
		return possibleValues;
	}

	public Stream<Cell> streamEmptyCells() {
		return streamAllCells().filter(Cell::isEmpty);
	}

	public Stream<Cell> streamFilledCells() {
		return streamAllCells().filter(c -> !c.isEmpty());
	}

	private Stream<Cell> streamAllCells() {
		return cells.stream();
	}

	public boolean isSolved() {

		final BitSet targets = new BitSet(size());
		streamFilledCells().forEach(e -> targets.set(e.getValue() - 1));
		return targets.cardinality() == size();
	}

	protected int firstRow() {
		return cells.get(0).getPos().row();
	}

	protected int firstCol() {
		return cells.get(0).getPos().col();
	}

	@Override
	public String toString() {
		return String.format("%s[%s ...]", type, cells.get(0));
	}
}