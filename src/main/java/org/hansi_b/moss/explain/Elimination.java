package org.hansi_b.moss.explain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.hansi_b.moss.Cell;
import org.hansib.sundries.CollectUtils;

/**
 * Remove specific candidates from specific cells.
 */
public record Elimination(Move.Strategy strategy, SortedMap<SortedSet<Cell>, SortedSet<Integer>> cellsByCandidates)
		implements Move {

	public static class Builder {
		private final Strategy strategy;
		final Map<Cell, SortedSet<Integer>> candsBySingleCell;

		public Builder(final Move.Strategy strategy) {
			this.strategy = strategy;
			this.candsBySingleCell = Cell.newPosSortedMap();
		}

		public Builder with(final Cell cell, final int candidate) {
			targetSet(cell).add(candidate);
			return this;
		}

		public Builder with(final Collection<Cell> cells, final Collection<Integer> candidates) {
			for (final Cell cell : cells)
				targetSet(cell).addAll(candidates);
			return this;
		}

		private SortedSet<Integer> targetSet(final Cell cell) {
			return candsBySingleCell.computeIfAbsent(cell, k -> new TreeSet<>());
		}

		public boolean isEmpty() {
			return candsBySingleCell.isEmpty();
		}

		public Elimination build() {
			// aggregate cells by candidates
			final Map<SortedSet<Integer>, SortedSet<Cell>> cellsByCands = CollectUtils.invertMap(candsBySingleCell,
					v -> Cell.newPosSortedSet(), new HashMap<>());

			/*
			 * NB: by construction, there can be no duplicate values in cellsByCands, so we
			 * can just turn it around
			 */
			final SortedMap<SortedSet<Cell>, SortedSet<Integer>> candsByCells = new TreeMap<>(
					CollectUtils.sortedSetComparator(Cell.positionComparator));
			cellsByCands.forEach((k, v) -> candsByCells.put(v, k));

			return new Elimination(strategy, candsByCells);
		}

		/**
		 * A null-tolerant filter on non-empty builders.
		 *
		 * @param builders a stream of builders, possibly empty, possibly containing
		 *                 nulls
		 * @return a list of Moves from the non-null, non-empty builders
		 */
		public static Stream<Move> collectNonEmpty(final Stream<Builder> builders) {
			return builders.filter(b -> b != null && !b.isEmpty()).map(Elimination.Builder::build);
		}
	}

	@Override
	public void apply(final PencilMarks marks) {
		cellsByCandidates
				.forEach((cells, cands) -> cells.forEach(cell -> cands.forEach(cand -> marks.remove(cell, cand))));
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Elimination))
			return false;
		final Elimination m = (Elimination) obj;
		return strategy == m.strategy && //
				cellsByCandidates.equals(m.cellsByCandidates);
	}

	@Override
	public int hashCode() {
		return Objects.hash(strategy, cellsByCandidates);
	}

	@Override
	public String toString() {
		final String joined = String.join(", ",
				CollectUtils.mapMapToList(cellsByCandidates, (k, v) -> String.format("%s - %s", k, v)));
		return String.format("Eliminate: %s (%s)", joined, strategy);
	}
}