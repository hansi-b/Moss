package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.CollectUtils;

/**
 * As explained, e.g., on https://sudoku9x9.com/naked_pair.html (along with
 * pairs)
 */
public class NakedTriple extends GroupBasedTechnique {
	private static final Function<Type, Move.Strategy> strategyByGroup = Move.Strategy.groupTypeMapper(//
			Move.Strategy.NakedTripleInRow, //
			Move.Strategy.NakedTripleInCol, //
			Move.Strategy.NakedTripleInBlock);

	private static Move.Strategy strategyByGroup(final CellGroup group) {
		return strategyByGroup.apply(group.type());
	}

	@Override
	public List<Move> findMoves(final CellGroup group, final PencilMarks marks) {

		final SortedMap<Cell, SortedSet<Integer>> candsByCell = marks.getCandidatesByCellFiltered(group,
				e -> e.getValue().size() == 2 || e.getValue().size() == 3);
		if (candsByCell.isEmpty())
			return Collections.emptyList();

		final SortedSet<Cell> sortedCells = Cell.newPosSortedSet(candsByCell.keySet());

		final List<SortedSet<Cell>> possibleCombinations = CollectUtils.combinations(sortedCells, 3).stream()
				.filter(combi -> getCandidates(combi, candsByCell).size() == 3).collect(Collectors.toList());
		if (possibleCombinations.isEmpty())
			return Collections.emptyList();

		final List<Move> moves = new ArrayList<>();
		final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate = marks.getCellsByCandidate(group);
		possibleCombinations.forEach(combi -> {
			final Elimination.Builder moveBuilder = new Elimination.Builder(strategyByGroup(group));
			getCandidates(combi, candsByCell).forEach(cand -> CollectUtils.difference(cellsByCandidate.get(cand), combi)
					.forEach(cell -> moveBuilder.with(cell, cand)));
			if (!moveBuilder.isEmpty())
				moves.add(moveBuilder.build());
		});
		return moves;
	}

	private static SortedSet<Integer> getCandidates(final SortedSet<Cell> cells,
			final SortedMap<Cell, SortedSet<Integer>> candsByCell) {
		return cells.stream().flatMap(c -> candsByCell.get(c).stream()).collect(Collectors.toCollection(TreeSet::new));
	}
}
