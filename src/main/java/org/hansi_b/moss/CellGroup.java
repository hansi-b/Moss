package org.hansi_b.moss;

import static java.util.stream.Collectors.toSet;
import static org.hansib.sundries.CollectUtils.difference;

import java.util.BitSet;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Stream;

public record CellGroup(Sudoku sudoku, GroupType type, List<Cell> cells) {

	public CellGroup {
		cells = List.copyOf(cells);
	}

	public int size() {
		return cells.size();
	}

	/**
	 * The values of the cells in this group. May contain duplicates and empty,
	 * i.e., null values.
	 *
	 * @return a fresh, mutable List of the values in this group, in order of cell
	 *         iteration
	 */
	public List<Integer> values() {
		return streamAllCells().map(Cell::getValue).toList();
	}

	/**
	 * The possible values for the Sudoku not in any cell of this group.
	 *
	 * @return a fresh sorted set on numbers missing in this group
	 */
	public SortedSet<Integer> missing() {

		return difference(sudoku.possibleValues(), //
				streamFilledCells().map(Cell::getValue).collect(toSet()));
	}

	public Stream<Cell> streamEmptyCells() {
		return streamAllCells().filter(Cell::isEmpty);
	}

	public Stream<Cell> streamFilledCells() {
		return streamAllCells().filter(c -> !c.isEmpty());
	}

	Stream<Cell> streamAllCells() {
		return cells.stream();
	}

	public boolean isSolved() {

		final BitSet targets = new BitSet(size());
		streamFilledCells().forEach(e -> targets.set(e.getValue() - 1));
		return targets.cardinality() == size();
	}

	private int firstRow() {
		return cells.get(0).getPos().row();
	}

	private int firstCol() {
		return cells.get(0).getPos().col();
	}

	@Override
	public String toString() {
		String idx = switch (type) {
		case Row -> Integer.toString(firstRow());
		case Col -> Integer.toString(firstCol());
		case Block -> String.format("%d/%d", firstRow(), firstCol());
		};
		return String.format("%s %s", type, idx);
	}
}