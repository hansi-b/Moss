package org.hansi_b.moss.explain;

import java.util.Optional;
import java.util.stream.Stream;

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

		Move move = findMoves(suCopy, pencilMarks);
		while (move != null) {
			move.apply(pencilMarks);
			move = findMoves(suCopy, pencilMarks);
		}
		return suCopy;
	}

	private Move findMoves(final Sudoku sudoku, final PencilMarks pencilMarks) {
		for (final Technique t : techniques) {
			final Stream<Move> moves = t.findMoves(sudoku, pencilMarks);
			final Optional<Move> nextMove = moves.findFirst();
			if (nextMove.isPresent())
				return nextMove.get();
		}

		return null;
	}
}
