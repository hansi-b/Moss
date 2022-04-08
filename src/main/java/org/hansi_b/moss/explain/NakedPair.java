package org.hansi_b.moss.explain;

import static org.hansib.sundries.CollectUtils.filterMap;
import static org.hansib.sundries.CollectUtils.intersection;
import static org.hansib.sundries.CollectUtils.invertMap;
import static org.hansib.sundries.CollectUtils.mapMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Stream;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * As explained, e.g., on https://www.learn-sudoku.com/naked-pairs.html
 *
 * For each group, determine the candidates. If a pair of cells has the same two
 * candidates, eliminate these from all other cell candidates.
 */
public class NakedPair extends GroupBasedTechnique {
	NakedPair() {
		super(Strategy.NakedPairInRow, //
				Strategy.NakedPairInCol, //
				Strategy.NakedPairInBlock);
	}

	@Override
	public Stream<Move> findMoves(final CellGroup group, final Strategy strategy, final PencilMarks marks) {

		final Map<SortedSet<Integer>, Set<Cell>> cellsByPairs = invertMap(
				filterMap(marks.getCandidatesByCell(group), (cell, cands) -> cands.size() == 2, Cell::newPosSortedMap),
				v -> new HashSet<>(), new HashMap<>());
		final Map<SortedSet<Integer>, Set<Cell>> pairsByPairs = filterMap(cellsByPairs,
				(cands, cells) -> cells.size() == 2, HashMap::new);

		return Elimination.Builder.collectNonEmpty(mapMap(pairsByPairs, (nakedPair, nakedPairCells) -> {
			final Elimination.Builder builder = new Elimination.Builder(strategy);
			group.streamEmptyCells().filter(c -> !nakedPairCells.contains(c))
					.forEach(c -> intersection(marks.candidates(c), nakedPair).forEach(cand -> builder.with(c, cand)));
			return builder;
		}));
	}
}
