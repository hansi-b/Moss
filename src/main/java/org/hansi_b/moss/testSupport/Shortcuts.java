package org.hansi_b.moss.testSupport;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Pos;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move;

public class Shortcuts {

	public static Cell cellAt(final Sudoku su, final int x, final int y) {
		return su.getCell(Pos.at(x, y));
	}

	public static Move move(final Sudoku sudoku, final Move.Strategy strategy, final int row, final int col,
			final int newValue) {
		return new Move(strategy, sudoku.getCell(Pos.at(row, col)), newValue);
	}
}
