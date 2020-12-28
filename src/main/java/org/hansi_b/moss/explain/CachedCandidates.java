package org.hansi_b.moss.explain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.Errors;

/**
 * A minimal cache of candidate values for cells.
 */
class CachedCandidates {
	private final Map<Cell, SortedSet<Integer>> candidatesByCell = new HashMap<>();

	/**
	 * @return an unmodifiable view of the cell's candidate values
	 */
	SortedSet<Integer> candidates(final Cell cell) {
		return Collections.unmodifiableSortedSet(candidatesInternal(cell));
	}

	private SortedSet<Integer> candidatesInternal(final Cell cell) {
		return candidatesByCell.computeIfAbsent(cell, c -> c.getCandidates());
	}

	/**
	 * Removes the given candidate from the given cell. Is strict in that it will
	 * throw an exception if the argument candidate is not an option for the
	 * argument cell.
	 * 
	 * @throws IllegalArgumentException if the given candidate is not a candidate
	 *                                  for the given cell
	 */
	void remove(final Cell cell, final int candidate) {
		final SortedSet<Integer> cands = candidatesInternal(cell);
		if (!cands.contains(candidate))
			throw Errors.illegalArg("Argument '%d' is not in candidates %s of cell %s", candidate, cands, cell);

		cands.remove(candidate);
	}

	/**
	 * Aggregates a mapping from missing numbers to cells in a group.
	 *
	 * @param group the group for which to aggregate the result map
	 * @return a SortedMap mapping values missing in the argument group to cells in
	 *         that group where the value is a candidate
	 */
	SortedMap<Integer, SortedSet<Cell>> getCellsByCandidate(final CellGroup group) {
		final SortedMap<Integer, SortedSet<Cell>> cellsByCandidate = new TreeMap<>();
		group.streamEmptyCells().forEach(c -> candidatesInternal(c)
				.forEach(i -> cellsByCandidate.computeIfAbsent(i, v -> Cell.newPosSortedSet()).add(c)));
		return cellsByCandidate;
	}
}
