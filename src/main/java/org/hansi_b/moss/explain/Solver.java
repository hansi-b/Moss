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
				new LockedCandidateBlockLine(), //
				new LockedCandidateLineBlock(), //
		};
	}

	/**
	 * Tries to solve the argument Sudoku
	 *
	 * @return a copy of the argument Sudoku, solved as far as possible
	 */
	public Sudoku solve(final Sudoku su) {
		final Sudoku suCopy = Sudoku.copyOf(su);
		for (List<Move> moves = findMoves(suCopy); //
				!moves.isEmpty(); //
				moves = findMoves(suCopy)) {
			final Move move = moves.get(0);
			move.getCell().setValue(move.getNewValue());
		}
		return suCopy;
	}

	private List<Move> findMoves(final Sudoku sudoku) {
		for (final Technique t : techniques) {
			final List<Move> moves = t.findMoves(sudoku);
			if (!moves.isEmpty())
				return moves;
		}

		return Collections.emptyList();
	}
}
