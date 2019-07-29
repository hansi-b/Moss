package org.hansi_b.moss;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight implementation of a position on a square Sudoku.
 *
 * The implementation guarantees object identity for position object(s) with the
 * same coordinates.
 */
public class Pos {

	public static final Comparator<Pos> positionComparator = (p1, p2) -> {
		final int rowDiff = p1.row - p2.row;
		return rowDiff != 0 ? rowDiff : p1.col - p2.col;
	};

	private static final Map<Integer, Map<Integer, Pos>> lookup = new HashMap<>();

	final int row;
	final int col;

	private Pos(final int row, final int col) {
		this.row = row;
		this.col = col;
	}

	public static Pos at(final int row, final int col) {
		final Map<Integer, Pos> rowMap = lookup.computeIfAbsent(row, r -> new HashMap<>());
		return rowMap.computeIfAbsent(col, c -> new Pos(row, c));
	}

	@Override
	public String toString() {
		return String.format("@(%d, %d)", row, col);
	}
}