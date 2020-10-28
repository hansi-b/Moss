package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;

/**
 * Aka "Locked Candidates Type 2" (e.g., http://www.angusj.com/sudoku/hints.php)
 *
 * As explained, e.g., on https://sudoku9x9.com/locked_candidates.html
 *
 * Find candidates in a row or column which are restricted to a single block;
 * eliminate those candidates from all other cells in the block.
 */
public class LockedCandidateLineBlock implements Technique {

	@Override
	public List<Move> findMoves(final Sudoku sudoku) {

		final var cached = new CachedCandidates();

		final List<Move> moves = new ArrayList<>();

		for (final Type lockingGroupType : Arrays.asList(Type.Row, Type.Col)) {
			sudoku.getGroups(lockingGroupType).forEach(lockingGroup -> {
				LockedCandidateBlockLine.findLocks(cached, lockingGroup, Type.Block, moves);
			});
		}
		return moves;
	}
}
