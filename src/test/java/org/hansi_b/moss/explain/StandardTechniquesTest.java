package org.hansi_b.moss.explain;

import static org.hansi_b.moss.SudokuTest.filledSudoku;

import org.hansi_b.moss.Sudoku;
import org.junit.Test;

import java.util.List;

public class StandardTechniquesTest {

	final StandardTechniques technique = new StandardTechniques();

	@Test
	public void testFindBig() {

		/*
		 * from City magazine
		 */
		final Integer[] values = { //
				0, 0, 1, 0, 9, 1, 0, 6, 0, //
				0, 0, 3, 0, 7, 0, 0, 0, 0, //
				6, 2, 0, 0, 3, 0, 5, 0, 0, //
				//
				0, 0, 0, 7, 0, 0, 6, 0, 0, //
				4, 8, 6, 0, 0, 2, 7, 5, 0, //
				0, 0, 0, 0, 6, 0, 0, 4, 1, //
				//
				0, 0, 5, 6, 2, 0, 0, 0, 0, //
				1, 0, 0, 0, 8, 5, 0, 0, 6, //
				0, 0, 0, 0, 0, 7, 0, 8, 0 //
		};
		final Sudoku su = filledSudoku(values);
		for (List<Move> moves = technique.findMoves(su); !moves.isEmpty(); moves = technique.findMoves(su)) {
			final Move move = moves.get(0);
			System.out.println(move);
			move.getCell().setValue(move.getNewValue());
		}
		su.iterateEmptyCells().forEach(System.out::println);

		System.out.println(su.isSolved());
	}
}
