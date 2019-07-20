package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * https://www.sudokuoftheday.com/techniques/single-position/
 *
 * Called Unique Candidate here:
 * https://www.kristanix.com/sudokuepic/sudoku-solving-techniques.php
 *
 * For a given group and missing number, find a unique cell which is the only
 * possible place in that group for that number.
 *
 * In other words:
 * <ol>
 * <li>for each group G
 * <ol>
 * <li>for each empty cell C in G: get the candidates Ca(C)
 * <li>remove all candidates from all other empty cells in G
 * <li>if exactly one is left, this is a move on C
 * </ol>
 * </ol>
 *
 * This would seem to be necessarily symmetric in the fashion that if you
 * identify a move on C relative to G, you will find the same move for C with
 * respect to its other groups. Note that this is less sophisticated than
 * "hidden singles".
 *
 * Is there a way to not do this in quadratic fashion?
 */
public class SinglePosition implements SolvingTechnique {

	@Override
	public List<Move> findPossibleMoves(final Sudoku sudoku) {

		// we will need these again and again
		final Map<Cell, SortedSet<Integer>> candidatesCache = new HashMap<>();
		final List<Move> moves = new ArrayList<Move>();
		for (final CellGroup group : sudoku.iterateGroups(Type.Block)) {

			for (final Cell cell : group) {
				if (cell.getValue() != null)
					continue;
				final SortedSet<Integer> cands = filteredCandidates(group, cell, candidatesCache);
				if (cands.size() == 1)
					moves.add(new Move(Strategy.SinglePosition, cell, cands.first()));
			}
		}

		return moves;
	}

	private static SortedSet<Integer> filteredCandidates(final CellGroup group, final Cell target,
			final Map<Cell, SortedSet<Integer>> candidatesCache) {
		final SortedSet<Integer> cands = new TreeSet<>(candidates(target, candidatesCache));
		for (final Cell otherCell : group) {
			if (otherCell.getValue() != null || otherCell != target)
				cands.removeAll(candidates(otherCell, candidatesCache));
		}
		return cands;
	}

	private static SortedSet<Integer> candidates(final Cell cell,
			final Map<Cell, SortedSet<Integer>> candidatesByCell) {
		return candidatesByCell.computeIfAbsent(cell, c -> c.getCandidates());
	}
}
