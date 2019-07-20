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
				null, null, null, null, //
				null, null, 1, null, //
				null, 1, null, null, //
				null, null, null, null };
		final Sudoku su = filledSudoku(values);

		assertThat(technique.findPossibleMoves(su)).containsExactly(//
				move(su, Strategy.SinglePosition, 0, 0, 1), //
				move(su, Strategy.SinglePosition, 3, 3, 1));
	}
}
