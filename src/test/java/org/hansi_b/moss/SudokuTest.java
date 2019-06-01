package org.hansi_b.moss;

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
}
