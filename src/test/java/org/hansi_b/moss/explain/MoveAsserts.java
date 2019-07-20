package org.hansi_b.moss.explain;

import org.hansi_b.moss.Pos;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;

class MoveAsserts {

	public static Move move(final Sudoku sudoku, final Strategy strategy, final int row, final int col,
			final int newValue) {
		return new Move(strategy, sudoku.getCell(Pos.at(row, col)), newValue);
	}
}