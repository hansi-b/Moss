package org.hansi_b.moss.explain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CollectUtils;

class Pair {

	final Cell a;
	final Cell b;

	private Pair(final Cell a, final Cell b) {
		this.a = a;
		this.b = b;
	}

	static Pair from(final List<Cell> cells, final int first, final int second) {
		return new Pair(cells.get(first), cells.get(second));
	}

	boolean isDisjointGroups() {
		return Arrays.stream(CellGroup.Type.values()).allMatch(t -> a.getGroup(t) != b.getGroup(t));
	}

	/**
	 * Get all cells in intersections of the groups of this wing's cells. (By
	 * consequence of the definition, the result cannot contain either of the wing
	 * cells.)
	 *
	 * @param candidates the cells in the intersection of both wing cells and the
	 *                   candidate cells
	 */
	Set<Cell> getIntersectingEmptyCells(final Collection<Cell> candidates) {
		return CollectUtils.intersection(emptyCellsFromGroups(a), emptyCellsFromGroups(b), candidates);
	}

	private static Set<Cell> emptyCellsFromGroups(final Cell x) {
		return x.streamGroups().flatMap(CellGroup::streamEmptyCells).collect(Collectors.toSet());
	}
}