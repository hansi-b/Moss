package org.hansi_b.moss.explain;

import java.util.Arrays;
import java.util.function.Function;

import org.hansi_b.moss.CellGroup.Type;

public interface Move {

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

	public void apply(final PencilMarks marks);
}
