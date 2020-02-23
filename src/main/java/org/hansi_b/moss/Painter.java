package org.hansi_b.moss;

import java.util.Random;
import org.hansi_b.moss.CellGroup.Type;

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
public class Painter {

	public String draw(final Sudoku su) {
		final int size = su.size();
		final int blockSize = (int) Math.sqrt(size);

		final StringBuilder bld = new StringBuilder();
		appendTopLine(blockSize, bld);

		int rowIndex = 1;
		for (final CellGroup row : su.iterateGroups(Type.Row)) {
			appendValuesLine(row, blockSize, bld);
			if (rowIndex < size) {
				if (rowIndex % blockSize == 0)
					appendLineBetweenBlocks(blockSize, bld);
				else
					appendLineInBlock(blockSize, bld);
			}
			rowIndex += 1;
		}

		appendBottomLine(blockSize, bld);
		return bld.toString();
	}

	private static void appendValuesLine(final CellGroup row, final int blockSize, final StringBuilder bld) {

		bld.append('║');
		int cellIndex = 1;
		for (final Integer val : row.values()) {
			bld.append(String.format(" %s ", val != null ? val : " "));
			bld.append(cellIndex % blockSize == 0 ? '║' : '│');
			cellIndex += 1;
		}
		bld.append('\n');
	}

	private static void appendTopLine(final int blockSize, final StringBuilder builder) {
		appendBorder(blockSize, '╔', '╤', '═', '╦', '╗', builder);
	}

	private static void appendBottomLine(final int blockSize, final StringBuilder builder) {
		appendBorder(blockSize, '╚', '╧', '═', '╩', '╝', builder);
	}

	private static void appendLineInBlock(final int blockSize, final StringBuilder builder) {
		// ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
		appendBorder(blockSize, '╟', '┼', '─', '╫', '╢', builder);
	}

	private static void appendLineBetweenBlocks(final int blockSize, final StringBuilder builder) {
		// ╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
		appendBorder(blockSize, '╠', '╪', '═', '╬', '╣', builder);
	}

	private static void appendBorder(final int blockSize, final char leftCorner, final char inBlock, final char ruler,
			final char betweenBlocks, final char rightCorner, final StringBuilder bld) {
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
		bld.append(rightCorner).append('\n');
	}

	public static void main(final String[] args) {
		final Integer[] vals = new Random().ints(9 * 9, -9, 9).map(i -> i < 0 ? 0 : i).boxed().toArray(Integer[]::new);
		System.err.println(new Painter().draw(Sudoku.filled(vals)));
	}
}
