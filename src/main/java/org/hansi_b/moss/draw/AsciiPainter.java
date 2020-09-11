package org.hansi_b.moss.draw;

import org.hansi_b.moss.CellGroup;

/**
 * Draws an ASCII representation of a Sudoku, e.g.:
 */
/*-
 *	╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗
 * 	║ 8 │ 5 │   ║   │   │ 2 ║ 4 │   │   ║
 * 	╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
 * 	║ 7 │ 2 │   ║   │   │   ║   │   │ 9 ║
 * 	╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
 * 	║   │   │ 4 ║   │   │   ║   │   │   ║
 * 	╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
 * 	║   │   │   ║ 1 │   │ 7 ║   │   │ 2 ║
 * 	╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
 * 	║ 3 │   │ 5 ║   │   │   ║ 9 │   │   ║
 * 	╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
 * 	║   │ 4 │   ║   │   │   ║   │   │   ║
 * 	╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
 * 	║   │   │   ║   │ 8 │   ║   │ 7 │   ║
 * 	╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
 * 	║   │ 1 │ 7 ║   │   │   ║   │   │   ║
 * 	╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
 * 	║   │   │   ║   │ 3 │ 6 ║   │ 4 │   ║
 *	╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝
 */
public class AsciiPainter extends AbstractLineBasedPrinter {

	@Override
	protected String topBorder(final int blockSize) {
		return appendBorder(blockSize, '╔', '╤', '═', '╦', '╗');
	}

	@Override
	protected String valuesLine(final CellGroup row, final int blockSize) {
		final StringBuilder bld = new StringBuilder();
		bld.append('║');
		int cellIndex = 1;
		for (final Integer val : row.values()) {
			bld.append(String.format(" %s ", val != null ? val : " "));
			bld.append(cellIndex % blockSize == 0 ? '║' : '│');
			cellIndex += 1;
		}
		return bld.toString();
	}

	@Override
	protected String borderInBlock(final int blockSize) {
		// ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
		return appendBorder(blockSize, '╟', '┼', '─', '╫', '╢');
	}

	@Override
	protected String borderBetweenBlocks(final int blockSize) {
		// ╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
		return appendBorder(blockSize, '╠', '╪', '═', '╬', '╣');
	}

	@Override
	protected String bottomBorder(final int blockSize) {
		return appendBorder(blockSize, '╚', '╧', '═', '╩', '╝');
	}

	private static String appendBorder(final int blockSize, final char leftCorner, final char inBlock, final char ruler,
			final char betweenBlocks, final char rightCorner) {
		final StringBuilder bld = new StringBuilder();
		bld.append(leftCorner);
		for (int b = 0; b < blockSize; b++) {
			for (int c = 0; c < blockSize; c++) {
				bld.append(ruler).append(ruler).append(ruler);
				if (c < blockSize - 1)
					bld.append(inBlock);
			}
			if (b < blockSize - 1)
				bld.append(betweenBlocks);
		}
		bld.append(rightCorner);
		return bld.toString();
	}
}
