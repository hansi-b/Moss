package org.hansi_b.moss.explain;

import static org.hansi_b.moss.testSupport.Shortcuts.*;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy

class NakedPairSpec extends spock.lang.Specification {

	NakedPair technique = new NakedPair()

	def testSmallNakedPair() {

		when:
		final Integer[] values = //
				[1, 2, 0, 0]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);

		def actual = technique.findMoves(su, new PencilMarks()) as Set

		then:
		assert actual == [
			eliminate(Strategy.NakedPairInRow, [3, 4], cellsAt(su, [1, 2], [1, 3])),
			eliminate(Strategy.NakedPairInBlock, [3, 4], cellsAt(su, [1, 2], [1, 3])),
		] as Set
	}

	def testBigNakedPair() {

		when:
		// adapted from
		// http://sudoku.org.uk/solvingtechniques/nakedpairs.asp
		final Integer[] values = //
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 5, 0, 0, 0, 1, 4, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[6, 0, 0, 0, 0, 0, 0, 0, 0]+
				[9, 0, 0, 0, 0, 0, 0, 0, 0]+
				[5, 0, 0, 0, 0, 0, 0, 0, 0]+
				[7, 2, 0, 0, 8, 4, 0, 3, 9]+
				[0, 8, 0, 9, 3, 0, 6, 0, 0]+
				[0, 0, 0, 0, 7, 2, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);

		def actual = technique.findMoves(su, new PencilMarks()) as Set

		then:
		// row 7 & block 3/0: some occurrences of 1 + 4 can go (tied to 7/0 and 7/2)
		assert actual == [
			eliminate(Strategy.NakedPairInRow, 1, cellsAt(su, [7, 5], [7, 7])).with([cellAt(su,7,8)] as Set, [1, 4] as Set),
			eliminate(Strategy.NakedPairInBlock, [1, 4], cellsAt(su, [8, 0], [8, 1], [8, 2])).with([cellAt(su,6,2)] as Set, [1] as Set)
		] as Set
	}
}
