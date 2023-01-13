package org.hansi_b.moss.explain;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

import org.hansi_b.moss.Cell;

/**
 * Remove specific candidates from specific cells.
 */
public record Elimination(Strategy strategy, Marks candidatesByCell) implements Move {

	public static class Builder {
		private final Strategy strategy;
		private final Marks candsByCell;

		public Builder(final Strategy strategy) {
			this.strategy = strategy;
			this.candsByCell = new Marks();
		}

		public Builder with(final Cell cell, final int candidate) {
			candsByCell.add(cell, candidate);
			return this;
		}

		public Builder with(final Cell cell, final Collection<Integer> candidates) {
			candsByCell.addAll(cell, candidates);
			return this;
		}

		public Builder with(final Collection<Cell> cells, final Integer candidate) {
			cells.forEach(c -> with(c, candidate));
			return this;
		}

		public Builder with(final Collection<Cell> cells, final Collection<Integer> candidates) {
			if (!candidates.isEmpty())
				cells.forEach(c -> with(c, candidates));
			return this;
		}

		private boolean isEmpty() {
			return candsByCell.isEmpty();
		}

		public Elimination build() {
			return new Elimination(strategy, candsByCell);
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
		candidatesByCell.forEach((cell, cands) -> cands.forEach(cand -> marks.remove(cell, cand)));
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof Elimination m && //
				strategy == m.strategy && //
				candidatesByCell.equals(m.candidatesByCell);
	}

	@Override
	public int hashCode() {
		return Objects.hash(strategy, candidatesByCell);
	}

	@Override
	public String toString() {
		return String.format("Eliminate: %s (%s)", candidatesByCell, strategy);
	}
}