package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CollectUtils;
import org.hansi_b.moss.Sudoku;

/**
 * As explained, e.g., on https://www.learn-sudoku.com/xy-wing.html
 *
 * Recipe:
 * <ol>
 * <li>Find all cells with two candidates.
 * <li>Identify three cells A,X,B where
 * <ol>
 * <li>X shares a - different - group with each A and B; and
 * <li>X,A,B have "chained" candidate pairs.
 * </ol>
 * <li>Identify all other empty cells that share groups with both A and B;
 * eliminate the candidate common to A and B from those empty cells.
 * </ol>
 *
 */
public class XyWing implements Technique {

	@Override
	public List<Move> findMoves(final Sudoku sudoku) {

		final CachedCandidates cached = new CachedCandidates();

		final List<Move> moves = new ArrayList<>();

		final List<Cell> candCellPool = sudoku.streamEmptyCells().filter(c -> cached.candidates(c).size() == 2)
				.collect(Collectors.toList());

		/*
		 * Filter by going through empty cells with two candidates
		 *
		 * NB: Be careful with the sets as keys - must not be modified
		 */
		final SortedMap<SortedSet<Integer>, List<Cell>> cellsByCandidates = new TreeMap<>(
				candCellPool.stream().collect(Collectors.groupingBy(cell -> cached.candidates(cell))));

		/*
		 * The cells for each pair of candidate values 
		 */

		/// old approach

		// wing candidates: pairs of candidate cells with no common group
		final Stream<Pair> wingCandidates = CollectUtils.getPairCombinations(candCellPool.size()).stream()
				.map(ind -> Pair.from(candCellPool, ind[0], ind[1]))//
				.filter(Pair::isDisjointGroups);

		wingCandidates.forEach(w -> {
			final SortedSet<Integer> candsA = cached.candidates(w.a);
			final SortedSet<Integer> candsB = cached.candidates(w.b);
			/*
			 * by precondition, they each have two candidates; they need to share one of
			 * them for xy-wing:
			 */
			final Set<Integer> candidateVals = CollectUtils.union(candsA, candsB);
			if (candidateVals.size() != 3)
				return;
			/*
			 * there is one shared value, and the other two values are the required ones to
			 * form a wing:
			 */
			candidateVals.removeAll(CollectUtils.intersection(candsA, candsB));

			final Set<Cell> intersectingEmptyCells = w.getIntersectingEmptyCells(candCellPool);
		});

		return moves;
	}

	static boolean isDisjointGroups(Cell a, Cell b) {
		return Arrays.stream(CellGroup.Type.values()).allMatch(t -> a.getGroup(t) != b.getGroup(t));
	}

}
