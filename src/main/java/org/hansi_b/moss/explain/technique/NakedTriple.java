package org.hansi_b.moss.explain.technique;

import static org.hansib.sundries.CollectUtils.combinations;
import static org.hansib.sundries.CollectUtils.difference;
import static org.hansib.sundries.CollectUtils.filterMap;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.explain.Elimination;
import org.hansi_b.moss.explain.Move;
import org.hansi_b.moss.explain.Move.Strategy;
import org.hansi_b.moss.explain.PencilMarks;

/**
 * As explained, e.g., on https://sudoku9x9.com/naked_pair.html (along with
 * pairs)
 */
public class NakedTriple extends GroupBasedTechnique {

	public NakedTriple() {
		super(Strategy.NakedTripleInRow, //
				Strategy.NakedTripleInCol, //
				Strategy.NakedTripleInBlock);
	}

	@Override
	public Stream<Move> findMoves(final CellGroup group, final Strategy strategy, final PencilMarks marks) {

		final SortedMap<Cell, SortedSet<Integer>> candsByCellFiltered = filterMap(marks.getCandidatesByCell(group),
				(c, cands) -> cands.size() == 2 || cands.size() == 3, Cell::newPosSortedMap);
		if (candsByCellFiltered.isEmpty())
			return Stream.empty();

		final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate = marks.getCellsByCandidate(group);
		return Elimination.Builder.collectNonEmpty(combinations(Cell.newPosSortedSet(candsByCellFiltered.keySet()), 3)
				.map(combi -> builderFromCombi(combi, strategy, candsByCellFiltered, cellsByCandidate)));
	}

	private static Elimination.Builder builderFromCombi(final SortedSet<Cell> combi, final Strategy strategy,
			final SortedMap<Cell, SortedSet<Integer>> candsByCellFiltered,
			final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate) {
		final SortedSet<Integer> candidates = new TreeSet<>();
		combi.forEach(c -> candidates.addAll(candsByCellFiltered.get(c)));
		if (candidates.size() != 3)
			return null;
		final Elimination.Builder moveBuilder = new Elimination.Builder(strategy);
		candidates.forEach(
				cand -> difference(cellsByCandidate.get(cand), combi).forEach(cell -> moveBuilder.with(cell, cand)));
		return moveBuilder;
	}
}
