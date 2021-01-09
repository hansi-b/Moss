package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

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
public class NakedTriple implements Technique {
	private static final Function<Type, Move.Strategy> strategyByGroup = Move.Strategy.groupTypeMapper(//
			Move.Strategy.NakedTripleInRow, //
			Move.Strategy.NakedTripleInCol, //
			Move.Strategy.NakedTripleInBlock);

	private static Move.Strategy strategyByGroup(final CellGroup group) {
		return strategyByGroup.apply(group.type());
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku, final PencilMarks marks) {

		final List<Move> moves = new ArrayList<>();
		sudoku.streamGroups().forEach(group -> findMovesInGroup(group, marks, moves));
		return moves;
	}

	private static void findMovesInGroup(final CellGroup group, final PencilMarks marks, final List<Move> moves) {

		final SortedMap<Cell, SortedSet<Integer>> candsByCell = marks.getCandidatesByCell(group).entrySet().stream()
				.filter(e -> e.getValue().size() == 2 || e.getValue().size() == 3)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, Cell::newPosSortedMap));
		if (candsByCell.isEmpty())
			return;

		final SortedSet<Cell> sortedCells = Cell.newPosSortedSet(candsByCell.keySet());

		final List<SortedSet<Cell>> possibleCombinations = CollectUtils.combinations(sortedCells, 3).stream()
				.filter(combi -> getCandidates(combi, candsByCell).size() == 3).collect(Collectors.toList());
		if (possibleCombinations.isEmpty())
			return;

		final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate = marks.getCellsByCandidate(group);
		possibleCombinations.forEach(combi -> {
			final Elimination.Builder moveBuilder = new Elimination.Builder(strategyByGroup(group));
			getCandidates(combi, candsByCell).forEach(cand -> CollectUtils.difference(cellsByCandidate.get(cand), combi)
					.forEach(cell -> moveBuilder.with(cell, cand)));
			final Elimination move = moveBuilder.build();
			if (!move.isEmpty())
				moves.add(move);
		});
	}

	private static SortedSet<Integer> getCandidates(final SortedSet<Cell> cells,
			final SortedMap<Cell, SortedSet<Integer>> candsByCell) {
		return cells.stream().flatMap(c -> candsByCell.get(c).stream()).collect(Collectors.toCollection(TreeSet::new));
	}
}
