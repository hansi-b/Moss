package org.hansi_b.moss.explain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import org.hansi_b.moss.Cell;

/**
 * A minimal cache of candidate values for cells.
 */
class CachedCandidates {
	private final Map<Cell, SortedSet<Integer>> candidatesByCell = new HashMap<>();

	/**
	 * @return an unmodifiable view of the cell's candidate values
	 */
	public SortedSet<Integer> candidates(final Cell cell) {
		return Collections.unmodifiableSortedSet(candidatesByCell.computeIfAbsent(cell, Cell::getCandidates));
	}
}
