package org.hansi_b.moss.explain

import static org.hansi_b.moss.testSupport.Shortcuts.move

import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Move.Strategy

import spock.lang.Specification

public class LockedCandidateBoxLineSpec extends Specification {

	def "can find simple example"() {
		when:
		final Integer[] values = //
				[0, 1, 2, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 3, 0]+
				[0, 5, 6, 0, 0, 0, 0, 0, 0]+
				//
				[0, 0, 1, 0, 0, 0, 0, 0, 0]+
				[9, 2, 7, 0, 0, 0, 0, 0, 0]+
				[8, 6, 5, 0, 0, 0, 0, 0, 0]+
				//
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]


		Sudoku su = Sudoku.filled(values)

		def actual = new LockedCandidateBoxLine().findMoves(su) as Set

		then:
		assert actual == [
			move(su, Strategy.LockedCandidateBoxLine, 3, 0, 4)
		] as Set
	}

	def "can find locked candidate from column"() {

		when:
		final Integer[] values = //
				[6, 0, 1, 9, 0, 2, 0, 0, 0]+
				[2, 8, 4, 0, 0, 0, 0, 6, 9]+
				[3, 0, 9, 0, 0, 6, 2, 0, 0]+
				[0, 2, 8, 3, 9, 0, 0, 1, 0]+
				[0, 1, 6, 8, 5, 0, 0, 2, 0]+
				[0, 3, 7, 0, 0, 1, 4, 0, 0]+
				[1, 4, 2, 0, 0, 0, 7, 0, 0]+
				[7, 6, 3, 0, 0, 0, 0, 5, 0]+
				[8, 9, 5, 0, 7, 3, 0, 4, 0]


		Sudoku su = Sudoku.filled(values)

		def actual = new LockedCandidateBoxLine().findMoves(su) as Set

		then:
		assert actual == [
			move(su, Strategy.LockedCandidateBoxLine, 6, 4, 6),
			move(su, Strategy.LockedCandidateBoxLine, 1, 5, 5)
		] as Set
	}
}
