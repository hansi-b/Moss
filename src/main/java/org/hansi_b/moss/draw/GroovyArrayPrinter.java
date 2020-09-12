package org.hansi_b.moss.draw;

import java.util.stream.Collectors;

import org.hansi_b.moss.CellGroup;

/*-
 * Draws an array representation of a Sudoku for use in Groovy
 *
 *	final Integer[] values =
 				[0, 2, 0, 0]+
				[0, 0, 0, 1]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]
 */
public class GroovyArrayPrinter extends AbstractLineBasedPrinter {

	@Override
	protected String topBorder(final int blockSize) {
		return "final Integer[] values =";
	}

	@Override
	protected String valuesLine(final CellGroup row, final int blockSize, final boolean isLastRow) {
		return String.format("\t[%s]%s",
				row.values().stream().map(i -> i == null ? "0" : String.valueOf(i)).collect(Collectors.joining(", ")),
				isLastRow ? "" : "+");
	}

	@Override
	protected String borderInBlock(final int blockSize) {
		return null;
	}

	@Override
	protected String borderBetweenBlocks(final int blockSize) {
		return null;
	}

	@Override
	protected String bottomBorder(final int blockSize) {
		return null;
	}
}
