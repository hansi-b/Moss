package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.List;

import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * As described in http://www.sudoku-space.de/sudoku-loesungstechniken/
 *
 * For a given group and missing number, find a unique cell which is the only
 * possible place in that group for that number.
 *
 * Approach:
 * <ol>
 * <li>iterate over groups
 * <li>build map from candidates to empty cells in group
 * <li>candidates with exactly one empty cell are hidden singles with regard to
 * that group
 * </ol>
 *
 * Also known as Unique Candidate.
 *
 * This would sometimes seem to be symmetric in the fashion that if you identify
 * a move on C relative to G, you can find the same move for C with respect to
 * its other groups.
 */
public class HiddenSingle extends GroupBasedTechnique {

	public HiddenSingle() {
		super(Strategy.HiddenSingleInRow, //
				Strategy.HiddenSingleInCol, //
				Strategy.HiddenSingleInBlock);
	}

	@Override
	public List<Move> findMoves(final CellGroup g, final Strategy strategy, final PencilMarks marks) {

		final List<Move> moves = new ArrayList<>();
		marks.getCellsByCandidateFiltered(g, e -> e.getValue().size() == 1)
				.forEach((cand, cells) -> moves.add(new Insertion(strategy, cells.first(), cand)));
		return moves;
	}
}
