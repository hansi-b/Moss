package org.hansi_b.moss.explain.technique;

import static org.hansi_b.moss.testsupport.Shortcuts.cellsAt

import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Elimination
import org.hansi_b.moss.explain.Move
import org.hansi_b.moss.explain.PencilMarks

import spock.lang.Specification

public class XWingSpec extends Specification {

	XWing technique = new XWing()

	// transcribed from https://www.learn-sudoku.com/x-wing.html
	def "can find X-Wing"() {

		given:
		final Integer[] values =
				[0, 0, 3, 8, 0, 0, 5, 1, 0]+
				[0, 0, 8, 7, 0, 0, 9, 3, 0]+
				[1, 0, 0, 3, 0, 5, 7, 2, 8]+
				//
				[0, 0, 0, 2, 0, 0, 8, 4, 9]+
				[8, 0, 1, 9, 0, 6, 2, 5, 7]+
				[0, 0, 0, 5, 0, 0, 1, 6, 3]+
				//
				[9, 6, 4, 1, 2, 7, 3, 8, 5]+
				[3, 8, 2, 6, 5, 9, 4, 7, 1]+
				[0, 1, 0, 4, 0, 0, 6, 9, 2]

		Sudoku su = Sudoku.filled(values)

		expect:
		technique.findMoves(su, new PencilMarks()).toList() == [
			new Elimination.Builder(Move.Strategy.XWingFromRow).with(cellsAt(su,
			[0, 1], [0, 4], [1, 1], [1, 4], [5, 1], [5, 4]) as List, [4]).build()
		]
	}
}
