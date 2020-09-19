package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.List;

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
	public List<Move> findMoves(final Sudoku sudoku) {

		final List<Move> moves = new ArrayList<>();

		final XyWingFinder xyWingFinder = new XyWingFinder(sudoku);

		final List<WingTriple> wings = xyWingFinder.findAllWings();
		wings.forEach(x -> System.out.println(x));

		return moves;
	}
}
