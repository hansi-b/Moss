package org.hansi_b.moss.explain;

import java.util.Arrays;
import java.util.function.Function;

import org.hansi_b.moss.GroupType;

public sealed interface Move permits Insertion, Elimination {

	public enum Strategy {
		NakedSingleInRow, //
		NakedSingleInCol, //
		NakedSingleInBlock, //
		NakedSingle, //
		NakedSinglePencilMark, //
		HiddenSingleInRow, //
		HiddenSingleInCol, //
		HiddenSingleInBlock, //
		NakedPairInRow, NakedPairInCol, NakedPairInBlock, //
		NakedTripleInRow, NakedTripleInCol, NakedTripleInBlock, //
		HiddenPairInRow, //
		HiddenPairInCol, //
		HiddenPairInBlock, //
		LockedCandidateBlockRow, //
		LockedCandidateBlockCol, //
		LockedCandidateRowBlock, //
		LockedCandidateColBlock, //
		XWingFromRow, //
		XWingFromCol, //
		XyWing //
		;

		public static Function<GroupType, Strategy> groupTypeMapper(final Strategy... rowColBlockReturns) {
			if (rowColBlockReturns.length != 3)
				throw new IllegalArgumentException(String.format("Require three arguments to strategy mapping, got %s",
						Arrays.toString(rowColBlockReturns)));
			return (final GroupType type) -> {
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
