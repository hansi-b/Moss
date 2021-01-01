package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
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

	private static final Function<Type, Strategy> strategyByGroup = Strategy.groupTypeMapper(//
			Strategy.NakedSingleInRow, //
			Strategy.NakedSingleInCol, //
			Strategy.NakedSingleInBlock);

	private static Strategy strategyByGroup(final Type type) {
		return strategyByGroup.apply(type);
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku, PencilMarks cached) {

		final List<Move> moves = new ArrayList<>();
		sudoku.streamGroups().forEach(g -> findMove(g, moves));
		return moves;
	}

	private static void findMove(final CellGroup group, final List<Move> moves) {

		final SortedSet<Integer> missing = group.missing();
		if (missing.size() != 1)
			return;

		// double-check that there is only one empty cell
		final SortedSet<Cell> emptyCells = Cell.collect(group.streamEmptyCells());
		if (emptyCells.size() != 1)
			return;

		/*
		 * we want to have found exactly one empty cell, and only one value is missing
		 */
		moves.add(new Move(strategyByGroup(group.type()), emptyCells.first(), missing.first()));
	}
}
