package org.hansi_b.moss.explain;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Function;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;

/**
 * The trivial variant of NakedSingle:
 *
 * Finds rows/cols/blocks which are correctly filled except for one element.
 *
 * Called open single here: https://www.learn-sudoku.com/open-singles.html
 */
public class TrivialNakedSingle extends GroupBasedTechnique {

	private static final Function<Type, Move.Strategy> strategyByGroup = Move.Strategy.groupTypeMapper(//
			Move.Strategy.NakedSingleInRow, //
			Move.Strategy.NakedSingleInCol, //
			Move.Strategy.NakedSingleInBlock);

	private static Move.Strategy strategyByGroup(final Type type) {
		return strategyByGroup.apply(type);
	}

	@Override
	public List<Move> findMoves(final CellGroup group, final PencilMarks marks) {

		final SortedSet<Integer> missing = group.missing();
		if (missing.size() != 1)
			return Collections.emptyList();

		// double-check that there is only one empty cell
		final SortedSet<Cell> emptyCells = Cell.collect(group.streamEmptyCells());
		if (emptyCells.size() != 1)
			return Collections.emptyList();

		/*
		 * we want to have found exactly one empty cell, and only one value is missing
		 */
		return Collections
				.singletonList(new Insertion(strategyByGroup(group.type()), emptyCells.first(), missing.first()));
	}
}
