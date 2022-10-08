package org.hansi_b.moss;

import java.util.stream.IntStream;
import java.util.stream.Stream;

interface GroupPosStreamer {
	Stream<Pos> streamPos(final int groupIdx, final int sudokuSize);
}

public enum GroupType {

	Row(GroupType::rowPos), //
	Col(GroupType::colPos), //
	Block(GroupType::blockPos);

	private final GroupPosStreamer posStreamer;

	GroupType(final GroupPosStreamer posStreamer) {
		this.posStreamer = posStreamer;
	}

	/**
	 * @param groupIdx   the index of this group in the Sudoku; by definition, there
	 *                   should be - per group type - one group for each index
	 *                   between zero and the Sudoku size minus one
	 * @param sudokuSize the size of the Sudoku
	 * @return a Stream of the positions of this group's cells
	 */
	public Stream<Pos> streamPos(final int groupIdx, final int sudokuSize) {
		return posStreamer.streamPos(groupIdx, sudokuSize);
	}

	private static Stream<Pos> rowPos(final int groupIdx, final int sudokuSize) {
		return IntStream.range(0, sudokuSize).mapToObj(colIdx -> Pos.at(groupIdx, colIdx));
	}

	private static Stream<Pos> colPos(final int groupIdx, final int sudokuSize) {
		return IntStream.range(0, sudokuSize).mapToObj(rowIdx -> Pos.at(rowIdx, groupIdx));
	}

	private static Stream<Pos> blockPos(final int groupIdx, final int sudokuSize) {
		final int sizeSqrt = (int) Math.sqrt(sudokuSize);
		// integer cutoff for the row offset:
		final int rowOffset = sizeSqrt * (groupIdx / sizeSqrt);
		final int colOffset = sizeSqrt * (groupIdx % sizeSqrt);

		return IntStream.range(0, sizeSqrt).boxed().flatMap( //
				rowIdx -> IntStream.range(0, sizeSqrt)
						.mapToObj(colIdx -> Pos.at(rowOffset + rowIdx, colOffset + colIdx)));
	}
}