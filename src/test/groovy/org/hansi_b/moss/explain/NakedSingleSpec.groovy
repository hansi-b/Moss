package org.hansi_b.moss.explain;

import static org.hansi_b.moss.testsupport.Shortcuts.insert

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy

class NakedSingleSpec extends spock.lang.Specification {

	NakedSingle technique = new NakedSingle()

	def testFindsTrivialSoleInRow() {

		when:
		Integer[] values =
				[1, 0, 2, 4]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);

		then:
		technique.findMoves(su, new PencilMarks()).toList() == [
			insert(su, Strategy.NakedSingle, 0, 1, 3)
		]
	}

	def testFindsSingleSoleFromCombination() {

		when:
		Integer[] values =
				[1, 0, 0, 0]+
				[0, 0, 3, 0]+
				[0, 2, 0, 0]+
				[0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);

		then:
		technique.findMoves(su, new PencilMarks()).toList() == [
			insert(su, Strategy.NakedSingle, 1, 1, 4)
		]
	}

	def testFindsMultipleFromCombinations() {

		when:
		Integer[] values =
				[1, 0, 0, 0]+
				[0, 0, 3, 0]+
				[4, 2, 0, 0]+
				[0, 0, 0, 0]
		Sudoku su = Sudoku.filled(values);

		then:

		technique.findMoves(su, new PencilMarks()).toSet() == [
			insert(su, Strategy.NakedSingle, 1, 0, 2),
			insert(su, Strategy.NakedSingle, 1, 1, 4),
			insert(su, Strategy.NakedSingle, 2, 2, 1),
			insert(su, Strategy.NakedSingle, 3, 0, 3)] as Set
	}

	def testFindBig() {

		/*
		 * from http://www.sudoku-space.de/sudoku-loesungstechniken/
		 */
		when:
		Integer[] values =
				[0, 0, 0, 0, 0, 1, 0, 0, 0]+
				[0, 0, 0, 0, 0, 5, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				//
				[0, 0, 0, 8, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 2, 0, 0, 0, 0]+
				[0, 4, 0, 6, 0, 0, 7, 0, 0]+
				//
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 9, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);

		then:
		technique.findMoves(su, new PencilMarks()).toList() == [
			insert(su, Strategy.NakedSingle, 5, 5, 3)
		]
	}
}
