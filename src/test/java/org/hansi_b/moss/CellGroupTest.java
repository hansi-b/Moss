package org.hansi_b.moss;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.*;

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

		final Sudoku su = filledSudoku(values);
		assertThat(row(su, 0).values()).isEqualTo(list(1, null, 4, 2));
		assertThat(row(su, 1).values()).isEqualTo(list(2, 4, null, 1));
		assertThat(row(su, 2).values()).isEqualTo(list(3, 1, 2, null));
		assertThat(row(su, 3).values()).isEqualTo(list(null, 2, 1, 3));
	}

	@Test
	public void testColValues() {
		final Integer[] values = { //
				1, null, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, null, //
				null, 2, 1, 3 };
		final Sudoku su = filledSudoku(values);
		assertThat(col(su, 0).values()).isEqualTo(list(1, 2, 3, null));
		assertThat(col(su, 1).values()).isEqualTo(list(null, 4, 1, 2));
		assertThat(col(su, 2).values()).isEqualTo(list(4, null, 2, 1));
		assertThat(col(su, 3).values()).isEqualTo(list(2, 1, null, 3));
	}

	@Test
	public void testBlockValues() {
		final Integer[] values = { //
				1, null, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, null, //
				null, 2, 1, 3 };

		final Sudoku su = filledSudoku(values);

		assertThat(block(su, 0).values()).isEqualTo(list(1, null, 2, 4));
		assertThat(block(su, 1).values()).isEqualTo(list(4, 2, null, 1));
		assertThat(block(su, 2).values()).isEqualTo(list(3, 1, null, 2));
		assertThat(block(su, 3).values()).isEqualTo(list(2, null, 1, 3));
	}

	@Test
	public void testBlockMissing() {
		final Integer[] values = { //
				1, null, 4, 2, //
				2, 4, null, null, //
				3, null, null, null, //
				null, null, null, null };

		final Sudoku su = filledSudoku(values);

		assertThat(block(su, 0).missing()).isEqualTo(newTreeSet(3));
		assertThat(block(su, 1).missing()).isEqualTo(newTreeSet(1, 3));
		assertThat(block(su, 2).missing()).isEqualTo(newTreeSet(1, 2, 4));
		assertThat(block(su, 3).missing()).isEqualTo(newTreeSet(1, 2, 3, 4));
	}

	@Test
	public void testRowMissingSpecialCases() {
		final Integer[] values = { //
				1, 1, 1, 1, //
				null, null, null, null, //
				null, null, null, null, //
				null, null, null, null };

		final Sudoku su = filledSudoku(values);

		assertThat(row(su, 0).missing()).isEqualTo(newTreeSet(2, 3, 4));
		assertThat(row(su, 1).missing()).isEqualTo(su.possibleValues());
	}

	private static CellGroup row(final Sudoku su, final int groupIndex) {
		return su.getGroup(Type.Row, groupIndex);
	}

	private static CellGroup col(final Sudoku su, final int groupIndex) {
		return su.getGroup(Type.Col, groupIndex);
	}

	private static CellGroup block(final Sudoku su, final int groupIndex) {
		return su.getGroup(Type.Block, groupIndex);
	}
}
