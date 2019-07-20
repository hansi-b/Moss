package org.hansi_b.moss;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class CellTest {

	@Test
	public void testIdentity() throws Exception {

		final Sudoku su1 = new Sudoku.Factory().create();
		su1.set(0, 0, 2);
		final Sudoku su2 = new Sudoku.Factory().create();
		su2.set(0, 0, 2);

		assertSame(su1.iterator().next(), su1.iterator().next());
		assertNotSame(su1.iterator().next(), su2.iterator().next());
	}
}
