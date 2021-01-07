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
 * 
 * Does not use the pencil marks - that is done by the generalization
 * {@link NakedSinglePencilMark}
 */
public class NakedSingle implements Technique {

	@Override
	public List<Move> findMoves(final Sudoku sudoku, final PencilMarks marks) {

		return sudoku.streamEmptyCells().//
				filter(c -> c.getCandidates().size() == 1)
				.map(c -> new Insertion(Move.Strategy.NakedSingle, c, c.getCandidates().first()))//
				.collect(Collectors.toList());
	}
}
