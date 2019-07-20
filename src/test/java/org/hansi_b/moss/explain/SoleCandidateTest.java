package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hansi_b.moss.SudokuTest.givenSudoku;

import java.util.List;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;
import org.junit.Test;

import static org.hansi_b.moss.explain.MoveAsserts.*;

public class SoleCandidateTest {

	final SoleCandidate technique = new SoleCandidate();

	@Test
	public void testFindsTrivialSoleInRow() {

		final Integer[] values = { //
				1, null, 2, 4, //
				null, null, null, null, //
				null, null, null, null, //
				null, null, null, null };
		final Sudoku su = givenSudoku(values);

		final List<Move> candidates = technique.findPossibleMoves(su);

		assertThat(candidates.size()).isEqualTo(1);

		assertThatMoveIs(candidates.get(0), Strategy.SoleCandidate, 0, 1, 3);
	}

	@Test
	public void testFindsSingleSoleFromCombination() {

		final Integer[] values = { //
				1, null, null, null, //
				null, null, 3, null, //
				null, 2, null, null, //
				null, null, null, null };
		final Sudoku su = givenSudoku(values);

		final List<Move> candidates = technique.findPossibleMoves(su);

		assertThat(candidates.size()).isEqualTo(1);
		assertThatMoveIs(candidates.get(0), Strategy.SoleCandidate, 1, 1, 4);
	}

}
