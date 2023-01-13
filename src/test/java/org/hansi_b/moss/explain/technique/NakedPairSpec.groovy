package org.hansi_b.moss.explain.technique

import static org.hansi_b.moss.testsupport.Shortcuts.*

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Elimination
import org.hansi_b.moss.explain.PencilMarks
import org.hansi_b.moss.explain.Strategy

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

		then:
		technique.findMoves(su, new PencilMarks()).toSet() == [
			eliminate(Strategy.NakedPairInRow, [3, 4], cellsAt(su, [1, 2], [1, 3])),
			eliminate(Strategy.NakedPairInBlock, [3, 4], cellsAt(su, [1, 2], [1, 3]))
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

		then:
		// row 7 & block 3/0: some occurrences of 1 + 4 can go (tied to 7/0 and 7/2)
		technique.findMoves(su, new PencilMarks()).toSet() == [
			new Elimination.Builder(Strategy.NakedPairInRow)
			.with(cellsAt(su, [7, 5], [7, 7]) as Set, 1).with([cellAt(su,7,8)], [1, 4]).build(),
			new Elimination.Builder(Strategy.NakedPairInBlock)
			.with(cellsAt(su, [8, 0], [8, 1], [8, 2]) as Set, [1, 4]).with([cellAt(su,6,2)], 1).build()
		] as Set
	}
}
