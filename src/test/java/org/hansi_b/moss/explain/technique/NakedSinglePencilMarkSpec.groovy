package org.hansi_b.moss.explain.technique;

import static org.hansi_b.moss.testsupport.Shortcuts.*

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.PencilMarks
import org.hansi_b.moss.explain.Strategy

class NakedSinglePencilMarkSpec extends spock.lang.Specification {

	NakedSinglePencilMark technique = new NakedSinglePencilMark()

	def "finds NakedSingle with fresh PencilMarks"() {

		when:
		Integer[] values =
				[1, 0, 0, 0]+
				[0, 0, 3, 0]+
				[0, 2, 0, 0]+
				[0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);

		then:
		technique.findMoves(su, new PencilMarks()).toList() == [
			insert(su, Strategy.NakedSinglePencilMark, 1, 1, 4)
		]
	}

	def "finds only after elimination"() {

		given:
		Integer[] values =
				[1, 2, 0, 0]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);

		when:
		def pm = new PencilMarks()
		then:
		technique.findMoves(su, pm).toList() == []

		when:
		pm.remove(cellAt(su, 1, 1), 3)
		then:
		technique.findMoves(su, pm).toList() == [
			insert(su, Strategy.NakedSinglePencilMark, 1, 1, 4)
		]
	}
}
