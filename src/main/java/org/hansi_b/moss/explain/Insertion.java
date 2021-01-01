package org.hansi_b.moss.explain;

import java.util.Objects;

import org.hansi_b.moss.Cell;

/**
 * A move that sets a value in a cell.
 */
public class Insertion implements Move {

	final Move.Strategy strategy;
	final Cell cell;
	final Integer newValue;

	public Insertion(final Move.Strategy strategy, final Cell cell, final Integer newValue) {
		this.strategy = strategy;
		this.cell = cell;
		this.newValue = newValue;
	}

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