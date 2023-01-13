package org.hansi_b.moss.explain.technique;

import java.util.stream.Stream;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move;
import org.hansi_b.moss.explain.PencilMarks;

public interface Technique {

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