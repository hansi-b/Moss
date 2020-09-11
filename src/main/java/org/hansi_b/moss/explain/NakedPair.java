package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * As explained, e.g., on https://www.learn-sudoku.com/naked-pairs.html
 *
 * For each group, determine the candidates. If a pair of cells has the same two
 * candidates, eliminate these from all other cell candidates. Then, if a cell
 * remains with only a single candidate, that is the move.
 */
public class NakedPair implements Technique {
	private static final Function<Type, Strategy> strategyByGroup = Strategy.typeMapper(//
			Strategy.NakedPairInRow, //
			Strategy.NakedPairInCol, //
			Strategy.NakedPairInBlock);

	private static Strategy strategyByGroup(final CellGroup group) {
		return strategyByGroup.apply(group.type());
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku) {

		final var cached = new CachedCandidates();

		final List<Move> moves = new ArrayList<>();
		sudoku.iterateGroups().forEach(group -> findMovesInGroup(cached, group, moves));

		return moves;
	}

	private static void findMovesInGroup(final CachedCandidates cached, final CellGroup group,
			final List<Move> resultMoves) {

		final Set<Cell> possibleTargets = new HashSet<>();
		final Map<Set<Integer>, Set<Cell>> cellsByCandidates = new HashMap<>();

		group.emptyCells().forEach(cell -> {
			final Set<Integer> cands = cached.candidates(cell);
			if (cands.size() == 2)
				cellsByCandidates.computeIfAbsent(cands, k -> new HashSet<>()).add(cell);
			else if (cands.size() == 3)
				possibleTargets.add(cell);
		});

		if (possibleTargets.isEmpty())
			return;

		for (final Entry<Set<Integer>, Set<Cell>> entry : cellsByCandidates.entrySet()) {
			if (entry.getValue().size() != 2)
				continue;

			final Set<Integer> nakedPair = entry.getKey();
			possibleTargets.stream().filter(c -> cached.candidates(c).containsAll(nakedPair)).forEach(c -> {
				final var candsCopy = new HashSet<>(cached.candidates(c));
				candsCopy.removeAll(nakedPair);
				resultMoves.add(new Move(strategyByGroup(group), c, candsCopy.iterator().next()));
			});
		}
	}
}
