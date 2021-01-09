package org.hansi_b.moss.explain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.draw.AsciiPainter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SolverTest {

	final Solver solver = new Solver();

	@Test
	void testEasy() {

		/*
		 * from a city magazine (easy)
		 */
		final Integer[] values = { //
				0, 0, 1, 0, 9, 0, 0, 6, 0, //
				0, 0, 3, 0, 7, 0, 0, 0, 0, //
				6, 2, 0, 0, 3, 0, 5, 0, 0, //
				//
				0, 0, 0, 7, 0, 0, 6, 0, 0, //
				4, 8, 6, 0, 0, 2, 7, 5, 0, //
				0, 0, 0, 0, 6, 0, 0, 4, 1, //
				//
				0, 0, 5, 6, 2, 0, 0, 0, 0, //
				1, 0, 0, 0, 8, 5, 0, 0, 6, //
				0, 0, 0, 0, 0, 7, 0, 8, 0, //
		};
		final Sudoku su = Sudoku.filled(values);
		assertTrue(solver.solve(su).isSolved());
	}

	@Test
	void testLittleDifficult() {

		/*
		 * from a city magazine (difficult)
		 */
		final Integer[] values = { //
				0, 4, 0, 0, 8, 0, 7, 0, 0, //
				5, 0, 8, 0, 0, 0, 0, 9, 0, //
				0, 9, 0, 0, 7, 3, 0, 2, 0, //
				//
				0, 0, 0, 0, 0, 8, 9, 0, 0, //
				9, 0, 6, 0, 0, 1, 0, 0, 0, //
				0, 0, 7, 4, 5, 0, 0, 0, 0, //
				//
				4, 0, 0, 2, 0, 0, 0, 5, 3, //
				0, 6, 5, 0, 0, 0, 2, 0, 0, //
				0, 0, 0, 0, 0, 0, 8, 0, 0 //
		};
		final Sudoku su = Sudoku.filled(values);
		assertTrue(solver.solve(su).isSolved());
	}

	@Test
	void testSehrSchwer() {

		/*
		 * Die Zeit (sehr schwer, 8th June 2019)
		 */
		final Integer[] values = { //
				5, 0, 0, 0, 0, 0, 0, 4, 0, //
				0, 3, 0, 4, 0, 0, 0, 0, 0, //
				0, 1, 0, 6, 0, 5, 0, 0, 0, //
				//
				0, 5, 1, 0, 6, 0, 0, 0, 2, //
				0, 0, 7, 1, 0, 0, 0, 5, 0, //
				2, 0, 0, 0, 7, 9, 0, 8, 3, //
				//
				0, 0, 2, 7, 0, 3, 9, 0, 0, //
				4, 0, 3, 2, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 8, 0, 0, 2, 0 //
		};
		final Sudoku su = Sudoku.filled(values);
		final Sudoku filled = solver.solve(su);
		assertTrue(filled.isSolved());
	}

	@Disabled("Cannot be solved yet")
	@Test
	void testSehrSchwer02() {
		/*
		 * Die Zeit (sehr schwer, 16th October 2019)
		 */
		final Integer[] values = { //
				6, 0, 0, 9, 0, 2, 0, 0, 0, //
				0, 8, 4, 0, 0, 0, 0, 6, 9, //
				0, 0, 0, 0, 0, 6, 2, 0, 0, //
				//
				0, 0, 0, 0, 9, 0, 0, 1, 0, //
				0, 1, 0, 8, 5, 0, 0, 2, 0, //
				0, 3, 7, 0, 0, 1, 4, 0, 0, //
				//
				1, 0, 0, 0, 0, 0, 7, 0, 0, //
				0, 6, 3, 0, 0, 0, 0, 5, 0, //
				0, 0, 5, 0, 7, 3, 0, 4, 0 //
		};
		final Sudoku su = Sudoku.filled(values);
		assertTrue(solver.solve(su).isSolved());
	}

	@Disabled("Cannot be solved yet")
	@Test
	void test_Zeit_schwer_2020_10_19() {
		final Integer[] values = { //
				8, 0, 0, 0, 0, 0, 9, 1, 0, //
				0, 0, 5, 6, 9, 0, 3, 2, 0, //
				0, 4, 0, 0, 1, 0, 5, 7, 0, //
				//
				0, 9, 0, 0, 0, 0, 0, 6, 0, //
				6, 0, 0, 1, 0, 0, 0, 8, 0, //
				5, 0, 0, 0, 0, 2, 0, 0, 0, //
				//
				0, 0, 3, 4, 2, 0, 0, 0, 0, //
				0, 8, 0, 0, 5, 1, 0, 0, 2, //
				2, 0, 0, 0, 7, 0, 0, 4, 0, //
		};
		final Sudoku su = Sudoku.filled(values);
		final Sudoku filled = solver.solve(su);
		System.out.println(new AsciiPainter().draw(filled));
		assertTrue(filled.isSolved());
	}
}
