package org.hansi_b.moss.explain;

import static org.hansi_b.moss.CollectUtils.intersection;
import static org.hansi_b.moss.CollectUtils.pairCombinations;
import static org.hansi_b.moss.CollectUtils.union;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Elimination.Builder;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * As explained, e.g., on https://www.learn-sudoku.com/x-wing.html
 *
 * Recipe:
 * <ol>
 * <li>For all rows/columns, filter for candidates with two marks matched in
 * another row/column.
 * <li>From the crossing columns/rows, eliminate that candidate.
 * </ol>
 *
 */
public class XWing implements Technique {

	enum WingType {

		XWingFromRow(Type.Row, Type.Col, Move.Strategy.XWingFromRow), //
		XWingFromCol(Type.Col, Type.Row, Move.Strategy.XWingFromCol);

		private final Type pairsType;
		private final Type crossingType;
		private final Strategy moveStrategy;

		WingType(final Type pairGroups, final Type crossingType, final Strategy moveStrategy) {
			this.pairsType = pairGroups;
			this.crossingType = crossingType;
			this.moveStrategy = moveStrategy;
		}
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku, final PencilMarks marks) {

		final List<Move> moves = new ArrayList<>();

		for (final WingType wingType : WingType.values())
			pairCombinations(eligibleCellSets(sudoku, marks, wingType.pairsType)).forEach(eligiblePair -> {
				final Map<Integer, SortedSet<Cell>> one = eligiblePair[0];
				final Map<Integer, SortedSet<Cell>> other = eligiblePair[1];
				moves.addAll(Elimination.Builder.collectNonEmpty(intersection(one.keySet(), other.keySet()).stream()
						.map(cand -> findBuilder(wingType, one, other, marks, cand))));
			});
		return moves;
	}

	private static Builder findBuilder(final WingType wingType, final Map<Integer, SortedSet<Cell>> one,
			final Map<Integer, SortedSet<Cell>> other, final PencilMarks marks, final Integer cand) {
		final SortedSet<Cell> oneCells = one.get(cand);
		final SortedSet<Cell> otherCells = other.get(cand);

		// should not all be in the same block
		if (union(Cell.toGroups(oneCells, Type.Block), Cell.toGroups(otherCells, Type.Block)).size() < 2)
			return null;

		final Set<CellGroup> crossingGroups = Cell.toGroups(oneCells, wingType.crossingType);
		if (!crossingGroups.equals(Cell.toGroups(otherCells, wingType.crossingType)))
			return null;

		final Builder builder = new Elimination.Builder(wingType.moveStrategy);
		crossingGroups
				.forEach(group -> builder.with(
						Cell.collect(group.streamEmptyCells().filter(cell -> !oneCells.contains(cell)
								&& !otherCells.contains(cell) && marks.candidates(cell).contains(cand))),
						Set.of(cand)));
		return builder;
	}

	/*
	 * Find candidates for which we have pairs of possible cells.
	 */
	private static List<Map<Integer, SortedSet<Cell>>> eligibleCellSets(final Sudoku sudoku, final PencilMarks marks,
			final Type pairsType) {
		return sudoku.getGroups(pairsType).stream().map(group -> marks.getCellsByCandidateFiltered(group, 2))
				.filter(m -> !m.isEmpty()).collect(Collectors.toList());
	}
}
