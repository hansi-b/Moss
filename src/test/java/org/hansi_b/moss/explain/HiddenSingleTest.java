package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hansi_b.moss.testSupport.Shortcuts.move;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;
import org.junit.jupiter.api.Test;

public class HiddenSingleTest {

	final HiddenSingle technique = new HiddenSingle();

	@Test
	public void testFindsMultipleHidden() {

		final Integer[] values = { //
				0, 0, 0, 0, //
				0, 0, 1, 0, //
				0, 1, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su)).containsExactlyInAnyOrder(//
				move(su, Strategy.HiddenSingleInRow, 0, 0, 1), //
				move(su, Strategy.HiddenSingleInCol, 0, 0, 1), //
				move(su, Strategy.HiddenSingleInBlock, 0, 0, 1), //
				move(su, Strategy.HiddenSingleInRow, 3, 3, 1), //
				move(su, Strategy.HiddenSingleInCol, 3, 3, 1), //
				move(su, Strategy.HiddenSingleInBlock, 3, 3, 1));
	}

	@Test
	public void testFindsSingleHidden() {

		final Integer[] values = { //
				0, 0, 0, 0, //
				2, 0, 0, 0, //
				0, 1, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su)).containsExactlyInAnyOrder(//
				move(su, Strategy.HiddenSingleInCol, 0, 0, 1), //
				move(su, Strategy.HiddenSingleInBlock, 0, 0, 1), //
				move(su, Strategy.HiddenSingleInCol, 3, 1, 2), //
				move(su, Strategy.HiddenSingleInBlock, 3, 1, 2) //
		);
	}

	@Test
	public void testFindsSingleHiddenBig() {

		/*
		 * from http://www.sudoku-space.de/sudoku-loesungstechniken/
		 */
		final Integer[] values = { //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				//
				0, 4, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 3, 0, 7, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 4, 0, 0, //
				//
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0 //
		};
		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su)).containsExactly(//
				move(su, Strategy.HiddenSingleInRow, 4, 4, 4), //
				move(su, Strategy.HiddenSingleInBlock, 4, 4, 4) //
		);
	}

	@Test
	public void testBigSinglePositive() {
		/*
		 * the explained example from
		 * https://www.sudokuoftheday.com/techniques/single-position/
		 */
		final Integer[] values = { //
				0, 0, 6, 0, 3, 0, 7, 0, 8, //
				0, 3, 0, 0, 0, 0, 0, 0, 1, //
				2, 0, 0, 0, 0, 0, 6, 0, 0, //
				//
				1, 0, 0, 3, 5, 0, 0, 0, 6, //
				0, 7, 9, 0, 4, 0, 1, 5, 0, //
				5, 0, 0, 0, 1, 7, 0, 0, 4, //
				//
				0, 0, 2, 0, 0, 0, 0, 0, 7, //
				6, 0, 0, 0, 0, 0, 0, 8, 0, //
				4, 0, 7, 0, 6, 0, 2, 0, 0 //
		};

		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su)).containsExactlyInAnyOrder(//
				move(su, Strategy.HiddenSingleInCol, 1, 0, 7), //
				move(su, Strategy.HiddenSingleInBlock, 1, 0, 7),
				//
				move(su, Strategy.HiddenSingleInRow, 3, 7, 7), //
				move(su, Strategy.HiddenSingleInCol, 3, 7, 7), //
				move(su, Strategy.HiddenSingleInBlock, 3, 7, 7),
				//
				move(su, Strategy.HiddenSingleInCol, 4, 8, 2),
				//
				move(su, Strategy.HiddenSingleInCol, 5, 1, 6), //
				move(su, Strategy.HiddenSingleInBlock, 5, 1, 6),
				//
				move(su, Strategy.HiddenSingleInRow, 6, 7, 6), //
				move(su, Strategy.HiddenSingleInCol, 6, 7, 6), //
				move(su, Strategy.HiddenSingleInBlock, 6, 7, 6) //
		);
	}
}
