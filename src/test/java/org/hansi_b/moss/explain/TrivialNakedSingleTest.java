package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hansi_b.moss.explain.MoveAsserts.*;
import static org.junit.Assert.assertTrue;

import org.hansi_b.moss.Sudoku;
import static org.hansi_b.moss.SudokuTest.filledSudoku;
import org.hansi_b.moss.explain.TrivialNakedSingle;
import org.hansi_b.moss.explain.Move.Strategy;
import org.junit.Test;

public class TrivialNakedSingleTest {

	final TrivialNakedSingle technique = new TrivialNakedSingle();

	@Test
	public void testFindsSingleMissingInRow() {

		final Integer[] values = { //
				1, 0, 2, 4, //
				0, 0, 0, 0, //
				0, 0, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = filledSudoku(values);

		assertThat(technique.findMoves(su)).containsExactly(//
				move(su, Strategy.NakedSingleInRow, 0, 1, 3));
	}

	@Test
	public void testFindsNoSingleOnDuplicates() {

		final Integer[] values = { //
				1, 0, 2, 2, //
				0, 0, 0, 0, //
				0, 0, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = filledSudoku(values);

		assertTrue(technique.findMoves(su).isEmpty());
	}

	@Test
	public void testFindsSingleMissingInCol() {

		final Integer[] values = { //
				1, 0, 0, 0, //
				0, 0, 0, 0, //
				2, 0, 0, 0, //
				3, 0, 0, 0 };
		final Sudoku su = filledSudoku(values);

		assertThat(technique.findMoves(su)).containsExactly(//
				move(su, Strategy.NakedSingleInCol, 1, 0, 4));
	}

	@Test
	public void testFindsSingleMissingInBlock() {

		final Integer[] values = { //
				0, 0, 2, 3, //
				0, 0, 4, 0, //
				0, 0, 1, 0, //
				0, 0, 3, 0 };

		final Sudoku su = filledSudoku(values);

		assertThat(technique.findMoves(su)).containsExactly(//
				move(su, Strategy.NakedSingleInBlock, 1, 3, 1));
	}

	@Test
	public void testFindsAllThreeOnSameCell() {

		final Integer[] values = { //
				1, 2, 0, 3, //
				0, 0, 4, 1, //
				0, 0, 2, 0, //
				0, 0, 3, 0 };

		final Sudoku su = filledSudoku(values);

		assertThat(technique.findMoves(su)).containsExactlyInAnyOrder(//
				move(su, Strategy.NakedSingleInCol, 0, 2, 1), //
				move(su, Strategy.NakedSingleInBlock, 0, 2, 2), //
				move(su, Strategy.NakedSingleInRow, 0, 2, 4));
	}
}
