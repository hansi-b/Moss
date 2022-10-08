package org.hansi_b.moss;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;
import static org.hansib.sundries.CollectUtils.difference;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 * A single field in the Sudoku. Has a row and a column (at least in the square
 * version). Does not maintain its value, but lets the Sudoku do that. It is
 * itself actually immutable.
 */
public record Cell(Sudoku sudoku, Pos pos) {

	public static final Comparator<Cell> positionComparator = (c1, c2) -> Pos.positionComparator.compare(c1.pos,
			c2.pos);

	public Sudoku getSudoku() {
		return sudoku;
	}

	public Integer getValue() {
		return sudoku.getValue(pos.row(), pos.col());
	}

	public boolean isEmpty() {
		return sudoku.getValue(pos.row(), pos.col()) == null;
	}

	/**
	 * @return a set of the values not set in any of the groups of this cell
	 */
	public SortedSet<Integer> getCandidates() {
		return difference(sudoku.possibleValues(), //
				streamGroups().flatMap(CellGroup::streamFilledCells).distinct().map(Cell::getValue).collect(toSet()));
	}

	public Pos getPos() {
		return pos;
	}

	public CellGroup getGroup(final GroupType groupType) {
		return sudoku.getGroup(groupType, pos);
	}

	public boolean sharesAnyGroup(final Cell other) {
		return this == other || Arrays.stream(GroupType.values()).anyMatch(t -> getGroup(t) == other.getGroup(t));
	}

	public Stream<CellGroup> streamGroups() {
		return sudoku.streamGroups(pos);
	}

	/**
	 * @return a stream of all empty cells from all groups of this cell, including
	 *         this cell, without duplicates, in the order row/column/block
	 */
	public Stream<Cell> streamEmptyCellsFromGroups() {
		return streamGroups().flatMap(CellGroup::streamEmptyCells).distinct();
	}

	public void setValue(final Integer newValue) {
		sudoku.set(pos.row(), pos.col(), newValue);
	}

	public static Set<CellGroup> toGroups(final Collection<Cell> cells, final GroupType groupType) {
		return cells.stream().map(c -> c.getGroup(groupType)).collect(toSet());
	}

	/**
	 * @return an empty sorted set with sorts Cells by their position
	 */
	public static SortedSet<Cell> newPosSortedSet() {
		return new TreeSet<>(positionComparator);
	}

	/**
	 * @return a set with the argument Cells sorted by their position
	 */
	public static SortedSet<Cell> newPosSortedSet(final Collection<Cell> cells) {
		final TreeSet<Cell> res = new TreeSet<>(positionComparator);
		res.addAll(cells);
		return res;
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
		return cells.collect(toCollection(Cell::newPosSortedSet));
	}

	@Override
	public String toString() {
		return String.format("Cell%s", pos);
	}
}