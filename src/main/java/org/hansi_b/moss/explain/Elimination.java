package org.hansi_b.moss.explain;

import java.util.Set;

import org.hansi_b.moss.Cell;

/**
 * Remove candidates from cells.
 */
class Elimination implements Move {

	final Strategy strategy;
	private final Set<Cell> cells;
	private final Set<Integer> candidates;

	public Elimination(final Move.Strategy strategy, final Set<Cell> cells, final Set<Integer> candidates) {
		this.strategy = strategy;
		this.cells = cells;
		this.candidates = candidates;
	}

	@Override
	public void apply(final PencilMarks marks) {
		cells.forEach(cell -> candidates.forEach(cand -> marks.remove(cell, cand)));
	}
}