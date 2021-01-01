package org.hansi_b.moss.explain;

import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hansi_b.moss.Cell;

/**
 * Remove specific candidates from specific cells.
 */
public class Elimination implements Move {

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

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Elimination))
			return false;
		final Elimination m = (Elimination) obj;
		return strategy == m.strategy && //
				cells.equals(m.cells) && //
				candidates.equals(m.candidates);
	}

	@Override
	public int hashCode() {
		return Objects.hash(strategy, cells, candidates);
	}

	@Override
	public String toString() {
		final SortedSet<Cell> sortedCells = Cell.newPosSortedSet();
		sortedCells.addAll(cells);
		return String.format("%s X %s (%s)", sortedCells, new TreeSet<>(candidates), strategy);
	}
}