package org.hansi_b.moss.explain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.hansi_b.moss.Cell;

/**
 * A minimal cache of candidate values for cells.
 */
class CachedCandidates {
	private final Map<Cell, Set<Integer>> candidatesByCell = new HashMap<>();

	public Set<Integer> candidates(final Cell cell) {
		return candidatesByCell.computeIfAbsent(cell, Cell::getCandidates);
	}
}
