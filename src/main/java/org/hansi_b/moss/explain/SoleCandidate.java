package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

import org.hansi_b.moss.CellGroup;
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

			final BitSet blockBits = valuesAsBits(block);

			block.forEach(cell -> {
				if (cell.getValue() != null)
					return;

				final BitSet cellBits = new BitSet(sudoku.size());
				cellBits.or(blockBits);
				cellBits.or(valuesAsBits(cell.getGroup(Type.Row)));
				cellBits.or(valuesAsBits(cell.getGroup(Type.Col)));

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

	private static BitSet valuesAsBits(final CellGroup group) {
		final BitSet values = new BitSet(group.size());
		group.getValues().stream().filter(Objects::nonNull).forEach(v -> values.set(v - 1));
		return values;
	}
}
