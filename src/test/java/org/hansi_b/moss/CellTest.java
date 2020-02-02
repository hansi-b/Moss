package org.hansi_b.moss;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.junit.Test;

public class CellTest {

	@Test
	public void testIdentity() {

		final Sudoku su1 = Sudoku.empty();
		su1.set(0, 0, 2);
		final Sudoku su2 = Sudoku.empty();
		su2.set(0, 0, 2);

		assertSame(su1.iterator().next(), su1.iterator().next());
		assertNotSame(su1.iterator().next(), su2.iterator().next());
	}

	@Test
	public void testGetCandidates() {

		final Integer[] values = { //
				1, 0, 2, 4, //
				0, 0, 0, 0, //
				0, 0, 0, 0, //
				0, 0, 0, 0 };
		final Sudoku su = Sudoku.filled(values);
		assertThat(su.getCell(Pos.at(0, 0)).getCandidates()).isEqualTo(//
				newLinkedHashSet(3));
		assertThat(su.getCell(Pos.at(0, 1)).getCandidates()).isEqualTo(//
				newLinkedHashSet(3));
		assertThat(su.getCell(Pos.at(1, 0)).getCandidates()).isEqualTo(//
				newLinkedHashSet(2, 3, 4));
		assertThat(su.getCell(Pos.at(2, 1)).getCandidates()).isEqualTo(//
				newLinkedHashSet(1, 2, 3, 4));
	}
}
