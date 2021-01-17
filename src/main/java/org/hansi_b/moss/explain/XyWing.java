package org.hansi_b.moss.explain;

import java.util.List;
import java.util.stream.Collectors;

import org.hansi_b.moss.Sudoku;

/**
 * As explained, e.g., on https://www.learn-sudoku.com/xy-wing.html
 *
 * Recipe:
 * <ol>
 * <li>Find all cells with two candidates.
 * <li>Identify three cells A,X,B where
 * <ol>
 * <li>X shares a - different - group with each A and B; and
 * <li>X,A,B have "chained" candidate pairs.
 * </ol>
 * <li>Identify all other empty cells that share groups with both A and B;
 * eliminate the candidate common to A and B from those empty cells.
 * </ol>
 *
 */
public class XyWing implements Technique {

	@Override
	public List<Move> findMoves(final Sudoku sudoku, final PencilMarks marks) {

		/*
		 * now find all empty cells in the intersection of the two wing cells (other
		 * than the middle wing cell), and remove the wings's common candidate
		 */
		return Elimination.Builder.collectNonEmpty(new XyWingFinder(sudoku, marks).streamWings().map(wing -> {
			final Elimination.Builder builder = new Elimination.Builder(Move.Strategy.XyWing);
			wing.targetCells().stream().filter(c -> marks.candidates(c).contains(wing.commonCandidate))
					.collect(Collectors.toSet()).forEach(cell -> builder.with(cell, wing.commonCandidate));
			return builder;
		}));
	}
}
