package org.hansi_b.moss.explain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Explainer;
import org.hansi_b.moss.explain.Move;
import org.junit.Test;

public class ExplainerTest {

	@Test
	public void testCreate() {
		final Sudoku su = Sudoku.create(//
				1, 3, 4, 2, //
				2, 4, null, 1, // needs 3
				3, 1, 2, 4, //
				4, 2, 1, 3);

		final Explainer ex = new Explainer(su);

		final List<Move> candidates = ex.getPossibleMoves();
		assertThat(candidates.size()).isEqualTo(1);
		Move m = candidates.get(0);
		assertThat(m.getNewValue()).isEqualTo(3);
	}
}
