package org.hansi_b.moss.explain.technique;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hansi_b.moss.testsupport.Shortcuts.insert;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;
import org.hansi_b.moss.explain.PencilMarks;
import org.junit.jupiter.api.Test;

class HiddenSingleTest {

	final HiddenSingle technique = new HiddenSingle();

	@Test
	void testFindsMultipleHidden() {

		final Integer[] values = { //
				0, 0, 0, 0, //
				0, 0, 1, 0, //
				0, 1, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su, new PencilMarks())).containsExactlyInAnyOrder(//
				insert(su, Strategy.HiddenSingleInRow, 0, 0, 1), //
				insert(su, Strategy.HiddenSingleInCol, 0, 0, 1), //
				insert(su, Strategy.HiddenSingleInBlock, 0, 0, 1), //
				insert(su, Strategy.HiddenSingleInRow, 3, 3, 1), //
				insert(su, Strategy.HiddenSingleInCol, 3, 3, 1), //
				insert(su, Strategy.HiddenSingleInBlock, 3, 3, 1));
	}

	@Test
	void testFindsSingleHidden() {

		final Integer[] values = { //
				0, 0, 0, 0, //
				2, 0, 0, 0, //
				0, 1, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su, new PencilMarks())).containsExactlyInAnyOrder(//
				insert(su, Strategy.HiddenSingleInCol, 0, 0, 1), //
				insert(su, Strategy.HiddenSingleInBlock, 0, 0, 1), //
				insert(su, Strategy.HiddenSingleInCol, 3, 1, 2), //
				insert(su, Strategy.HiddenSingleInBlock, 3, 1, 2) //
		);
	}

	@Test
	void testFindsSingleHiddenBig() {

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

		assertThat(technique.findMoves(su, new PencilMarks())).containsExactly(//
				insert(su, Strategy.HiddenSingleInRow, 4, 4, 4), //
				insert(su, Strategy.HiddenSingleInBlock, 4, 4, 4) //
		);
	}

	@Test
	void testBigSinglePositive() {
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

		assertThat(technique.findMoves(su, new PencilMarks())).containsExactlyInAnyOrder(//
				insert(su, Strategy.HiddenSingleInCol, 1, 0, 7), //
				insert(su, Strategy.HiddenSingleInBlock, 1, 0, 7),
				//
				insert(su, Strategy.HiddenSingleInRow, 3, 7, 7), //
				insert(su, Strategy.HiddenSingleInCol, 3, 7, 7), //
				insert(su, Strategy.HiddenSingleInBlock, 3, 7, 7),
				//
				insert(su, Strategy.HiddenSingleInCol, 4, 8, 2),
				//
				insert(su, Strategy.HiddenSingleInCol, 5, 1, 6), //
				insert(su, Strategy.HiddenSingleInBlock, 5, 1, 6),
				//
				insert(su, Strategy.HiddenSingleInRow, 6, 7, 6), //
				insert(su, Strategy.HiddenSingleInCol, 6, 7, 6), //
				insert(su, Strategy.HiddenSingleInBlock, 6, 7, 6) //
		);
	}
}
