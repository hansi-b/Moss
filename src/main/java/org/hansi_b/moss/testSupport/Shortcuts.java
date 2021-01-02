package org.hansi_b.moss.testSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Pos;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Elimination;
import org.hansi_b.moss.explain.Insertion;
import org.hansi_b.moss.explain.Move;

public class Shortcuts {

	public static Cell cellAt(final Sudoku su, final int x, final int y) {
		return su.getCell(Pos.at(x, y));
	}

	@SafeVarargs
	public static Cell[] cellsAt(final Sudoku su, final ArrayList<Integer>... cellPosses) {
		return Arrays.stream(cellPosses).map(l -> su.getCell(Pos.at(l.get(0), l.get(1)))).collect(Collectors.toSet())
				.toArray(new Cell[] {});
	}

	public static Insertion insert(final Sudoku sudoku, final Move.Strategy strategy, final int row, final int col,
			final int newValue) {
		return new Insertion(strategy, sudoku.getCell(Pos.at(row, col)), newValue);
	}

	public static Elimination eliminate(final Sudoku sudoku, final Move.Strategy strategy, final int row, final int col,
			final int candidate) {
		return new Elimination(strategy, Collections.singleton(sudoku.getCell(Pos.at(row, col))),
				Collections.singleton(candidate));
	}

	public static Elimination eliminate(final Move.Strategy strategy, final int candidate, final Cell... cells) {
		return new Elimination(strategy, Set.of(cells), Collections.singleton(candidate));
	}
}
