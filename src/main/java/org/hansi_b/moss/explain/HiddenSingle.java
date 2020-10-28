package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;
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
public class HiddenSingle implements Technique {

	private static final Function<Type, Strategy> strategyByGroup = Strategy.groupTypeMapper(//
			Strategy.HiddenSingleInRow, //
			Strategy.HiddenSingleInCol, //
			Strategy.HiddenSingleInBlock);

	private static Strategy strategyByGroup(final Type type) {
		return strategyByGroup.apply(type);
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku) {

		final CachedCandidates cached = new CachedCandidates();
		final List<Move> moves = new ArrayList<>();
		sudoku.streamGroups().forEach(g -> {
			cached.getCellsByCandidate(g).forEach((i, cells) -> {
				if (cells.size() == 1)
					moves.add(new Move(strategyByGroup(g.type()), cells.first(), i));
			});
		});
		return moves;
	}
}
