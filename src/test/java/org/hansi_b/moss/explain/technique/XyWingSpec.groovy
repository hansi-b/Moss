package org.hansi_b.moss.explain.technique

import static org.hansi_b.moss.testsupport.Shortcuts.*

import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Elimination
import org.hansi_b.moss.explain.Move
import org.hansi_b.moss.explain.PencilMarks

class XyWingSpec extends spock.lang.Specification {

	XyWing technique = new XyWing()

	def testXyWing() {

		given:
		final Integer[] values =
				[5, 2, 0, 0, 0, 0, 0, 4, 0]+
				[6, 3, 0, 4, 0, 0, 5, 0, 0]+
				[7, 1, 4, 6, 9, 5, 2, 3, 8]+
				[0, 5, 1, 0, 6, 0, 0, 0, 2]+
				[0, 8, 7, 1, 0, 0, 0, 5, 0]+
				[2, 4, 6, 5, 7, 9, 1, 8, 3]+
				[8, 6, 2, 7, 4, 3, 9, 1, 5]+
				[4, 9, 3, 2, 5, 1, 8, 6, 7]+
				[1, 7, 5, 9, 8, 6, 3, 2, 4]

		Sudoku su = Sudoku.filled(values)

		expect:
		technique.findMoves(su, new PencilMarks()).toList() == [
			new Elimination.Builder(Move.Strategy.XyWing).with(cellAt(su, 0, 6), 7).build()
		]
	}
}
