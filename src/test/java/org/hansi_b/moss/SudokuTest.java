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
		assertEquals(null, su.get(1, 1));
		assertEquals(null, su.get(5, 5));
		assertEquals(null, su.get(9, 9));
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
		su.set(1, 1, 3);
		assertThat(su.get(1, 1)).isEqualTo(3);
		su.set(3, 5, 6);
		assertThat(su.get(3, 5)).isEqualTo(6);
		su.set(9, 9, 7);
		assertThat(su.get(9, 9)).isEqualTo(7);
	}

	@Test
	public void testCreate() {
		final Sudoku su = Sudoku.create(//
				1, 3, 4, 2, //
				2, 4, 3, 1, //
				3, 1, 2, 4, //
				4, 2, 1, 3);
		assertThat(su.get(1, 2)).isEqualTo(3);
	}

	@Test
	public void testCreateIncomplete() {
		final Sudoku su = Sudoku.create(//
				1, 3, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, 4, //
				4, 2, 1, 3);
		assertNull(su.get(2, 3));
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
		assertThat(su.get(1, 2)).isEqualTo(3);
		su.set(1, 2, null);
		assertNull(su.get(1, 2));
	}

	@Test
	public void testUnsetIsAllowedOnEmptyCell() {
		final Sudoku su = new Sudoku();
		assertNull(su.get(1, 2));
		su.set(1, 2, null);
		assertNull(su.get(1, 2));
	}
}
