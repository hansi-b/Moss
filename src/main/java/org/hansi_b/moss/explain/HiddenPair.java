package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.function.Function;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.CollectUtils;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Elimination.Builder;

/**
 * As described in http://www.sudoku-space.de/sudoku-loesungstechniken/
 *
 * For a given group and missing number, find a tuple of cells which are the
 * only possible cells for the same tuple of candidates (or subset thereof, with
 * some restriction).
 */
public class HiddenPair implements Technique {

	private static final Function<Type, Move.Strategy> strategyByGroup = Move.Strategy.groupTypeMapper(//
			Move.Strategy.HiddenPairInRow, //
			Move.Strategy.HiddenPairInCol, //
			Move.Strategy.HiddenPairInBlock);

	private static Move.Strategy strategyByGroup(final Type type) {
		return strategyByGroup.apply(type);
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku, final PencilMarks marks) {

		final List<Move> moves = new ArrayList<>();

		sudoku.streamGroups().forEach(g -> {
			final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate = marks.getCellsByCandidateFiltered(g,
					e -> e.getValue().size() == 2);

			cellsByCandidate.forEach((upperCandidate, upperCells) -> cellsByCandidate.headMap(upperCandidate)
					.forEach((lowerCandiate, lowerCells) -> {
						if (!upperCells.equals(lowerCells))
							return;

						final Set<Integer> pair = Set.of(upperCandidate, lowerCandiate);
						final Builder builder = new Elimination.Builder(strategyByGroup(g.type()));
						upperCells.forEach(cell -> {
							final SortedSet<Integer> others = CollectUtils.difference(marks.candidates(cell), pair);
							if (!others.isEmpty())
								builder.with(Collections.singleton(cell), others);
						});
						if (!builder.isEmpty())
							moves.add(builder.build());
					}));
		});
		return moves;
	}

}
