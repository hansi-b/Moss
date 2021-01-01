package org.hansi_b.moss.explain;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup.Type;

public class Move {

	public enum Strategy {
		NakedSingleInRow, //
		NakedSingleInCol, //
		NakedSingleInBlock, //
		NakedSingle, //
		HiddenSingleInRow, //
		HiddenSingleInCol, //
		HiddenSingleInBlock, //
		NakedPairInRow, NakedPairInCol, NakedPairInBlock, //
		LockedCandidateBlockRow, //
		LockedCandidateBlockCol, //
		LockedCandidateRowBlock, //
		LockedCandidateColBlock, //
		XyWing //
		;

		static Function<Type, Strategy> groupTypeMapper(final Strategy... rowColBlockReturns) {
			if (rowColBlockReturns.length != 3)
				throw new IllegalArgumentException(String.format("Require three arguments to strategy mapping, got %s",
						Arrays.toString(rowColBlockReturns)));
			return (final Type type) -> {
				switch (type) {
				case Row:
					return rowColBlockReturns[0];
				case Col:
					return rowColBlockReturns[1];
				case Block:
					return rowColBlockReturns[2];
				default:
					throw new IllegalStateException(String.format("Unknown cell group type %s", type));
				}
			};
		}
	}

	private final Strategy strategy;
	private final Cell cell;
	private final Integer newValue;

	public Move(final Strategy strategy, final Cell cell, final Integer newValue) {
		this.strategy = strategy;
		this.cell = cell;
		this.newValue = newValue;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	void apply() {
		cell.setValue(newValue);
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Move))
			return false;
		final Move m = (Move) obj;
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
