package org.hansi_b.moss.explain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;

/**
 * A minimal cache of candidate values for cells.
 */
class CachedCandidates {
	private final Map<Cell, SortedSet<Integer>> candidatesByCell = new HashMap<>();

	/**
	 * @return an unmodifiable view of the cell's candidate values
	 */
	SortedSet<Integer> candidates(final Cell cell) {
		return candidatesByCell.computeIfAbsent(cell, c -> Collections.unmodifiableSortedSet(c.getCandidates()));
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
		group.streamEmptyCells().forEach(c -> candidates(c)
				.forEach(i -> cellsByCandidate.computeIfAbsent(i, v -> Cell.newPosSortedSet()).add(c)));
		return cellsByCandidate;
	}
}
