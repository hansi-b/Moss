package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.Sudoku.CellGroup;
import org.hansi_b.moss.Sudoku.CellGroup.Type;
import org.hansi_b.moss.explain.Move.Strategy;

public class Explainer {

	private final Sudoku sudoku;

	public Explainer(final Sudoku sudoku) {
		this.sudoku = sudoku;

	}

	public List<Move> getPossibleMoves() {

		final List<Move> moves = new ArrayList<Move>();

		for (int rowIdx = 1; rowIdx <= sudoku.size(); rowIdx++) {
			findMove(sudoku.getRow(rowIdx), moves);
			findMove(sudoku.getCol(rowIdx), moves);
		}
		return moves;
	}

	private static void findMove(final CellGroup cells, final List<Move> moves) {
		final BitSet values = new BitSet(cells.size());

		Cell emptyCell = null;
		for (final Cell c : cells) {
			if (c.getValue() == null) {
				if (emptyCell != null)
					return;
				emptyCell = c;
			} else {
				values.set(c.getValue() - 1);
			}
		}

		/*
		 * we want to have found an empty cell, and all values but one must have
		 * occurred
		 */
		final boolean canSolve = emptyCell != null && values.cardinality() == cells.size() - 1;

		if (canSolve)
			moves.add(new Move(mapCellGroupToStrategy(cells.type()), emptyCell, 1 + values.nextClearBit(0)));
	}

	private static Strategy mapCellGroupToStrategy(final Type type) {
		switch (type) {
		case Row:
			return Strategy.SingleMissingNumberInRow;
		case Col:
			return Strategy.SingleMissingNumberInCol;
		case Block:
			return Strategy.SingleMissingNumberInBlock;
		default:
			throw new IllegalStateException(String.format("Unknown cell group type %s", type));
		}
	}
}
