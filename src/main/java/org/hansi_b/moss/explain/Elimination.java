package org.hansi_b.moss.explain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CollectUtils;

/**
 * Remove specific candidates from specific cells.
 */
public class Elimination implements Move {

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
			final Map<SortedSet<Integer>, SortedSet<Cell>> cellsByCands = new HashMap<>();
			candsBySingleCell.entrySet().forEach(
					e -> cellsByCands.computeIfAbsent(e.getValue(), k -> Cell.newPosSortedSet()).add(e.getKey()));
			/*
			 * NB: by construction, there can be no duplicate values in cellsByCands, so we
			 * can just turn it around
			 */
			final SortedMap<SortedSet<Cell>, SortedSet<Integer>> candsByCells = new TreeMap<>(
					CollectUtils.sortedSetComparator(Cell.positionComparator));
			cellsByCands.forEach((k, v) -> candsByCells.put(v, k));

			return new Elimination(strategy, candsByCells);
		}

		public static List<Move> collectNonEmpty(final Stream<Builder> builders) {
			return builders.filter(b -> !b.isEmpty()).map(Elimination.Builder::build).collect(Collectors.toList());
		}
	}

	final Strategy strategy;
	private final SortedMap<SortedSet<Cell>, SortedSet<Integer>> candidatesCellsBy;

	private Elimination(final Move.Strategy strategy,
			final SortedMap<SortedSet<Cell>, SortedSet<Integer>> cellsByCandidates) {
		this.strategy = strategy;
		this.candidatesCellsBy = cellsByCandidates;
	}

	@Override
	public void apply(final PencilMarks marks) {
		candidatesCellsBy
				.forEach((cells, cands) -> cells.forEach(cell -> cands.forEach(cand -> marks.remove(cell, cand))));
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Elimination))
			return false;
		final Elimination m = (Elimination) obj;
		return strategy == m.strategy && //
				candidatesCellsBy.equals(m.candidatesCellsBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(strategy, candidatesCellsBy);
	}

	@Override
	public String toString() {
		final String joined = String.join(", ",
				CollectUtils.mapSortedMapToList(candidatesCellsBy, (k, v) -> String.format("%s - %s", k, v)));
		return String.format("Eliminate: %s (%s)", joined, strategy);
	}
}