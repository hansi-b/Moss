package org.hansi_b.moss.explain;

import static org.hansi_b.moss.CollectUtils.filterMap;
import static org.hansi_b.moss.CollectUtils.intersection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * As explained, e.g., on https://www.learn-sudoku.com/naked-pairs.html
 *
 * For each group, determine the candidates. If a pair of cells has the same two
 * candidates, eliminate these from all other cell candidates. Then, if a cell
 * remains with only a single candidate, that is the move.
 */
public class NakedPair extends GroupBasedTechnique {
	NakedPair() {
		super(Strategy.NakedPairInRow, //
				Strategy.NakedPairInCol, //
				Strategy.NakedPairInBlock);
	}

	@Override
	public List<Move> findMoves(final CellGroup group, final Strategy strategy, final PencilMarks marks) {

		final List<Move> resultMoves = new ArrayList<>();
		final Map<Set<Integer>, Set<Cell>> cellsByPairs = findCellsByPairs(marks, group);

		for (final Entry<Set<Integer>, Set<Cell>> candidatePairEntry : cellsByPairs.entrySet()) {
			final Set<Cell> nakedPairCells = candidatePairEntry.getValue();
			if (nakedPairCells.size() != 2)
				continue;
			final Set<Integer> nakedPair = candidatePairEntry.getKey();

			final Elimination.Builder builder = new Elimination.Builder(strategy);
			group.streamEmptyCells().filter(c -> !nakedPairCells.contains(c))
					.forEach(c -> intersection(marks.candidates(c), nakedPair).forEach(cand -> builder.with(c, cand)));
			if (!builder.isEmpty()) {
				resultMoves.add(builder.build());
			}
		}
		return resultMoves;
	}

	private static Map<Set<Integer>, Set<Cell>> findCellsByPairs(final PencilMarks marks, final CellGroup group) {
		final Map<Set<Integer>, Set<Cell>> cellsByPairs = new HashMap<>();
		filterMap(marks.getCandidatesByCell(group), (cell, cands) -> cands.size() == 2, Cell::newPosSortedMap)
				.forEach((cell, cands) -> cellsByPairs.computeIfAbsent(cands, k -> new HashSet<>()).add(cell));
		return cellsByPairs;
	}
}
