package org.hansi_b.moss;

import static org.hansi_b.moss.SudokuTest.filledSudoku;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.util.Sets;
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

	@Test
	public void testGetCandidates() throws Exception {

		final Integer[] values = { //
				1, null, 2, 4, //
				null, null, null, null, //
				null, null, null, null, //
				null, null, null, null };
		final Sudoku su = filledSudoku(values);
		assertThat(su.getCell(Pos.at(0, 0)).getCandidates()).isEqualTo(//
				Sets.newLinkedHashSet(3));
		assertThat(su.getCell(Pos.at(0, 1)).getCandidates()).isEqualTo(//
				Sets.newLinkedHashSet(3));
		assertThat(su.getCell(Pos.at(1, 0)).getCandidates()).isEqualTo(//
				Sets.newLinkedHashSet(2, 3, 4));
		assertThat(su.getCell(Pos.at(2, 1)).getCandidates()).isEqualTo(//
				Sets.newLinkedHashSet(1, 2, 3, 4));
	}
}
