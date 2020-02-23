package org.hansi_b.moss;

import java.util.List;
import java.util.SortedSet;

/**
 * A single field in the Sudoku. Has a row and a column (at least in the square
 * version). Does not maintain its value, but lets the Sudoku do that. It is
 * itself actually immutable.
 */
public class Cell {

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
		for (final CellGroup group : getGroups())
			candidates.removeAll(group.values());
		return candidates;
	}

	public Pos getPos() {
		return pos;
	}

	public CellGroup getGroup(final CellGroup.Type groupType) {
		return sudoku.getGroup(groupType, pos);
	}

	public List<CellGroup> getGroups() {
		return sudoku.getGroups(pos);
	}

	public void setValue(final Integer newValue) {
		sudoku.set(pos.row, pos.col, newValue);
	}

	@Override
	public String toString() {
		return String.format("Cell%s", pos);
	}
}