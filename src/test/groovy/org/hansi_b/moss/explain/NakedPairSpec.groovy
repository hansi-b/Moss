package org.hansi_b.moss.explain;

import static org.hansi_b.moss.testSupport.Shortcuts.move;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy

class NakedPairSpec extends spock.lang.Specification {

	NakedPair technique = new NakedPair()

	def testSmallNakedPair() {

		when:
		final Integer[] values = //
				// first row contains a naked pair of 3,4
				[0, 2, 0, 0]+
				[0, 0, 0, 1]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);

		def actual = technique.findMoves(su) as Set

		then:
		assert actual == [
			move(su, Strategy.NakedPairInRow, 0, 0, 1),
			move(su, Strategy.NakedPairInRow, 1, 2, 2),
			move(su, Strategy.NakedPairInBlock, 0, 0, 1),
			move(su, Strategy.NakedPairInBlock, 1, 2, 2)
		] as Set
	}

	def testBigNakedPair() {

		when:
		// adapted from
		// http://sudoku.org.uk/solvingtechniques/nakedpairs.asp
		final Integer[] values = //
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[6, 0, 0, 0, 0, 0, 0, 0, 0]+
				[9, 0, 0, 0, 0, 0, 0, 0, 0]+
				[5, 0, 0, 0, 0, 0, 0, 0, 0]+
				[7, 2, 0, 0, 8, 4, 0, 3, 9]+
				[0, 8, 0, 9, 3, 6, 0, 5, 0]+
				[0, 0, 0, 0, 7, 2, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);

		def actual = technique.findMoves(su) as Set

		then:
		assert actual == [
			move(su, Strategy.NakedPairInBlock, 8, 0, 3)
		] as Set
	}
}
