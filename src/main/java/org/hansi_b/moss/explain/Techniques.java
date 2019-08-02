package org.hansi_b.moss.explain;

import java.util.Collections;
import java.util.List;

import org.hansi_b.moss.Sudoku;

class StandardTechniques implements Technique {

	private final Technique[] techniques;

	public StandardTechniques() {
		this.techniques = new Technique[] { //
				new TrivialNakedSingle(), //
				new NakedSingle(), //
				new HiddenSingle(), //
		};
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku) {
		for (final Technique t : techniques) {
			final List<Move> moves = t.findMoves(sudoku);
			if (!moves.isEmpty())
				return moves;
		}

		return Collections.emptyList();
	}
}
