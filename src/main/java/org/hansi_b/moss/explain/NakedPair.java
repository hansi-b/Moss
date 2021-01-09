package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.CollectUtils;
import org.hansi_b.moss.Sudoku;

/**
 * As explained, e.g., on https://www.learn-sudoku.com/naked-pairs.html
 *
 * For each group, determine the candidates. If a pair of cells has the same two
 * candidates, eliminate these from all other cell candidates. Then, if a cell
 * remains with only a single candidate, that is the move.
 */
public class NakedPair implements Technique {
	private static final Function<Type, Move.Strategy> strategyByGroup = Move.Strategy.groupTypeMapper(//
			Move.Strategy.NakedPairInRow, //
			Move.Strategy.NakedPairInCol, //
			Move.Strategy.NakedPairInBlock);

	private static Move.Strategy strategyByGroup(final CellGroup group) {
		return strategyByGroup.apply(group.type());
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku, final PencilMarks marks) {

		final List<Move> moves = new ArrayList<>();
		sudoku.streamGroups().forEach(group -> findMovesInGroup(marks, group, moves));
		return moves;
	}

	private static void findMovesInGroup(final PencilMarks marks, final CellGroup group, final List<Move> resultMoves) {

		final Map<Set<Integer>, Set<Cell>> cellsByPairs = findCellsByPairs(marks, group);

		for (final Entry<Set<Integer>, Set<Cell>> candidatePairEntry : cellsByPairs.entrySet()) {
			final Set<Cell> nakedPairCells = candidatePairEntry.getValue();
			if (nakedPairCells.size() != 2)
				continue;
			final Set<Integer> nakedPair = candidatePairEntry.getKey();

			final Elimination.Builder builder = new Elimination.Builder(strategyByGroup(group));
			group.streamEmptyCells().filter(c -> !nakedPairCells.contains(c)).forEach(c -> {
				final Set<Integer> candsToRemove = CollectUtils.intersection(marks.candidates(c), nakedPair);
				candsToRemove.forEach(cand -> builder.with(c, cand));
			});
			final Elimination move = builder.build();
			if (!move.isEmpty()) {
				resultMoves.add(move);
			}
		}
	}

	private static Map<Set<Integer>, Set<Cell>> findCellsByPairs(final PencilMarks marks, final CellGroup group) {
		final Map<Set<Integer>, Set<Cell>> cellsByPairs = new HashMap<>();

		group.streamEmptyCells().forEach(cell -> {
			final SortedSet<Integer> cands = marks.candidates(cell);
			if (cands.size() == 2)
				cellsByPairs.computeIfAbsent(cands, k -> new HashSet<>()).add(cell);
		});
		return cellsByPairs;
	}
}
