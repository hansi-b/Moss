package org.hansi_b.moss.explain;

import java.util.Arrays;

import org.hansi_b.moss.Sudoku;

public class Solver {

	private final Technique[] techniques;

	public Solver() {
		this(Technique.allTechniques());
	}

	public Solver(final Technique... techniques) {
		this.techniques = techniques;
	}

	/**
	 * Tries to solve the argument Sudoku
	 *
	 * @return a copy of the argument Sudoku, solved as far as possible
	 */
	public Sudoku solve(final Sudoku su) {
		final Sudoku suCopy = Sudoku.copyOf(su);
		final PencilMarks pencilMarks = new PencilMarks();

		Move move = findNextMove(suCopy, pencilMarks);
		while (move != null) {
			move.apply(pencilMarks);
			move = findNextMove(suCopy, pencilMarks);
		}
		return suCopy;
	}

	private Move findNextMove(final Sudoku sudoku, final PencilMarks pencilMarks) {
		return Arrays.stream(techniques).flatMap(t -> t.findMoves(sudoku, pencilMarks)).findFirst()
				.orElseGet(() -> null);
	}
}
