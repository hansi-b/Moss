package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Function;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;

/**
 * The trivial variant of NakedSingle:
 *
 * Finds rows/cols/blocks which are correctly filled except for one element.
 *
 * Called open single here: https://www.learn-sudoku.com/open-singles.html
 */
public class TrivialNakedSingle implements Technique {

	private static final Function<Type, Move.Strategy> strategyByGroup = Move.Strategy.groupTypeMapper(//
			Move.Strategy.NakedSingleInRow, //
			Move.Strategy.NakedSingleInCol, //
			Move.Strategy.NakedSingleInBlock);

	private static Move.Strategy strategyByGroup(final Type type) {
		return strategyByGroup.apply(type);
	}

	@Override
	public List<Insertion> findMoves(final Sudoku sudoku, final PencilMarks cached) {

		final List<Insertion> moves = new ArrayList<>();
		sudoku.streamGroups().forEach(g -> findMove(g, moves));
		return moves;
	}

	private static void findMove(final CellGroup group, final List<Insertion> moves) {

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
		moves.add(new Insertion(strategyByGroup(group.type()), emptyCells.first(), missing.first()));
	}
}
