package org.hansi_b.moss;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hansi_b.moss.SudokuTest.*;
import org.hansi_b.moss.CellGroup.Type;
import org.junit.Test;

public class CellGroupTest {

	@Test
	public void testRowValues() {
		final Integer[] values = { //
				1, null, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, null, //
				null, 2, 1, 3 };

		final Sudoku su = givenSudoku(values);
		assertThat(su.getGroup(Type.Row, 0).values()).isEqualTo(listOf(1, null, 4, 2));
		assertThat(su.getGroup(Type.Row, 1).values()).isEqualTo(listOf(2, 4, null, 1));
		assertThat(su.getGroup(Type.Row, 2).values()).isEqualTo(listOf(3, 1, 2, null));
		assertThat(su.getGroup(Type.Row, 3).values()).isEqualTo(listOf(null, 2, 1, 3));
	}

	@Test
	public void testColValues() {
		final Integer[] values = { //
				1, null, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, null, //
				null, 2, 1, 3 };
		final Sudoku su = givenSudoku(values);
		assertThat(su.getGroup(Type.Col, 0).values()).isEqualTo(listOf(1, 2, 3, null));
		assertThat(su.getGroup(Type.Col, 1).values()).isEqualTo(listOf(null, 4, 1, 2));
		assertThat(su.getGroup(Type.Col, 2).values()).isEqualTo(listOf(4, null, 2, 1));
		assertThat(su.getGroup(Type.Col, 3).values()).isEqualTo(listOf(2, 1, null, 3));
	}

	@Test
	public void testBlockValues() {
		final Integer[] values = { //
				1, null, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, null, //
				null, 2, 1, 3 };

		final Sudoku su = givenSudoku(values);

		assertThat(su.getGroup(Type.Block, 0).values()).isEqualTo(listOf(1, null, 2, 4));
		assertThat(su.getGroup(Type.Block, 1).values()).isEqualTo(listOf(4, 2, null, 1));
		assertThat(su.getGroup(Type.Block, 2).values()).isEqualTo(listOf(3, 1, null, 2));
		assertThat(su.getGroup(Type.Block, 3).values()).isEqualTo(listOf(2, null, 1, 3));
	}
}
