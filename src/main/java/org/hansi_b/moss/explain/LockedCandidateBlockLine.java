package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
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
 * Aka "Locked Candidates Type 1" (e.g., http://www.angusj.com/sudoku/hints.php)
 *
 * As explained, e.g., on https://sudoku9x9.com/locked_candidates.html
 *
 * Find candidates in a Block which are restricted to either a row or column;
 * eliminate those candidates from all other cells in the row/column, and check
 * the intersecting blocks for moves.
 */
public class LockedCandidateBlockLine implements Technique {

	private static EnumMap<Type, EnumMap<Type, Strategy>> strategyByTypes = new EnumMap<>(Type.class);
	static {
		for (final Type t : Type.values())
			strategyByTypes.put(t, new EnumMap<>(Type.class));
		strategyByTypes.get(Type.Block).put(Type.Col, Strategy.LockedCandidateBlockCol);
		strategyByTypes.get(Type.Block).put(Type.Row, Strategy.LockedCandidateBlockRow);
		strategyByTypes.get(Type.Col).put(Type.Block, Strategy.LockedCandidateColBlock);
		strategyByTypes.get(Type.Row).put(Type.Block, Strategy.LockedCandidateRowBlock);
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku) {

		final var cached = new CachedCandidates();

		final List<Move> moves = new ArrayList<>();
		final Type lockingGroupType = Type.Block;

		sudoku.getGroups(lockingGroupType).forEach(lockingGroup -> {
			for (final Type targetGroupType : Arrays.asList(Type.Row, Type.Col)) {
				findLocks(cached, lockingGroup, targetGroupType, moves);
			}
		});
		return moves;
	}

	static void findLocks(final CachedCandidates cached, final CellGroup lockingGroup, final Type targetGroupType,
			final List<Move> moves) {
		final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate = cached.getCellsByCandidate(lockingGroup);
		cellsByCandidate.forEach((i, cells) -> {
			final Set<CellGroup> groups = cells.stream().map(c -> c.getGroup(targetGroupType))
					.collect(Collectors.toSet());
			if (groups.size() != 1)
				return;
			/*
			 * All cells are in the same target row/column/block: check all cells in the
			 * row/col outside the locking group.
			 */
			final CellGroup target = groups.iterator().next();
			target.streamEmptyCells().filter(c -> c.getGroup(lockingGroup.type()) != lockingGroup).forEach(c -> {
				final SortedSet<Integer> candidates = cached.candidates(c);
				if (candidates.size() == 2 && candidates.contains(i)) {
					final HashSet<Integer> cands = new HashSet<Integer>(candidates);
					cands.remove(i);
					moves.add(new Move(getLockStrategy(lockingGroup.type(), targetGroupType), c,
							cands.iterator().next()));
				}
			});
		});
	}

	static Strategy getLockStrategy(final Type lockingGroupType, final Type targetGroupType) {
		final EnumMap<Type, Strategy> lockMapping = strategyByTypes.get(lockingGroupType);
		final Strategy result = lockMapping.get(targetGroupType);
		if (result == null)
			throw new IllegalStateException(
					String.format("Cannot have lock from %s on %s", lockingGroupType, lockingGroupType));
		return result;
	}
}
