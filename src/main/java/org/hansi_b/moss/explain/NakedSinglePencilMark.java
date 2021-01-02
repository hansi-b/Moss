package org.hansi_b.moss.explain;

import java.util.List;
import java.util.stream.Collectors;

import org.hansi_b.moss.Sudoku;

/**
 *
 * Also known as: Singleton, Sole Candidate, Lone Single
 * (https://www.learn-sudoku.com/lone-singles.html)
 * 
 * A generalization of the {@link NakedSingle}: Looks at the pencil marks
 * instead of filled cells.
 */
public class NakedSinglePencilMark implements Technique {

	@Override
	public List<Move> findMoves(final Sudoku sudoku, final PencilMarks marks) {

		// TODO: use the marks instead of looking at the cells directly?
		// then again, that's not really the spirit here
		return sudoku.streamEmptyCells().//
				filter(c -> marks.candidates(c).size() == 1)
				.map(c -> new Insertion(Move.Strategy.NakedSinglePencilMark, c, marks.candidates(c).first()))//
				.collect(Collectors.toList());
	}
}
