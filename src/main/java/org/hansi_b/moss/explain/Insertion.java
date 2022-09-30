package org.hansi_b.moss.explain;

import java.util.Objects;

import org.hansi_b.moss.Cell;

/**
 * A move that sets a value in a cell.
 */
public record Insertion(Move.Strategy strategy, Cell cell, Integer newValue) implements Move {

	@Override
	public void apply(final PencilMarks marks) {
		cell.setValue(newValue);
		marks.updateByInsertion(cell, newValue);
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Insertion))
			return false;
		final Insertion m = (Insertion) obj;
		return strategy == m.strategy && //
				cell == m.cell && //
				newValue.intValue() == m.newValue.intValue();
	}

	@Override
	public int hashCode() {
		return Objects.hash(strategy, cell, newValue);
	}

	@Override
	public String toString() {
		return String.format("%s <- %d (%s)", cell, newValue, strategy);
	}
}