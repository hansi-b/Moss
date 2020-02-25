package org.hansi_b.moss;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CellGroup implements Iterable<Cell> {

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
	}

	public static class Col extends CellGroup {
		Col(final Sudoku sudoku, final List<Cell> cells) {
			super(sudoku, Type.Col, cells);
		}
	}

	public static class Block extends CellGroup {
		Block(final Sudoku sudoku, final List<Cell> cells) {
			super(sudoku, Type.Block, cells);
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
		return cells.stream().map(Cell::getValue).collect(Collectors.toList());
	}

	/**
	 * The possible values for the Sudoku not in any cell of this group.
	 *
	 * @return a fresh sorted set on numbers missing in this group
	 */
	public Set<Integer> missing() {

		final Set<Integer> possibleValues = sudoku.possibleValues();
		for (final Cell c : this) {
			final Integer value = c.getValue();
			if (value != null)
				possibleValues.remove(value);
		}
		return possibleValues;
	}

	public Stream<Cell> emptyCells() {
		return cells.stream().filter(Cell::isEmpty);
	}

	@Override
	public Iterator<Cell> iterator() {
		return cells.iterator();
	}

	@Override
	public String toString() {
		return String.format("%s%s", type, cells.toString());
	}

	public boolean isSolved() {

		final BitSet targets = new BitSet(size());
		for (final Cell e : this) {
			final Integer v = e.getValue();
			if (v != null)
				targets.set(v - 1);
		}

		return targets.cardinality() == size();
	}
}