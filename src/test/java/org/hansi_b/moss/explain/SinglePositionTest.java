package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hansi_b.moss.SudokuTest.filledSudoku;
import static org.hansi_b.moss.explain.MoveAsserts.move;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;
import org.junit.Test;

public class SinglePositionTest {

	final SinglePosition technique = new SinglePosition();

	@Test
	public void testFindsSinglePosition() {

		final Integer[] values = { //
				0, 0, 0, 0, //
				0, 0, 1, 0, //
				0, 1, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = filledSudoku(values);

		assertThat(technique.findPossibleMoves(su)).containsExactly(//
				move(su, Strategy.SinglePosition, 0, 0, 1), //
				move(su, Strategy.SinglePosition, 3, 3, 1));
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

		final Sudoku su = filledSudoku(values);

		assertThat(technique.findPossibleMoves(su)).containsExactly(//
				move(su, Strategy.SinglePosition, 3, 7, 7), //
				move(su, Strategy.SinglePosition, 6, 7, 6));
	}
}
