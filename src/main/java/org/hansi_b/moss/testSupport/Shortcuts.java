package org.hansi_b.moss.testSupport;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Pos;
import org.hansi_b.moss.Sudoku;

public class Shortcuts {

	public static Cell cellAt(Sudoku su, int x, int y) {
		return su.getCell(Pos.at(x, y));
	}
}
