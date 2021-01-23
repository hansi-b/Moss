package org.hansi_b.moss.explain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.draw.JavaArrayPrinter;
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
	void test_Zeit_schwer_2019_06_08() {

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

	@Test
	void test_Zeit_unknown_2019_11_09() {
		final Integer[] values = { //
				8, 0, 0, 0, 0, 0, 9, 0, 0, //
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
		assertTrue(solver.solve(su).isSolved());
	}

	@Disabled("Needs a more advanced technique")
	@Test
	void test_Zeit_unknown_2019_12_01() {
		final Integer[] values = { //
				0, 5, 3, 0, 0, 0, 4, 0, 0, //
				1, 0, 0, 4, 6, 3, 0, 0, 0, //
				0, 0, 6, 0, 0, 0, 0, 9, 0, //
				//
				0, 6, 0, 0, 0, 0, 0, 0, 7, //
				0, 0, 0, 0, 8, 9, 6, 5, 1, //
				0, 1, 8, 0, 0, 0, 0, 4, 0, //
				//
				6, 0, 0, 9, 7, 0, 3, 8, 0, //
				0, 4, 7, 0, 0, 5, 0, 0, 2, //
				0, 0, 9, 2, 0, 0, 0, 0, 0, //
		};
		final Sudoku su = Sudoku.filled(values);
		final Sudoku filled = solver.solve(su);
		System.out.println(new JavaArrayPrinter().draw(filled));
		assertTrue(filled.isSolved());
	}

	@Test
	void test_Zeit_unknown_2019_11_10() {
		final Integer[] values = { //
				5, 0, 4, 0, 6, 1, 0, 9, 0, //
				3, 1, 8, 9, 2, 5, 0, 7, 0, //
				0, 2, 0, 0, 7, 0, 1, 0, 0, //
				//
				9, 0, 0, 1, 0, 0, 7, 8, 0, //
				0, 0, 0, 6, 0, 0, 0, 0, 4, //
				0, 0, 0, 0, 0, 0, 0, 0, 1, //
				//
				0, 0, 0, 0, 5, 0, 0, 0, 3, //
				0, 0, 0, 3, 0, 0, 0, 0, 9, //
				0, 0, 0, 0, 4, 8, 5, 6, 0, //
		};
		final Sudoku su = Sudoku.filled(values);
		assertTrue(solver.solve(su).isSolved());
	}

	@Disabled("Needs a more advanced technique")
	@Test
	void test_Zeit_schwer_2020_10_16() {
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
		final Sudoku filled = solver.solve(su);
		System.out.println(new JavaArrayPrinter().draw(filled));
		assertTrue(filled.isSolved());
	}

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
		assertTrue(solver.solve(su).isSolved());
	}
}
