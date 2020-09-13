package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * As described in http://www.sudoku-space.de/sudoku-loesungstechniken/
 *
 * For a given group and missing number, find a unique cell which is the only
 * possible place in that group for that number.
 *
 * In other words:
 * <ol>
 * <li>for each group G
 * <ol>
 * <li>for each empty cell C in G: get its candidates Ca(C)
 * <li>remove all candidates that are in any other empty cell in G
 * <li>if exactly one candidate is left, this is a move on C
 * </ol>
 * </ol>
 *
 * Also known as Unique Candidate.
 *
 * This would sometimes seem to be symmetric in the fashion that if you identify
 * a move on C relative to G, you can find the same move for C with respect to
 * its other groups.
 */
public class HiddenSingle implements Technique {

	private static final Function<Type, Strategy> strategyByGroup = Strategy.typeMapper(//
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
		sudoku.streamEmptyCells().forEach(c -> {
			c.streamGroups().forEach(g -> {
				final SortedSet<Integer> cands = filteredCandidates(g, c, cached);
				if (cands.size() == 1) {
					moves.add(new Move(strategyByGroup(g.type()), c, cands.first()));
				}
			});
		});

		return moves;
	}

	private static SortedSet<Integer> filteredCandidates(final CellGroup group, final Cell target,
			final CachedCandidates candidatesCache) {
		final SortedSet<Integer> cands = new TreeSet<>(candidatesCache.candidates(target));
		group.forEach(c -> {
			if (c != target && c.isEmpty())
				cands.removeAll(candidatesCache.candidates(c));
		});
		return cands;
	}
}
