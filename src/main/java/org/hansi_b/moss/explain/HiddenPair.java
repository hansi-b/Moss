package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CollectUtils;
import org.hansi_b.moss.explain.Elimination.Builder;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * As described in http://www.sudoku-space.de/sudoku-loesungstechniken/
 *
 * For a given group and missing number, find a tuple of cells which are the
 * only possible cells for the same tuple of candidates (or subset thereof, with
 * some restriction).
 */
public class HiddenPair extends GroupBasedTechnique {

	public HiddenPair() {
		super(Strategy.HiddenPairInRow, //
				Strategy.HiddenPairInCol, //
				Strategy.HiddenPairInBlock);
	}

	@Override
	public List<Move> findMoves(final CellGroup group, final Strategy strategy, final PencilMarks marks) {

		final List<Move> moves = new ArrayList<>();

		final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate = marks.getCellsByCandidateFiltered(group, 2);

		cellsByCandidate.forEach((upperCandidate, upperCells) -> cellsByCandidate.headMap(upperCandidate)
				.forEach((lowerCandiate, lowerCells) -> {
					if (!upperCells.equals(lowerCells))
						return;

					final Set<Integer> pair = Set.of(upperCandidate, lowerCandiate);
					final Builder builder = new Elimination.Builder(strategy);
					upperCells.forEach(cell -> {
						final SortedSet<Integer> others = CollectUtils.difference(marks.candidates(cell), pair);
						if (!others.isEmpty())
							builder.with(Collections.singleton(cell), others);
					});
					if (!builder.isEmpty())
						moves.add(builder.build());
				}));
		return moves;
	}
}
