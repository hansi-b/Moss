package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hansi_b.moss.testSupport.Shortcuts.insert;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;
import org.junit.jupiter.api.Test;

class TrivialNakedSingleTest {

	final TrivialNakedSingle technique = new TrivialNakedSingle();

	@Test
	void testFindsSingleMissingInRow() {

		final Integer[] values = { //
				1, 0, 2, 4, //
				0, 0, 0, 0, //
				0, 0, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su, new PencilMarks())).containsExactly(//
				insert(su, Strategy.NakedSingleInRow, 0, 1, 3));
	}

	@Test
	void testFindsNoSingleOnDuplicates() {

		final Integer[] values = { //
				1, 0, 2, 2, //
				0, 0, 0, 0, //
				0, 0, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = Sudoku.filled(values);

		assertTrue(technique.findMoves(su, new PencilMarks()).findAny().isEmpty());
	}

	@Test
	void testFindsSingleMissingInCol() {

		final Integer[] values = { //
				1, 0, 0, 0, //
				0, 0, 0, 0, //
				2, 0, 0, 0, //
				3, 0, 0, 0 };
		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su, new PencilMarks())).containsExactly(//
				insert(su, Strategy.NakedSingleInCol, 1, 0, 4));
	}

	@Test
	void testFindsSingleMissingInBlock() {

		final Integer[] values = { //
				0, 0, 2, 3, //
				0, 0, 4, 0, //
				0, 0, 1, 0, //
				0, 0, 3, 0 };

		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su, new PencilMarks())).containsExactly(//
				insert(su, Strategy.NakedSingleInBlock, 1, 3, 1));
	}

	@Test
	void testFindsAllThreeOnSameCell() {

		final Integer[] values = { //
				1, 2, 0, 3, //
				0, 0, 4, 1, //
				0, 0, 2, 0, //
				0, 0, 3, 0 };

		final Sudoku su = Sudoku.filled(values);

		assertThat(technique.findMoves(su, new PencilMarks())).containsExactlyInAnyOrder(//
				insert(su, Strategy.NakedSingleInCol, 0, 2, 1), //
				insert(su, Strategy.NakedSingleInBlock, 0, 2, 2), //
				insert(su, Strategy.NakedSingleInRow, 0, 2, 4));
	}
}
