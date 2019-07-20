package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.explain.Move.Strategy;

class MoveAsserts {
	static void assertThatMoveIs(final Move move, final Strategy expectedStrategy, final int expectedRowIdx,
			final int expectedColIdx, final int expectedNewValue) {
		assertThat(move.getStrategy()).isEqualTo(expectedStrategy);
		final Cell c = move.getCell();
		assertThat(c.getRow()).isEqualTo(expectedRowIdx);
		assertThat(c.getCol()).isEqualTo(expectedColIdx);
		assertThat(move.getNewValue()).isEqualTo(expectedNewValue);
	}
}