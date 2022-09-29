package org.hansi_b.moss.explain;

import static org.hansib.sundries.CollectUtils.flatten;
import static org.hansib.sundries.CollectUtils.mapMap;

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.GroupType;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * Aka "Locked Candidates Type 1/2" (e.g.,
 * http://www.angusj.com/sudoku/hints.php)
 *
 * As explained, e.g., on https://sudoku9x9.com/locked_candidates.html
 *
 * Find candidates in a Block which are restricted to either a row or column;
 * eliminate those candidates from all other cells in the row/column, and check
 * the intersecting blocks for moves.
 *
 * The inverse: Find candidates in a row or column which are restricted to a
 * single block; eliminate those candidates from all other cells in the block.
 */
public class LockedCandidates implements Technique {

	enum LockType {

		BlockCol(GroupType.Block, GroupType.Col, Strategy.LockedCandidateBlockCol), //
		BlockRow(GroupType.Block, GroupType.Row, Strategy.LockedCandidateBlockRow), //
		ColBlock(GroupType.Col, GroupType.Block, Strategy.LockedCandidateColBlock), //
		RowBlock(GroupType.Row, GroupType.Block, Strategy.LockedCandidateRowBlock);

		private final GroupType lockingType;
		private final GroupType targetType;
		private final Strategy moveStrategy;

		LockType(final GroupType lockingType, final GroupType targetType, final Strategy moveStrategy) {
			this.lockingType = lockingType;
			this.targetType = targetType;
			this.moveStrategy = moveStrategy;
		}
	}

	@Override
	public Stream<Move> findMoves(final Sudoku sudoku, final PencilMarks marks) {
		return Elimination.Builder.collectNonEmpty(flatten(Arrays.stream(LockType.values()),
				lockType -> flatten(sudoku.streamGroups(lockType.lockingType),
						lockingGroup -> mapMap(marks.getCellsByCandidate(lockingGroup),
								(cand, cells) -> buildForGroup(lockingGroup, lockType, marks, cand, cells)))));
	}

	private static Elimination.Builder buildForGroup(final CellGroup lockingGroup, final LockType lockType,
			final PencilMarks marks, final Integer cand, final SortedSet<Cell> cells) {
		final Set<CellGroup> groups = cells.stream().map(c -> c.getGroup(lockType.targetType))
				.collect(Collectors.toSet());
		if (groups.size() != 1)
			return null;
		/*
		 * All cells are in the same target group: check all cells in that group outside
		 * the locking group.
		 */
		final CellGroup target = groups.iterator().next();
		final Set<Cell> targetCells = Cell.collect(target.streamEmptyCells()
				.filter(c -> c.getGroup(lockingGroup.type()) != lockingGroup && marks.candidates(c).contains(cand)));
		return targetCells.isEmpty() ? null
				: new Elimination.Builder(lockType.moveStrategy).with(targetCells, Set.of(cand));
	}
}
