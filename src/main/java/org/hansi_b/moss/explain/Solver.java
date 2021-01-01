package org.hansi_b.moss.explain;

import java.util.Collections;
import java.util.List;

import org.hansi_b.moss.Sudoku;

public class Solver {

	private final Technique[] techniques;

	public Solver() {
		this.techniques = new Technique[] { //
				new TrivialNakedSingle(), //
				new NakedSingle(), //
				new HiddenSingle(), //
				new NakedPair(), //
				new XyWing(), //
				new LockedCandidates(), //
		};
	}

	/**
	 * Tries to solve the argument Sudoku
	 *
	 * @return a copy of the argument Sudoku, solved as far as possible
	 */
	public Sudoku solve(final Sudoku su) {
		final Sudoku suCopy = Sudoku.copyOf(su);
		final PencilMarks pencilMarks = new PencilMarks();

		List<Insertion> moves = findMoves(suCopy, pencilMarks);
		while (!moves.isEmpty()) {
			final Insertion move = moves.get(0);
			move.apply(pencilMarks);
			moves = findMoves(suCopy, pencilMarks);
		}
		return suCopy;
	}

	private List<Insertion> findMoves(final Sudoku sudoku, final PencilMarks pencilMarks) {
		for (final Technique t : techniques) {
			final List<Insertion> moves = t.findMoves(sudoku, pencilMarks);
			if (!moves.isEmpty())
				return moves;
		}

		return Collections.emptyList();
	}
}
