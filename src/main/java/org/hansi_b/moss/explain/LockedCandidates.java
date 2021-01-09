package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
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

		BlockCol(Type.Block, Type.Col, Strategy.LockedCandidateBlockCol), //
		BlockRow(Type.Block, Type.Row, Strategy.LockedCandidateBlockRow), //
		ColBlock(Type.Col, Type.Block, Strategy.LockedCandidateColBlock), //
		RowBlock(Type.Row, Type.Block, Strategy.LockedCandidateRowBlock);

		private final Type lockingType;
		private final Type targetType;
		private final Strategy moveStrategy;

		LockType(final Type lockingType, final Type targetType, final Strategy moveStrategy) {
			this.lockingType = lockingType;
			this.targetType = targetType;
			this.moveStrategy = moveStrategy;
		}
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku, final PencilMarks marks) {

		final List<Move> moves = new ArrayList<>();
		for (final LockType lockType : LockType.values()) {
			sudoku.getGroups(lockType.lockingType).forEach(lockingGroup -> {
				final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate = marks.getCellsByCandidate(lockingGroup);
				cellsByCandidate.forEach((cand, cells) -> {
					final Set<CellGroup> groups = cells.stream().map(c -> c.getGroup(lockType.targetType))
							.collect(Collectors.toSet());
					if (groups.size() != 1)
						return;
					/*
					 * All cells are in the same target group: check all cells in that group outside
					 * the locking group.
					 */
					final CellGroup target = groups.iterator().next();
					final Set<Cell> targetCells = Cell.collect(
							target.streamEmptyCells().filter(c -> c.getGroup(lockingGroup.type()) != lockingGroup
									&& marks.candidates(c).contains(cand)));
					if (!targetCells.isEmpty()) {
						moves.add(
								new Elimination.Builder(lockType.moveStrategy).with(targetCells, Set.of(cand)).build());
					}
				});
			});
		}
		return moves;
	}
}
