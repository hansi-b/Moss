package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.explain.Move.Strategy;
import org.hansi_b.moss.Sudoku;

/**
 * Aka "Locked Candidates Type 1" (e.g., http://www.angusj.com/sudoku/hints.php)
 *
 * As explained, e.g., on https://sudoku9x9.com/locked_candidates.html
 *
 * Find candidates in a Block which are restricted to either a row or column;
 * eliminate those candidates from all other cells in the row/column, and check
 * the intersecting blocks for moves.
 */
public class LockedCandidateBoxLine implements Technique {

	@Override
	public List<Move> findMoves(final Sudoku sudoku) {

		final var cached = new CachedCandidates();

		final List<Move> moves = new ArrayList<>();
		sudoku.getGroups(Type.Block).forEach(lockingGroup -> {
			final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate = cached.getCellsByCandidate(lockingGroup);

			cellsByCandidate.forEach((i, cells) -> {
				for (final Type targetType : Arrays.asList(Type.Row, Type.Col)) {
					final Set<CellGroup> groups = cells.stream().map(c -> c.getGroup(targetType))
							.collect(Collectors.toSet());
					if (groups.size() != 1)
						continue;
					/*
					 * All cells are in the same row/column: check all cells in the row/col outside
					 * this block.
					 */
					final CellGroup target = groups.iterator().next();
					target.streamEmptyCells().filter(c -> c.getGroup(Type.Block) != lockingGroup).forEach(c -> {
						final SortedSet<Integer> candidates = cached.candidates(c);
						if (candidates.size() == 2 && candidates.contains(i)) {
							final HashSet<Integer> cands = new HashSet<Integer>(candidates);
							cands.remove(i);
							moves.add(new Move(Strategy.LockedCandidateBoxLine, c, cands.iterator().next()));
						}
					});
				}
			});
		});
		return moves;
	}
}
