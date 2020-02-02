package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;
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
		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su)).containsExactly(//
				move(su, Strategy.NakedSingle, 0, 1, 3));
	}

	@Test
	public void testFindsSingleSoleFromCombination() {

		final Integer[] values = { //
				1, 0, 0, 0, //
				0, 0, 3, 0, //
				0, 2, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su)).containsExactly(//
				move(su, Strategy.NakedSingle, 1, 1, 4));
	}

	@Test
	public void testFindsMultipleFromCombinations() {

		final Integer[] values = { //
				1, 0, 0, 0, //
				0, 0, 3, 0, //
				4, 2, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su)).containsExactlyInAnyOrder( //
				move(su, Strategy.NakedSingle, 1, 0, 2), //
				move(su, Strategy.NakedSingle, 1, 1, 4), //
				move(su, Strategy.NakedSingle, 2, 2, 1), //
				move(su, Strategy.NakedSingle, 3, 0, 3));
	}

	@Test
	public void testFindBig() {

		/*
		 * from http://www.sudoku-space.de/sudoku-loesungstechniken/
		 */
		final Integer[] values = { //
				0, 0, 0, 0, 0, 1, 0, 0, 0, //
				0, 0, 0, 0, 0, 5, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				//
				0, 0, 0, 8, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 2, 0, 0, 0, 0, //
				0, 4, 0, 6, 0, 0, 7, 0, 0, //
				//
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 9, 0, 0, 0 //
		};
		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su)).containsExactlyInAnyOrder( //
				move(su, Strategy.NakedSingle, 5, 5, 3));
	}
}
