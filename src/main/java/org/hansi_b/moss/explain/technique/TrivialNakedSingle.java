package org.hansi_b.moss.explain.technique;

import java.util.SortedSet;
import java.util.stream.Stream;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.explain.Insertion;
import org.hansi_b.moss.explain.Move;
import org.hansi_b.moss.explain.PencilMarks;
import org.hansi_b.moss.explain.Strategy;

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
	public Stream<Move> findMoves(final CellGroup group, final Strategy strategy, final PencilMarks marks) {

		final SortedSet<Integer> missing = group.missing();
		if (missing.size() != 1)
			return Stream.empty();

		// double-check that there is only one empty cell
		final SortedSet<Cell> emptyCells = Cell.collect(group.streamEmptyCells());
		if (emptyCells.size() != 1)
			return Stream.empty();

		/*
		 * we want to have found exactly one empty cell, and only one value is missing
		 */
		return Stream.of(new Insertion(strategy, emptyCells.first(), missing.first()));
	}
}
