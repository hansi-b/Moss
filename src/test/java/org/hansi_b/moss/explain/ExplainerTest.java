package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Explainer;
import org.hansi_b.moss.explain.Move;
import org.hansi_b.moss.explain.Move.Strategy;
import org.junit.Test;

public class ExplainerTest {

	@Test
	public void testFindsSingleMissingInRow() {

		final Sudoku su = Sudoku.create(//
				1, null, 2, 4, //
				null, null, null, null, //
				null, null, null, null, //
				null, null, null, null);

		final Explainer ex = new Explainer(su);

		final List<Move> candidates = ex.getPossibleMoves();

		assertThat(candidates.size()).isEqualTo(1);

		assertThatMoveIs(candidates.get(0), Strategy.SingleMissingNumberInRow, 1, 2, 3);
	}

	@Test
	public void testFindsNoSingleOnDuplicates() {

		final Sudoku su = Sudoku.create(//
				1, null, 2, 2, //
				null, null, null, null, //
				null, null, null, null, //
				null, null, null, null);

		assertTrue(new Explainer(su).getPossibleMoves().isEmpty());
	}

	@Test
	public void testFindsSingleMissingInCol() {

		final Sudoku su = Sudoku.create(//
				1, null, null, null, //
				null, null, null, null, //
				2, null, null, null, //
				3, null, null, null);

		final Explainer ex = new Explainer(su);

		final List<Move> candidates = ex.getPossibleMoves();

		assertThat(candidates.size()).isEqualTo(1);

		assertThatMoveIs(candidates.get(0), Strategy.SingleMissingNumberInCol, 2, 1, 4);
	}

	@Test
	public void testFindsSingleMissingInBlock() {

		final Sudoku su = Sudoku.create(//
				null, null, 2, 3, //
				null, null, 4, null, //
				null, null, 1, null, //
				null, null, 3, null);

		final Explainer ex = new Explainer(su);

		final List<Move> candidates = ex.getPossibleMoves();

		assertThat(candidates.size()).isEqualTo(1);

		assertThatMoveIs(candidates.get(0), Strategy.SingleMissingNumberInBlock, 2, 4, 1);
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
