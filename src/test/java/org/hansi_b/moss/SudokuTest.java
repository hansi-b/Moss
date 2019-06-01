package org.hansi_b.moss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
		assertEquals(0, su.get(1, 1));
		assertEquals(0, su.get(5, 5));
		assertEquals(0, su.get(9, 9));
	}

	@Test
	public void testGetWithIllegalArgs() {
		final Sudoku su = new Sudoku();
		assertThatExceptionThrownBy(() -> su.get(0, 1)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Row argument must be positive and at most 9 (is 0)");
		assertThatExceptionThrownBy(() -> su.get(1, 0)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Column argument must be positive and at most 9 (is 0)");
		assertThatExceptionThrownBy(() -> su.get(10, 1)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Row argument must be positive and at most 9 (is 10)");
		assertThatExceptionThrownBy(() -> su.get(1, 10)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Column argument must be positive and at most 9 (is 10)");
	}

	@Test
	public void testSetAndGet() {
		final Sudoku su = new Sudoku();
		su.set(1, 2, 3);
		assertEquals(3, su.get(1, 2));
	}

	@Test
	public void testGetWithIllegalValueArgs() {
		final Sudoku su = new Sudoku();
		assertThatExceptionThrownBy(() -> {
			su.set(1, 1, 0);
			return null;
		}).isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Cell value argument must be positive and at most 9 (is 0)");
		assertThatExceptionThrownBy(() -> {
			su.set(1, 1, 10);
			return null;
		}).isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Cell value argument must be positive and at most 9 (is 10)");
	}

	@Test
	public void testUnset() {
		final Sudoku su = new Sudoku();
		su.set(1, 2, 3);
		assertEquals(3, su.get(1, 2));
		su.unset(1, 2);
		assertEquals(0, su.get(1, 2));
	}

	@Test
	public void testUnsetIsAllowedOnEmptyCell() {
		final Sudoku su = new Sudoku();
		assertEquals(0, su.get(1, 2));
		su.unset(1, 2);
		assertEquals(0, su.get(1, 2));
	}

}
