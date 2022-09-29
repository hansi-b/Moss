package org.hansi_b.moss;

import java.util.BitSet;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public sealed class CellGroup {

	public static final class Row extends CellGroup {
		Row(final Sudoku sudoku, final List<Cell> cells) {
			super(sudoku, GroupType.Row, cells);
		}

		@Override
		public String toString() {
			return String.format("%s %d", GroupType.Row, firstRow());
		}
	}

	public static final class Col extends CellGroup {
		Col(final Sudoku sudoku, final List<Cell> cells) {
			super(sudoku, GroupType.Col, cells);
		}

		@Override
		public String toString() {
			return String.format("%s %d", GroupType.Col, firstCol());
		}
	}

	public static final class Block extends CellGroup {
		Block(final Sudoku sudoku, final List<Cell> cells) {
			super(sudoku, GroupType.Block, cells);
		}

		@Override
		public String toString() {
			return String.format("%s %d/%d", GroupType.Block, firstRow(), firstCol());
		}
	}

	private final Sudoku sudoku;
	private final GroupType type;
	private final List<Cell> cells;

	private CellGroup(final Sudoku sudoku, final GroupType type, final List<Cell> cells) {
		this.sudoku = sudoku;
		this.type = type;
		this.cells = cells;
	}

	public int size() {
		return cells.size();
	}

	public GroupType type() {
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