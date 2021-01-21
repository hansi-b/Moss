package org.hansi_b.moss.explain;

import java.util.stream.Stream;

import org.hansi_b.moss.Sudoku;

interface Technique {

	static Technique[] allTechniques() {
		return new Technique[] { //
				new TrivialNakedSingle(), //
				new NakedSingle(), //
				new NakedSinglePencilMark(), //
				new HiddenSingle(), //
				new NakedPair(), //
				new NakedTriple(), //
				new HiddenPair(), //
				new LockedCandidates(), //
				new XWing(), //
				new XyWing(), //
		};
	}

	Stream<Move> findMoves(Sudoku sudoku, PencilMarks marks);
}