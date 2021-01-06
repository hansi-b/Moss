package org.hansi_b.moss.explain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.hansi_b.moss.Cell;

/**
 * Remove specific candidates from specific cells.
 */
public class Elimination implements Move {

	final Strategy strategy;
	private final Map<Cell, SortedSet<Integer>> candidatesByCells;

	public Elimination(final Move.Strategy strategy) {
		this.strategy = strategy;
		this.candidatesByCells = new HashMap<>();
	}

	/**
	 * Marks each of the argument candidates to be removed from each of the argument
	 * cells.
	 */
	public Elimination with(final Set<Integer> candidates, final Set<Cell> cells) {
		cells.forEach(c -> candidatesByCells.computeIfAbsent(c, k -> new TreeSet<>()).addAll(candidates));
		return this;
	}

	boolean isEmpty() {
		return candidatesByCells.isEmpty();
	}

	@Override
	public void apply(final PencilMarks marks) {
		candidatesByCells.forEach((cell, cands) -> cands.forEach(cand -> marks.remove(cell, cand)));
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Elimination))
			return false;
		final Elimination m = (Elimination) obj;
		return strategy == m.strategy && //
				candidatesByCells.equals(m.candidatesByCells);
	}

	@Override
	public int hashCode() {
		return Objects.hash(strategy, candidatesByCells);
	}

	@Override
	public String toString() {
		final SortedSet<Cell> sortedCells = Cell.newPosSortedSet();
		sortedCells.addAll(candidatesByCells.keySet());
		final String joined = String.join(", ", sortedCells.stream()
				.map(c -> String.format("%s - %s", c, candidatesByCells.get(c))).collect(Collectors.toList()));
		return String.format("Eliminate: %s (%s)", joined, strategy);
	}
}