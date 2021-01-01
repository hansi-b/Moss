package org.hansi_b.moss.explain;

import java.util.List;
import java.util.stream.Collectors;

import org.hansi_b.moss.Sudoku;

/**
 *
 * After https://www.sudoku-solutions.com/index.php?page=solvingnakedsubsets
 *
 * Also known as: Singleton, Sole Candidate, Lone Single
 * (https://www.learn-sudoku.com/lone-singles.html)
 *
 * Finds cells where the combinations of the cell's row+block+column contain all
 * numbers but one.
 */
public class NakedSingle implements Technique {

	@Override
	public List<Insertion> findMoves(final Sudoku sudoku, final PencilMarks cached) {

		// TODO: use the marks instead of looking at the cells directly?
		// then again, that's not really the spirit here
		return sudoku.streamEmptyCells().//
				filter(c -> c.getCandidates().size() == 1)
				.map(c -> new Insertion(Move.Strategy.NakedSingle, c, c.getCandidates().first()))//
				.collect(Collectors.toList());
	}
}
