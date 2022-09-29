package org.hansi_b.moss.testsupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Pos;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Elimination;
import org.hansi_b.moss.explain.Insertion;
import org.hansi_b.moss.explain.Move.Strategy;
import org.hansi_b.moss.explain.PencilMarks;
import org.hansib.sundries.CollectUtils;

public class Shortcuts {

	private Shortcuts() {
		// instantiation prevention
	}

	public static Cell cellAt(final Sudoku su, final int x, final int y) {
		return su.getCell(Pos.at(x, y));
	}

	@SafeVarargs
	public static Cell[] cellsAt(final Sudoku su, final ArrayList<Integer>... cellPosses) {
		return Arrays.stream(cellPosses).map(l -> su.getCell(Pos.at(l.get(0), l.get(1)))).toList()
				.toArray(new Cell[] {});
	}

	public static Insertion insert(final Sudoku sudoku, final Strategy strategy, final int row, final int col,
			final int newValue) {
		return new Insertion(strategy, sudoku.getCell(Pos.at(row, col)), newValue);
	}

	public static Elimination eliminate(final Strategy strategy, final List<Integer> cands, final Cell... cells) {
		return new Elimination.Builder(strategy).with(Set.of(cells), cands).build();
	}

	public static void marks(final PencilMarks pm, final Cell cell, final Integer... cands) {
		CollectUtils.difference(pm.candidates(cell), Arrays.asList(cands)).forEach(v -> pm.remove(cell, v));
	}
}
