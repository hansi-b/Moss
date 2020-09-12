package org.hansi_b.moss.draw;

import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;

/**
 * Abstract base class for line-based ASCII-printing of Sudokus
 *
 * Implementing classes have to provide Strings for the lines of the Sudoku (no
 * newlines). Any null lines are allowed and omitted.
 */
abstract class AbstractLineBasedPrinter {

	public String draw(final Sudoku su) {
		final int size = su.size();
		final int blockSize = (int) Math.sqrt(size);

		final StringBuilder bld = new StringBuilder();
		appendLine(topBorder(blockSize), bld);

		int rowIndex = 1;
		for (final CellGroup row : su.iterateGroups(Type.Row)) {
			appendLine(valuesLine(row, blockSize, rowIndex == size), bld);
			if (rowIndex < size) {
				final String line;
				if (rowIndex % blockSize == 0) {
					line = borderBetweenBlocks(blockSize);
				} else {
					line = borderInBlock(blockSize);
				}
				appendLine(line, bld);
			}
			rowIndex += 1;
		}

		appendLine(bottomBorder(blockSize), bld);
		return bld.toString();
	}

	private static void appendLine(final String line, final StringBuilder bld) {
		if (line != null) {
			bld.append(line).append('\n');
		}
	}

	abstract protected String topBorder(final int blockSize);

	abstract protected String valuesLine(final CellGroup row, final int blockSize, boolean isLastRow);

	abstract protected String borderInBlock(final int blockSize);

	abstract protected String borderBetweenBlocks(final int blockSize);

	abstract protected String bottomBorder(final int blockSize);
}
