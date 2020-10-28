package org.hansi_b.moss.explain

import static org.hansi_b.moss.testSupport.Shortcuts.move

import org.hansi_b.moss.Pos
import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Move.Strategy

import spock.lang.Specification

public class LockedCandidateBlockLineSpec extends Specification {

	def "can find for cand 1 in column 2 -> block 3"() {
		when:
		final Integer[] values = //
				[0, 0, 0, 1, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 1, 0, 0]+
				[2, 3, 4, 0, 0, 0, 0, 0, 0]+
				//
				[4, 5, 0, 0, 0, 0, 0, 0, 0]+
				[6, 7, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 9, 0, 0, 0, 0, 0]+
				//
				[3, 4, 5, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 1, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 1, 0]


		Sudoku su = Sudoku.filled(values)
		def actual = new LockedCandidateLineBlock().findMoves(su) as Set

		then:
		assert actual == [
			move(su, Strategy.LockedCandidateColBlock, 5, 0, 8)
		] as Set
	}
}
