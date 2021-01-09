package org.hansi_b.moss.explain;

import java.util.List;

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
				new LockedCandidates(), //
				new XWing(), //
				new XyWing(), //
		};
	}

	List<Move> findMoves(Sudoku sudoku, PencilMarks marks);
}