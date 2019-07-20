package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hansi_b.moss.explain.MoveAsserts.*;
import static org.junit.Assert.assertTrue;

import org.hansi_b.moss.Sudoku;
import static org.hansi_b.moss.SudokuTest.filledSudoku;
import org.hansi_b.moss.explain.TrivialSoleCandidate;
import org.hansi_b.moss.explain.Move.Strategy;
import org.junit.Test;

public class TrivialSoleCandidateTest {

	final TrivialSoleCandidate technique = new TrivialSoleCandidate();

	@Test
	public void testFindsSingleMissingInRow() {

		final Integer[] values = { //
				1, null, 2, 4, //
				null, null, null, null, //
				null, null, null, null, //
				null, null, null, null };
		final Sudoku su = filledSudoku(values);

		assertThat(technique.findPossibleMoves(su)).containsExactly(//
				move(su, Strategy.SoleCandidateInRow, 0, 1, 3));
	}

	@Test
	public void testFindsNoSingleOnDuplicates() {

		final Integer[] values = { //
				1, null, 2, 2, //
				null, null, null, null, //
				null, null, null, null, //
				null, null, null, null };
		final Sudoku su = filledSudoku(values);

		assertTrue(technique.findPossibleMoves(su).isEmpty());
	}

	@Test
	public void testFindsSingleMissingInCol() {

		final Integer[] values = { //
				1, null, null, null, //
				null, null, null, null, //
				2, null, null, null, //
				3, null, null, null };
		final Sudoku su = filledSudoku(values);

		assertThat(technique.findPossibleMoves(su)).containsExactly(//
				move(su, Strategy.SoleCandidateInCol, 1, 0, 4));
	}

	@Test
	public void testFindsSingleMissingInBlock() {

		final Integer[] values = { //
				null, null, 2, 3, //
				null, null, 4, null, //
				null, null, 1, null, //
				null, null, 3, null };

		final Sudoku su = filledSudoku(values);

		assertThat(technique.findPossibleMoves(su)).containsExactly(//
				move(su, Strategy.SoleCandidateInBlock, 1, 3, 1));
	}

	@Test
	public void testFindsAllThreeOnSameCell() {

		final Integer[] values = { //
				1, 2, null, 3, //
				null, null, 4, 1, //
				null, null, 2, null, //
				null, null, 3, null };

		final Sudoku su = filledSudoku(values);

		assertThat(technique.findPossibleMoves(su)).containsExactlyInAnyOrder(//
				move(su, Strategy.SoleCandidateInCol, 0, 2, 1), //
				move(su, Strategy.SoleCandidateInBlock, 0, 2, 2), //
				move(su, Strategy.SoleCandidateInRow, 0, 2, 4));
	}
}
