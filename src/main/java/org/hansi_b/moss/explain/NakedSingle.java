package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Sudoku;

/**
 *
 * After https://www.sudoku-solutions.com/index.php?page=solvingnakedsubsets
 *
 * https://www.kristanix.com/sudokuepic/sudoku-solving-techniques.php - also:
 * https://www.sudokuoftheday.com/techniques/single-candidate/
 *
 * Finds cells where the row+block+column contain all numbers but one.
 */
public class NakedSingle implements SolvingTechnique {

	@Override
	public List<Move> findPossibleMoves(final Sudoku sudoku) {

		final List<Move> moves = new ArrayList<Move>();

		for (final Cell cell : sudoku) {
			if (cell.getValue() != null)
				continue;
			final Set<Integer> cands = cell.getCandidates();
			if (cands.size() == 1)
				moves.add(new Move(Move.Strategy.NakedSingle, cell, cands.iterator().next()));

		}
		return moves;
	}
}
