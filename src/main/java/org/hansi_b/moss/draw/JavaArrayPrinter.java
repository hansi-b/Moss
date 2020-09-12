package org.hansi_b.moss.draw;

import java.util.stream.Collectors;

import org.hansi_b.moss.CellGroup;

/**
 * Draws an array representation of a Sudoku for use in program code
 */
/*-
 *	final Integer[] values = { //
				5, 0, 0, 0, 0, 0, 0, 4, 0, //
				0, 3, 0, 4, 0, 0, 0, 0, 0, //
				0, 1, 0, 6, 0, 5, 0, 0, 0, //
				//
				0, 5, 1, 0, 6, 0, 0, 0, 2, //
				0, 0, 7, 1, 0, 0, 0, 5, 0, //
				2, 0, 0, 0, 7, 9, 0, 8, 3, //
				//
				0, 0, 2, 7, 0, 3, 9, 0, 0, //
				4, 0, 3, 2, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 8, 0, 0, 2, 0 //
		};
 */
public class JavaArrayPrinter extends AbstractLineBasedPrinter {

	@Override
	protected String topBorder(final int blockSize) {
		return "final Integer[] values = { //";
	}

	@Override
	protected String valuesLine(final CellGroup row, final int blockSize, final boolean isLastRow) {
		return String.format("\t%s%s //",
				row.values().stream().map(i -> i == null ? "0" : String.valueOf(i)).collect(Collectors.joining(", ")),
				isLastRow ? "" : ",");
	}

	@Override
	protected String borderInBlock(final int blockSize) {
		return null;
	}

	@Override
	protected String borderBetweenBlocks(final int blockSize) {
		return "\t//";
	}

	@Override
	protected String bottomBorder(final int blockSize) {
		return "};";
	}
}
