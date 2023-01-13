package org.hansi_b.moss.explain.technique;

import java.util.stream.Stream;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Insertion;
import org.hansi_b.moss.explain.Move;
import org.hansi_b.moss.explain.PencilMarks;
import org.hansi_b.moss.explain.Strategy;
import org.hansi_b.moss.explain.Technique;

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
	public Stream<Move> findMoves(final Sudoku sudoku, final PencilMarks marks) {

		return sudoku.streamEmptyCells().//
				filter(c -> c.getCandidates().size() == 1)
				.map(c -> new Insertion(Strategy.NakedSingle, c, c.getCandidates().first()));
	}
}
