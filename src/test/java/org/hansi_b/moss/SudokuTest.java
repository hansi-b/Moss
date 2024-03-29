package org.hansi_b.moss;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Sets.newTreeSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;

class SudokuTest {

	@Test
	void testSimpleConstructor() {
		assertNotNull(Sudoku.empty());
		assertNotNull(Sudoku.empty(4));
	}

	@Test
	void testCopyOf() {

		final Sudoku su1 = Sudoku.empty();
		su1.set(4, 5, 1);

		final Sudoku su2 = su1.copy();
		for (final Cell c : su1)
			assertEquals(c.getValue(), su2.getCell(c.getPos()).getValue());

		su1.set(4, 5, 9);
		assertEquals(Integer.valueOf(1), su2.getValue(4, 5));
	}

	@Test
	void testConstructorWithInvalidSize() {
		assertThatThrownBy(() -> Sudoku.empty(5)) //
				.isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Sudoku cannot be initialised with a non-square size (got 5)");
	}

	@Test
	void testGetOnEmptyNew() {
		final Sudoku su = Sudoku.empty();
		assertEquals(null, su.getValue(1, 1));
		assertEquals(null, su.getValue(5, 5));
		assertEquals(null, su.getValue(8, 8));
	}

	@Test
	void testGetWithIllegalArgs() {
		final Sudoku su = Sudoku.empty();
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
	void testSetAndGet() {
		final Sudoku su = Sudoku.empty();
		su.set(1, 1, 3);
		assertThat(su.getValue(1, 1)).isEqualTo(3);
		su.set(3, 5, 6);
		assertThat(su.getValue(3, 5)).isEqualTo(6);
		su.set(8, 8, 7);
		assertThat(su.getValue(8, 8)).isEqualTo(7);
	}

	@Test
	void testEmptyBigEmpty() {

		/*
		 * mainly to have the empty template lying around
		 */
		final Integer[] values = { //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				//
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				//
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0 //
		};
		final Sudoku su = Sudoku.filled(values);
		assertEquals(81, Sets.newHashSet(su.streamEmptyCells()::iterator).size());
	}

	@Test
	void testEmpty() {
		final Integer[] values = { //
				1, 3, 4, 2, //
				2, 4, 3, 1, //
				3, 1, 2, 4, //
				4, 2, 1, 3 };
		final Sudoku su = Sudoku.filled(values);
		assertThat(su.getValue(0, 1)).isEqualTo(3);
	}

	@Test
	void testEmptyIncomplete() {
		final Integer[] values = { //
				1, 3, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, 4, //
				4, 2, 1, 3 };
		final Sudoku su = Sudoku.filled(values);
		assertNull(su.getValue(1, 2));
	}

	@Test
	void testGetWithIllegalValueArgs() {
		final Sudoku su = Sudoku.empty();
		assertThatThrownBy(() -> su.set(1, 1, 0)).isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Cell value must be null or between one and at most 9 (is 0)");
		assertThatThrownBy(() -> su.set(1, 1, 10)).isExactlyInstanceOf(IllegalArgumentException.class) //
				.hasMessage("Cell value must be null or between one and at most 9 (is 10)");
	}

	@Test
	void testUnset() {
		final Sudoku su = Sudoku.empty();
		su.set(0, 1, 3);
		assertThat(su.getValue(0, 1)).isEqualTo(3);
		su.set(0, 1, null);
		assertNull(su.getValue(0, 1));
	}

	@Test
	void testUnsetIsAllowedOnEmptyCell() {
		final Sudoku su = Sudoku.empty();
		assertNull(su.getValue(0, 1));
		su.set(0, 1, null);
		assertNull(su.getValue(0, 1));
	}

	@Test
	void testNewIsNotSolved() {
		final Sudoku su = Sudoku.empty();
		assertFalse(su.isSolved());
	}

	@Test
	void testPossibleValues() {
		final Sudoku su = Sudoku.empty();
		assertThat(su.possibleValues()).isEqualTo(newTreeSet(1, 2, 3, 4, 5, 6, 7, 8, 9));
		final Sudoku su4 = Sudoku.empty(4);
		assertThat(su4.possibleValues()).isEqualTo(newTreeSet(1, 2, 3, 4));
	}

	@Test
	void testIsSolved() {
		final Integer[] values = { //
				1, 3, 4, 2, //
				2, 4, null, 1, //
				3, 1, 2, 4, //
				4, 2, 1, 3 };
		final Sudoku su = Sudoku.filled(values);
		assertFalse(su.isSolved());
		su.set(1, 2, 3);
		assertTrue(su.isSolved());
	}

	@Test
	void testGetBlockAtPos() {
		final Sudoku su = Sudoku.empty(4);

		final CellGroup block0 = su.getGroup(GroupType.Block, Pos.at(0, 0));
		assertSame(block0, su.getGroup(GroupType.Block, Pos.at(0, 1)));
		assertSame(block0, su.getGroup(GroupType.Block, Pos.at(1, 0)));
		assertSame(block0, su.getGroup(GroupType.Block, Pos.at(1, 1)));
		final CellGroup block1 = su.getGroup(GroupType.Block, Pos.at(2, 0));
		assertNotSame(block0, block1);
		assertSame(block1, su.getGroup(GroupType.Block, Pos.at(2, 1)));
	}

	@Test
	void testGetGroupAtPos() {
		final Sudoku su = Sudoku.empty(4);

		final CellGroup block0 = su.getGroup(GroupType.Block, Pos.at(0, 0));
		assertSame(block0, su.getGroup(GroupType.Block, Pos.at(0, 1)));
		assertSame(block0, su.getGroup(GroupType.Block, Pos.at(1, 0)));
		assertSame(block0, su.getGroup(GroupType.Block, Pos.at(1, 1)));
	}

	@Test
	void testToString() throws Exception {
		final Integer[] values = { //
				null, 3, 4, 2, //
				2, 4, null, 1, //
				3, 0, 2, 4, //
				4, 2, 1, 3 };
		assertEquals("Sudoku[[null, 3, 4, 2][2, 4, null, 1][3, null, 2, 4][4, 2, 1, 3]]",
				Sudoku.filled(values).toString());
	}
}
