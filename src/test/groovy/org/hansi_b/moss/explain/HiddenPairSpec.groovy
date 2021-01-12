package org.hansi_b.moss.explain;

import static org.hansi_b.moss.testSupport.Shortcuts.*

import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Move.Strategy

import spock.lang.Specification

public class HiddenPairSpec extends Specification {

	HiddenPair technique = new HiddenPair()

	def "find simple hidden pair of 1,9 in upper left block"() {

		when:
		final Integer[] values = //
				[7, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 8, 4, 0, 0, 0, 0, 0, 0]+
				[0, 6, 0, 0, 0, 0, 0, 0, 0]+
				//
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				//
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);
		def pm = new PencilMarks()
		marks(pm, cellAt(su, 0, 1), 2, 3)
		marks(pm, cellAt(su, 0, 2), 2, 3, 5)
		marks(pm, cellAt(su, 1, 0), 1, 2, 5, 9)
		marks(pm, cellAt(su, 2, 0), 1, 5, 9)
		marks(pm, cellAt(su, 2, 2), 3, 5)

		def actual = technique.findMoves(su, pm) as Set

		then:
		actual == [
			new Elimination.Builder(Strategy.HiddenPairInBlock)
			.with(cellsAt(su, [1, 0]) as Set, [2, 5]).with(cellsAt(su, [2, 0]) as Set, [5]).build()
		] as Set
	}
}
