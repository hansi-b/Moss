package org.hansi_b.moss;

import java.util.BitSet;
import java.util.List;

/**
 * A single field in the Sudoku. Has a row and a column (at least in the square
 * version). Does not maintain its value, but lets the Sudoku do that. It is
 * itself actually immutable.
 */
public class Cell {

	private final Sudoku sudoku;
	private final Pos pos;

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

	/**
	 * @return the inverse of the union of all value BitSets from the groups of this
	 *         cell
	 */
	public BitSet getCandidateBits() {
		final BitSet cellBits = new BitSet(sudoku.size());
		for (final CellGroup group : getGroups())
			cellBits.or(group.valuesAsBits());
		cellBits.flip(0, cellBits.size());
		return cellBits;
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