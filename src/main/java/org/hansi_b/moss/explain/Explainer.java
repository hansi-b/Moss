package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.Sudoku.CellGroup;
import org.hansi_b.moss.Sudoku.Row;

public class Explainer {

	private final Sudoku sudoku;

	public Explainer(final Sudoku sudoku) {
		this.sudoku = sudoku;

	}

	public List<Move> getPossibleMoves() {

		final List<Move> moves = new ArrayList<Move>();

		for (int rowIdx = 1; rowIdx <= sudoku.size(); rowIdx++) {
			final Row row = sudoku.getRow(rowIdx);
			final Move move = findMissingValue(row);
			if (move != null)
				moves.add(move);
		}
		return moves;
	}

	private Move findMissingValue(final CellGroup cells) {

		final BitSet values = new BitSet(cells.size());

		Cell emptyCell = null;
		for (final Cell c : cells) {
			if (c.getValue() == null) {
				if (emptyCell != null)
					return null;
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

		return canSolve ? new Move(emptyCell, 1 + values.nextClearBit(0)) : null;
	}

}
