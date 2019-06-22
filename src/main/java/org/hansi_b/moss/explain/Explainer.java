package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.Sudoku.CellGroup;

public class Explainer {

	private final Sudoku sudoku;

	public Explainer(final Sudoku sudoku) {
		this.sudoku = sudoku;

	}

	public List<Move> getPossibleMoves() {

		final List<Move> moves = new ArrayList<Move>();

		for (int rowIdx = 1; rowIdx <= sudoku.size(); rowIdx++) {
			addMove(moves, sudoku.getRow(rowIdx));
			addMove(moves, sudoku.getCol(rowIdx));
		}
		return moves;
	}

	private void addMove(final List<Move> moves, final CellGroup cells) {
		final BitSet values = new BitSet(cells.size());

		Cell emptyCell = null;
		for (final Cell c : cells) {
			if (c.getValue() == null) {
				if (emptyCell != null)
					return;
				emptyCell = c;
			} else {
				values.set(c.getValue() - 1);
			}
		}

		/*
		 * we want to have found and empty cell, and all values but one must have
		 * occurred
		 */
		final boolean canSolve = emptyCell != null && values.cardinality() == cells.size() - 1;

		if (canSolve)
			moves.add(new Move(emptyCell, 1 + values.nextClearBit(0)));
	}
}
