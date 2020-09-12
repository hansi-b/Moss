package org.hansi_b.moss.explain;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import org.hansi_b.moss.Cell;

/**
 * A minimal cache of candidate values for cells.
 */
class CachedCandidates {
	private final Map<Cell, SortedSet<Integer>> candidatesByCell = new HashMap<>();

	public SortedSet<Integer> candidates(final Cell cell) {
		return candidatesByCell.computeIfAbsent(cell, Cell::getCandidates);
	}
}
