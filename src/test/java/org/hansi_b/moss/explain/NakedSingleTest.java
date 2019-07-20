package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hansi_b.moss.SudokuTest.filledSudoku;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;
import org.junit.Test;

import static org.hansi_b.moss.explain.MoveAsserts.*;

public class NakedSingleTest {

	final NakedSingle technique = new NakedSingle();

	@Test
	public void testFindsTrivialSoleInRow() {

		final Integer[] values = { //
				1, 0, 2, 4, //
				0, 0, 0, 0, //
				0, 0, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = filledSudoku(values);

		assertThat(technique.findPossibleMoves(su)).containsExactly(//
				move(su, Strategy.NakedSingle, 0, 1, 3));
	}

	@Test
	public void testFindsSingleSoleFromCombination() {

		final Integer[] values = { //
				1, 0, 0, 0, //
				0, 0, 3, 0, //
				0, 2, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = filledSudoku(values);

		assertThat(technique.findPossibleMoves(su)).containsExactly(//
				move(su, Strategy.NakedSingle, 1, 1, 4));
	}

	@Test
	public void testFindsMultipleFromCombinations() {

		final Integer[] values = { //
				1, 0, 0, 0, //
				0, 0, 3, 0, //
				4, 2, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = filledSudoku(values);

		assertThat(technique.findPossibleMoves(su)).containsExactlyInAnyOrder( //
				move(su, Strategy.NakedSingle, 1, 0, 2), //
				move(su, Strategy.NakedSingle, 1, 1, 4), //
				move(su, Strategy.NakedSingle, 2, 2, 1), //
				move(su, Strategy.NakedSingle, 3, 0, 3));
	}
}
