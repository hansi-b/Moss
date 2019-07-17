package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * Finds rows/cols/blocks which are correctly filled except for one element. The
 * trivial variant of
 * https://www.kristanix.com/sudokuepic/sudoku-solving-techniques.php
 */
public class TrivialSoleCandidate implements SolvingTechnique {

	@Override
	public List<Move> findPossibleMoves(final Sudoku sudoku) {

		final List<Move> moves = new ArrayList<Move>();

		for (int i = 0; i < sudoku.size(); i++) {
			for (final Type type : Type.values())
				findMove(sudoku.getGroup(type, i), moves);
		}
		return moves;
	}

	private static void findMove(final CellGroup cells, final List<Move> moves) {
		final BitSet values = new BitSet(cells.size());

		Cell emptyCell = null;
		for (final Cell c : cells) {
			final Integer value = c.getValue();
			if (value == null) {
				if (emptyCell != null)
					return; // second empty cell -> bail
				emptyCell = c;
			} else {
				values.set(value - 1);
			}
		}

		/*
		 * we want to have found exactly one empty cell, and all values but one must
		 * have occurred
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
