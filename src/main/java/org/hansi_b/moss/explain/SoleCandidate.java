package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Sudoku;

/**
 * https://www.kristanix.com/sudokuepic/sudoku-solving-techniques.php - also:
 * https://www.sudokuoftheday.com/techniques/single-candidate/
 *
 * Finds cells where the row+block+column contain all numbers but one.
 */
public class SoleCandidate implements SolvingTechnique {

	@Override
	public List<Move> findPossibleMoves(final Sudoku sudoku) {

		final List<Move> moves = new ArrayList<Move>();

		for (final Cell cell : sudoku) {
			if (cell.getValue() != null)
				continue;
			final BitSet cands = cell.getCandidateBits();
			if (cands.cardinality() == 1)
				moves.add(new Move(Move.Strategy.SoleCandidate, cell, 1 + cands.nextSetBit(0)));

		}
		return moves;
	}
}
