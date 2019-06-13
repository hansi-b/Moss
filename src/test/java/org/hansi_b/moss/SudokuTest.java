package org.hansi_b.moss;

import static org.junit.Assert.*;
import org.hansi_b.moss.Sudoku;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class SudokuTest {

	@Test
	public void testSimpleConstructor() {
		assertNotNull(new Sudoku());
		assertNotNull(new Sudoku(4));
	}

	@Test
	public void testConstructorWithInvalidSize() {
		assertThatExceptionThrownBy(() -> new Sudoku(5)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Sudoku cannot be initialised with a non-square size (got 5)");
	}

	@Test
	public void testGetOnEmptyNew() {
		final Sudoku su = new Sudoku();
		assertEquals(null, su.getValue(1, 1));
		assertEquals(null, su.getValue(5, 5));
		assertEquals(null, su.getValue(9, 9));
	}

	@Test
	public void testGetWithIllegalArgs() {
		final Sudoku su = new Sudoku();
		assertThatExceptionThrownBy(() -> su.getValue(0, 1)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Row argument must be positive and at most 9 (is 0)");
		assertThatExceptionThrownBy(() -> su.getValue(1, 0)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Column argument must be positive and at most 9 (is 0)");
		assertThatExceptionThrownBy(() -> su.getValue(10, 1)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Row argument must be positive and at most 9 (is 10)");
		assertThatExceptionThrownBy(() -> su.getValue(1, 10)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Column argument must be positive and at most 9 (is 10)");
	}

	@Test
	public void testSetAndGet() {
		final Sudoku su = new Sudoku();
		su.set(1, 1, 3);
		assertThat(su.getValue(1, 1)).isEqualTo(3);
		su.set(3, 5, 6);
		assertThat(su.getValue(3, 5)).isEqualTo(6);
		su.set(9, 9, 7);
		assertThat(su.getValue(9, 9)).isEqualTo(7);
	}

	@Test
	public void testCreate() {
		final Sudoku su = Sudoku.create(//
				1, 3, 4, 2, //
				2, 4, 3, 1, //
				3, 1, 2, 4, //
				4, 2, 1, 3);
		assertThat(su.getValue(1, 2)).isEqualTo(3);
	}

	@Test
	public void testCreateIncomplete() {
		final Sudoku su = Sudoku.create(//
				1, 3, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, 4, //
				4, 2, 1, 3);
		assertNull(su.getValue(2, 3));
	}

	@Test
	public void testGetWithIllegalValueArgs() {
		final Sudoku su = new Sudoku();
		assertThatExceptionThrownBy(() -> {
			su.set(1, 1, 0);
			return null;
		}).isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Cell value must be null or between one and at most 9 (is 0)");
		assertThatExceptionThrownBy(() -> {
			su.set(1, 1, 10);
			return null;
		}).isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Cell value must be null or between one and at most 9 (is 10)");
	}

	@Test
	public void testUnset() {
		final Sudoku su = new Sudoku();
		su.set(1, 2, 3);
		assertThat(su.getValue(1, 2)).isEqualTo(3);
		su.set(1, 2, null);
		assertNull(su.getValue(1, 2));
	}

	@Test
	public void testUnsetIsAllowedOnEmptyCell() {
		final Sudoku su = new Sudoku();
		assertNull(su.getValue(1, 2));
		su.set(1, 2, null);
		assertNull(su.getValue(1, 2));
	}

	@Test
	public void testNewIsNotSolved() throws Exception {
		final Sudoku su = new Sudoku();
		assertFalse(su.isSolved());
	}

	@Test
	public void testCanSolve() {
		final Sudoku su = Sudoku.create(//
				1, 3, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, 4, //
				4, 2, 1, 3);
		assertFalse(su.isSolved());
		su.set(2, 3, 3);
		assertTrue(su.isSolved());
	}

	@Test
	public void testGetRow() {
		final Sudoku su = Sudoku.create(//
				1, null, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, null, //
				null, 2, 1, 3);
		assertThat(su.getRow(1)).isEqualTo(new Integer[] { 1, null, 4, 2 });
		assertThat(su.getRow(2)).isEqualTo(new Integer[] { 2, 4, null, 1 });
		assertThat(su.getRow(3)).isEqualTo(new Integer[] { 3, 1, 2, null });
		assertThat(su.getRow(4)).isEqualTo(new Integer[] { null, 2, 1, 3 });
	}

	@Test
	public void testGetCol() {
		final Sudoku su = Sudoku.create(//
				1, null, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, null, //
				null, 2, 1, 3);
		assertThat(su.getCol(1)).isEqualTo(new Integer[] { 1, 2, 3, null });
		assertThat(su.getCol(2)).isEqualTo(new Integer[] { null, 4, 1, 2 });
		assertThat(su.getCol(3)).isEqualTo(new Integer[] { 4, null, 2, 1 });
		assertThat(su.getCol(4)).isEqualTo(new Integer[] { 2, 1, null, 3 });
	}

	@Test
	public void testGetBloc() {
		final Sudoku su = Sudoku.create(//
				1, null, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, null, //
				null, 2, 1, 3);

		assertThat(su.getBlock(1)).isEqualTo(new Integer[] { 1, null, 2, 4 });
		assertThat(su.getBlock(2)).isEqualTo(new Integer[] { 4, 2, null, 1 });
		assertThat(su.getBlock(3)).isEqualTo(new Integer[] { 3, 1, null, 2 });
		assertThat(su.getBlock(4)).isEqualTo(new Integer[] { 2, null, 1, 3 });
	}
}
