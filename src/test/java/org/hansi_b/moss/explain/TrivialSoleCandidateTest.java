package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Sets;
import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Sudoku;
import static org.hansi_b.moss.SudokuTest.givenSudoku;
import org.hansi_b.moss.explain.TrivialSoleCandidate;
import org.hansi_b.moss.explain.Move;
import org.hansi_b.moss.explain.Move.Strategy;
import org.junit.Test;

public class TrivialSoleCandidateTest {

	final TrivialSoleCandidate singleEmpty = new TrivialSoleCandidate();

	@Test
	public void testFindsSingleMissingInRow() {

		final Integer[] values = { //
				1, null, 2, 4, //
				null, null, null, null, //
				null, null, null, null, //
				null, null, null, null };
		final Sudoku su = givenSudoku(values);

		final List<Move> candidates = singleEmpty.findPossibleMoves(su);

		assertThat(candidates.size()).isEqualTo(1);

		assertThatMoveIs(candidates.get(0), Strategy.SingleMissingNumberInRow, 0, 1, 3);
	}

	@Test
	public void testFindsNoSingleOnDuplicates() {

		final Integer[] values = { //
				1, null, 2, 2, //
				null, null, null, null, //
				null, null, null, null, //
				null, null, null, null };
		final Sudoku su = givenSudoku(values);

		assertTrue(singleEmpty.findPossibleMoves(su).isEmpty());
	}

	@Test
	public void testFindsSingleMissingInCol() {

		final Integer[] values = { //
				1, null, null, null, //
				null, null, null, null, //
				2, null, null, null, //
				3, null, null, null };
		final Sudoku su = givenSudoku(values);

		final List<Move> candidates = singleEmpty.findPossibleMoves(su);

		assertThat(candidates.size()).isEqualTo(1);

		assertThatMoveIs(candidates.get(0), Strategy.SingleMissingNumberInCol, 1, 0, 4);
	}

	@Test
	public void testFindsSingleMissingInBlock() {

		final Integer[] values = { //
				null, null, 2, 3, //
				null, null, 4, null, //
				null, null, 1, null, //
				null, null, 3, null };

		final Sudoku su = givenSudoku(values);

		final List<Move> candidates = singleEmpty.findPossibleMoves(su);

		assertThat(candidates.size()).isEqualTo(1);

		assertThatMoveIs(candidates.get(0), Strategy.SingleMissingNumberInBlock, 1, 3, 1);
	}

	@Test
	public void testFindsAllThreeOnSameCell() {

		final Integer[] values = { //
				1, 2, null, 3, //
				null, null, 4, 1, //
				null, null, 2, null, //
				null, null, 3, null };

		final Sudoku su = givenSudoku(values);

		final List<Move> candidates = singleEmpty.findPossibleMoves(su);

		assertThat(candidates.stream().map(Move::getStrategy).collect(Collectors.toSet()))//
				.isEqualTo(Sets.newLinkedHashSet(//
						Strategy.SingleMissingNumberInRow, //
						Strategy.SingleMissingNumberInCol, //
						Strategy.SingleMissingNumberInBlock//
				));
		assertThat(candidates.stream().map(Move::getNewValue).collect(Collectors.toSet()))//
				.isEqualTo(Sets.newLinkedHashSet(4, 1, 2));
	}

	private static void assertThatMoveIs(final Move move, final Strategy expectedStrategy, final int expectedRowIdx,
			final int expectedColIdx, final int expectedNewValue) {
		assertThat(move.getStrategy()).isEqualTo(expectedStrategy);
		final Cell c = move.getCell();
		assertThat(c.getRow()).isEqualTo(expectedRowIdx);
		assertThat(c.getCol()).isEqualTo(expectedColIdx);
		assertThat(move.getNewValue()).isEqualTo(expectedNewValue);
	}
}
