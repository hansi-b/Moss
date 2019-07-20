package org.hansi_b.moss;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class SudokuTest {

	@Test
	public void testSimpleConstructor() {
		assertNotNull(givenDefaultSudoku());
		assertNotNull(givenSudokuOfSize(4));
	}

	@Test
	public void testConstructorWithInvalidSize() {
		assertThatThrownBy(() -> givenSudokuOfSize(5)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Sudoku cannot be initialised with a non-square size (got 5)");
	}

	@Test
	public void testGetOnEmptyNew() {
		final Sudoku su = givenDefaultSudoku();
		assertEquals(null, su.getValue(1, 1));
		assertEquals(null, su.getValue(5, 5));
		assertEquals(null, su.getValue(8, 8));
	}

	@Test
	public void testGetWithIllegalArgs() {
		final Sudoku su = givenDefaultSudoku();
		assertThatThrownBy(() -> su.getValue(-1, 0)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Row argument must not be negative and at most 8 (is -1)");
		assertThatThrownBy(() -> su.getValue(1, -1)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Column argument must not be negative and at most 8 (is -1)");
		assertThatThrownBy(() -> su.getValue(10, 0)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Row argument must not be negative and at most 8 (is 10)");
		assertThatThrownBy(() -> su.getValue(0, 10)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Column argument must not be negative and at most 8 (is 10)");
	}

	@Test
	public void testSetAndGet() {
		final Sudoku su = givenDefaultSudoku();
		su.set(1, 1, 3);
		assertThat(su.getValue(1, 1)).isEqualTo(3);
		su.set(3, 5, 6);
		assertThat(su.getValue(3, 5)).isEqualTo(6);
		su.set(8, 8, 7);
		assertThat(su.getValue(8, 8)).isEqualTo(7);
	}

	@Test
	public void testCreate() {
		final Integer[] values = { //
				1, 3, 4, 2, //
				2, 4, 3, 1, //
				3, 1, 2, 4, //
				4, 2, 1, 3 };
		final Sudoku su = givenSudoku(values);
		assertThat(su.getValue(0, 1)).isEqualTo(3);
	}

	@Test
	public void testCreateIncomplete() {
		final Integer[] values = { //
				1, 3, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, 4, //
				4, 2, 1, 3 };
		final Sudoku su = givenSudoku(values);
		assertNull(su.getValue(1, 2));
	}

	@Test
	public void testGetWithIllegalValueArgs() {
		final Sudoku su = givenDefaultSudoku();
		assertThatThrownBy(() -> {
			su.set(1, 1, 0);
		}).isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Cell value must be null or between one and at most 9 (is 0)");
		assertThatThrownBy(() -> {
			su.set(1, 1, 10);
		}).isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Cell value must be null or between one and at most 9 (is 10)");
	}

	@Test
	public void testUnset() {
		final Sudoku su = givenDefaultSudoku();
		su.set(0, 1, 3);
		assertThat(su.getValue(0, 1)).isEqualTo(3);
		su.set(0, 1, null);
		assertNull(su.getValue(0, 1));
	}

	@Test
	public void testUnsetIsAllowedOnEmptyCell() {
		final Sudoku su = givenDefaultSudoku();
		assertNull(su.getValue(0, 1));
		su.set(0, 1, null);
		assertNull(su.getValue(0, 1));
	}

	@Test
	public void testNewIsNotSolved() throws Exception {
		final Sudoku su = givenDefaultSudoku();
		assertFalse(su.isSolved());
	}

	@Test
	public void testCanSolve() {
		final Integer[] values = { //
				1, 3, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, 4, //
				4, 2, 1, 3 };
		final Sudoku su = givenSudoku(values);
		assertFalse(su.isSolved());
		su.set(1, 2, 3);
		assertTrue(su.isSolved());
	}

	@Test
	public void testGetBlockAtPos() {
		final Sudoku su = givenSudokuOfSize(4);

		final CellGroup block0 = su.getGroup(Type.Block, Pos.at(0, 0));
		assertSame(block0, su.getGroup(Type.Block, Pos.at(0, 1)));
		assertSame(block0, su.getGroup(Type.Block, Pos.at(1, 0)));
		assertSame(block0, su.getGroup(Type.Block, Pos.at(1, 1)));
		final CellGroup block1 = su.getGroup(Type.Block, Pos.at(2, 0));
		assertNotSame(block0, block1);
		assertSame(block1, su.getGroup(Type.Block, Pos.at(2, 1)));
	}

	@Test
	public void testGetGroupAtPos() {
		final Sudoku su = givenSudokuOfSize(4);

		final CellGroup block0 = su.getGroup(Type.Block, Pos.at(0, 0));
		assertSame(block0, su.getGroup(Type.Block, Pos.at(0, 1)));
		assertSame(block0, su.getGroup(Type.Block, Pos.at(1, 0)));
		assertSame(block0, su.getGroup(Type.Block, Pos.at(1, 1)));
	}

	public static Sudoku givenSudoku(final Integer[] values) {
		return new Sudoku.Factory().create(values);
	}

	public static Sudoku givenDefaultSudoku() {
		return new Sudoku.Factory().create();
	}

	public static Sudoku givenSudokuOfSize(final int size) {
		return new Sudoku.Factory().create(size);
	}

	public static List<Integer> listOf(final Integer... integers) {
		return Arrays.asList(integers);
	}
}
