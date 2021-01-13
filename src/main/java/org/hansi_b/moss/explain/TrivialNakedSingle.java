package org.hansi_b.moss.explain;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * The trivial variant of NakedSingle:
 *
 * Finds rows/cols/blocks which are correctly filled except for one element.
 *
 * Called open single here: https://www.learn-sudoku.com/open-singles.html
 */
public class TrivialNakedSingle extends GroupBasedTechnique {

	public TrivialNakedSingle() {
		super(Strategy.NakedSingleInRow, //
				Strategy.NakedSingleInCol, //
				Strategy.NakedSingleInBlock);
	}

	@Override
	public List<Move> findMoves(final CellGroup group, final Strategy strategy, final PencilMarks marks) {

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
		return Collections.singletonList(new Insertion(strategy, emptyCells.first(), missing.first()));
	}
}
