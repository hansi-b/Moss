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
 * Also known as: Singleton, Sole Candidate
 *
 * Finds cells where the combinations of the cell's row+block+column contain all
 * numbers but one.
 */
public class NakedSingle implements SolvingTechnique {

	@Override
	public List<Move> findMoves(final Sudoku sudoku) {

		final List<Move> moves = new ArrayList<Move>();

		for (final Cell cell : sudoku.iterateEmptyCells()) {
			final Set<Integer> cands = cell.getCandidates();
			if (cands.size() == 1)
				moves.add(new Move(Move.Strategy.NakedSingle, cell, cands.iterator().next()));
		}
		return moves;
	}
}
