package org.hansi_b.moss.explain;

import java.util.stream.Stream;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.technique.HiddenPair;
import org.hansi_b.moss.explain.technique.HiddenSingle;
import org.hansi_b.moss.explain.technique.LockedCandidates;
import org.hansi_b.moss.explain.technique.NakedPair;
import org.hansi_b.moss.explain.technique.NakedSingle;
import org.hansi_b.moss.explain.technique.NakedSinglePencilMark;
import org.hansi_b.moss.explain.technique.NakedTriple;
import org.hansi_b.moss.explain.technique.TrivialNakedSingle;
import org.hansi_b.moss.explain.technique.XWing;
import org.hansi_b.moss.explain.technique.XyWing;

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