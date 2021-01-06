package org.hansi_b.moss;

import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A single field in the Sudoku. Has a row and a column (at least in the square
 * version). Does not maintain its value, but lets the Sudoku do that. It is
 * itself actually immutable.
 */
public class Cell {

	private static final Comparator<Cell> positionComparator = (c1, c2) -> Pos.positionComparator.compare(c1.pos,
			c2.pos);

	private final Sudoku sudoku;
	private final Pos pos;

	/**
	 * Create a cell for the given Sudoku and position. Should not have to be
	 * called, but is constructed during Sudoku initialization.
	 */
	Cell(final Sudoku sudoku, final Pos pos) {
		this.sudoku = sudoku;
		this.pos = pos;
	}

	public Sudoku getSudoku() {
		return sudoku;
	}

	public Integer getValue() {
		return sudoku.getValue(pos.row, pos.col);
	}

	public boolean isEmpty() {
		return sudoku.getValue(pos.row, pos.col) == null;
	}

	/**
	 * @return a set of the values not set in any of the groups of this cell
	 */
	public SortedSet<Integer> getCandidates() {
		final SortedSet<Integer> candidates = sudoku.possibleValues();
		streamGroups().forEach(g -> candidates.removeAll(g.values()));
		return candidates;
	}

	public Pos getPos() {
		return pos;
	}

	public CellGroup getGroup(final CellGroup.Type groupType) {
		return sudoku.getGroup(groupType, pos);
	}

	public boolean sharesAnyGroup(final Cell other) {
		return this == other || Arrays.stream(CellGroup.Type.values()).anyMatch(t -> getGroup(t) == other.getGroup(t));
	}

	public Stream<CellGroup> streamGroups() {
		return sudoku.streamGroups(pos);
	}

	public Stream<Cell> streamEmptyCellsFromGroups() {
		return streamGroups().flatMap(CellGroup::streamEmptyCells);
	}

	public void setValue(final Integer newValue) {
		sudoku.set(pos.row, pos.col, newValue);
	}

	/**
	 * @return an empty sorted set with sorts Cells by their position
	 */
	public static SortedSet<Cell> newPosSortedSet() {
		return new TreeSet<>(positionComparator);
	}

	/**
	 * @return an empty sorted set with sorts Cells by their position
	 */
	public static <E> SortedMap<Cell, E> newPosSortedMap() {
		return new TreeMap<>(positionComparator);
	}

	/**
	 * @return a set sorting on position of the argument stream of cells
	 */
	public static SortedSet<Cell> collect(final Stream<Cell> cells) {
		return cells.collect(Collectors.toCollection(Cell::newPosSortedSet));
	}

	@Override
	public String toString() {
		return String.format("Cell%s", pos);
	}
}