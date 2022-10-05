package org.hansi_b.moss.explain;

import static org.hansib.sundries.CollectUtils.difference;
import static org.hansib.sundries.CollectUtils.mapMap;

import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.stream.Stream;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
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
	public Stream<Move> findMoves(final CellGroup group, final Strategy strategy, final PencilMarks marks) {

		final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate = marks.getCellsByCandidateFiltered(group, 2);

		return Elimination.Builder.collectNonEmpty(mapMap(cellsByCandidate, (upperCandidate,
				upperCells) -> mapMap(cellsByCandidate.headMap(upperCandidate), (lowerCandiate, lowerCells) -> {
					if (!upperCells.equals(lowerCells))
						return null;

					final Set<Integer> pair = Set.of(upperCandidate, lowerCandiate);
					final Builder builder = new Elimination.Builder(strategy);
					upperCells.forEach(cell -> {
						final SortedSet<Integer> others = difference(marks.candidates(cell), pair);
						if (!others.isEmpty())
							builder.with(cell, others);
					});
					return builder;
				})).flatMap(Function.identity()));
	}
}
