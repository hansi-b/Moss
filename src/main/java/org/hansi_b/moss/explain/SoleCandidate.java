package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;

/**
 * https://www.kristanix.com/sudokuepic/sudoku-solving-techniques.php Finds
 * cells where the row+block+column contain all numbers but one.
 */
public class SoleCandidate implements SolvingTechnique {

	@Override
	public List<Move> findPossibleMoves(final Sudoku sudoku) {

		final List<Move> moves = new ArrayList<Move>();

		// let's arbitrarly iterate over one type first:
		sudoku.iterateGroups(Type.Block).forEach(block -> {

			final BitSet blockBits = block.valuesAsBits();

			block.forEach(cell -> {
				if (cell.getValue() != null)
					return;

				final BitSet cellBits = new BitSet(sudoku.size());
				cellBits.or(blockBits);
				cellBits.or(cell.getGroup(Type.Row).valuesAsBits());
				cellBits.or(cell.getGroup(Type.Col).valuesAsBits());

				/*
				 * if the union of the three groups has left exactly one slot open, this must be
				 * the valid candidate
				 */
				if (cellBits.cardinality() == sudoku.size() - 1) {
					final int missingNumber = cellBits.nextClearBit(0) + 1;
					moves.add(new Move(Move.Strategy.SoleCandidate, cell, missingNumber));
				}
			});
		});

		return moves;
	}
}
