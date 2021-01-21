package org.hansi_b.moss.explain;

import static org.hansi_b.moss.testSupport.Shortcuts.*

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy

class NakedTripleSpec extends spock.lang.Specification {

	NakedTriple technique = new NakedTriple()

	def "can find naked triple in block"() {

		given:
		final Integer[] values = //
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 2, 0, 0, 0, 0, 0, 0]+
				[0, 9, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]
		Sudoku su = Sudoku.filled(values);
		PencilMarks pm = new PencilMarks()
		marks(pm, cellAt(su, 0, 0), 1, 6)
		marks(pm, cellAt(su, 2, 0), 1, 4)
		marks(pm, cellAt(su, 2, 2), 1, 4, 6)

		when:

		def actual = technique.findMoves(su, pm).toSet()

		then:
		actual == [
			eliminate(Strategy.NakedTripleInBlock, [1, 4, 6], cellsAt(su, [0, 1], [0, 2], [1, 0], [1, 1]))
		] as Set
	}
}
