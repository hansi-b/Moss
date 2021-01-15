package org.hansi_b.moss.explain;

import static org.hansi_b.moss.CollectUtils.combinations;
import static org.hansi_b.moss.CollectUtils.difference;
import static org.hansi_b.moss.CollectUtils.filterMap;

import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.explain.Move.Strategy;

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
	public List<Move> findMoves(final CellGroup group, final Strategy strategy, final PencilMarks marks) {

		final SortedMap<Cell, SortedSet<Integer>> candsByCell = filterMap(marks.getCandidatesByCell(group),
				(c, cands) -> cands.size() == 2 || cands.size() == 3, Cell::newPosSortedMap);
		if (candsByCell.isEmpty())
			return Collections.emptyList();

		final SortedSet<Cell> sortedCells = Cell.newPosSortedSet(candsByCell.keySet());

		final List<SortedSet<Cell>> possibleCombinations = combinations(sortedCells, 3)
				.filter(combi -> getCandidates(combi, candsByCell).size() == 3).collect(Collectors.toList());
		if (possibleCombinations.isEmpty())
			return Collections.emptyList();

		final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate = marks.getCellsByCandidate(group);
		return Elimination.Builder.collectNonEmpty(possibleCombinations.stream().map(combi -> {
			final Elimination.Builder moveBuilder = new Elimination.Builder(strategy);
			getCandidates(combi, candsByCell).forEach(cand -> difference(cellsByCandidate.get(cand), combi)
					.forEach(cell -> moveBuilder.with(cell, cand)));
			return moveBuilder;
		}));
	}

	private static SortedSet<Integer> getCandidates(final SortedSet<Cell> cells,
			final SortedMap<Cell, SortedSet<Integer>> candsByCell) {
		return cells.stream().flatMap(c -> candsByCell.get(c).stream()).collect(Collectors.toCollection(TreeSet::new));
	}
}
