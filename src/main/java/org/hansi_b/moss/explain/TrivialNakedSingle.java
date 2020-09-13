package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Function;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * The trivial variant of NakedSingle:
 *
 * Finds rows/cols/blocks which are correctly filled except for one element.
 *
 * Called open single here: https://www.learn-sudoku.com/open-singles.html
 */
public class TrivialNakedSingle implements Technique {

	private static final Function<Type, Strategy> strategyByGroup = Strategy.typeMapper(//
			Strategy.NakedSingleInRow, //
			Strategy.NakedSingleInCol, //
			Strategy.NakedSingleInBlock);

	private static Strategy strategyByGroup(final Type type) {
		return strategyByGroup.apply(type);
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku) {

		final List<Move> moves = new ArrayList<>();
		sudoku.streamGroups().forEach(g -> findMove(g, moves));
		return moves;
	}

	private static void findMove(final CellGroup group, final List<Move> moves) {
		final BitSet values = new BitSet(group.size());

		Cell emptyCell = null;
		for (final Cell c : group) {
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
		final boolean canSolve = emptyCell != null && values.cardinality() == group.size() - 1;

		if (canSolve)
			moves.add(new Move(strategyByGroup(group.type()), emptyCell, 1 + values.nextClearBit(0)));
	}
}
