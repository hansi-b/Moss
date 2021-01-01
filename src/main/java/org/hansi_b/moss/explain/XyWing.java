package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hansi_b.moss.Cell;
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
	public List<Insertion> findMoves(final Sudoku sudoku, final PencilMarks marks) {

		final List<Insertion> moves = new ArrayList<>();

		final XyWingFinder xyWingFinder = new XyWingFinder(sudoku, marks);

		final List<WingTriple> wings = xyWingFinder.findAllWings();

		/*
		 * now find all empty cells in the intersection of the two wing cells (other
		 * than the middle wing cell), and remove the wings's common candidate
		 */
		wings.forEach(wing -> {
			for (final Cell c : wing.targetCells()) {
				final Set<Integer> copiedCands = new HashSet<>(marks.candidates(c));
				if (copiedCands.remove(wing.commonCandidate) && copiedCands.size() == 1)
					moves.add(new Insertion(Move.Strategy.XyWing, c, copiedCands.iterator().next()));
			}
		});

		return moves;
	}
}
