package org.hansi_b.moss.explain

import static org.hansi_b.moss.testSupport.Shortcuts.move

import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Move.Strategy

import spock.lang.Specification

public class LockedCandidatesSpec extends Specification {

	def "can find simple BlockCol example"() {
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

		def actual = new LockedCandidates().findMoves(su) as Set

		then:
		assert actual == [
			move(su, Strategy.LockedCandidateBlockCol, 3, 0, 4)
		] as Set
	}

	def "can find simple BlockRow example"() {
		when:
		final Integer[] values = //
				[0, 1, 2, 0, 0, 0, 4, 5, 6]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 5, 6, 0, 0, 0, 0, 0, 0]+
				//
				[0, 0, 0, 0, 0, 0, 8, 3, 0]+
				[0, 0, 0, 0, 0, 0, 7, 0, 0]+
				[0, 0, 0, 0, 0, 0, 2, 0, 0]+
				//
				[0, 0, 0, 0, 0, 0, 1, 0, 0]+
				[3, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]


		Sudoku su = Sudoku.filled(values)

		def actual = new LockedCandidates().findMoves(su) as Set

		then:
		assert actual == [
			move(su, Strategy.LockedCandidateBlockRow, 1, 6, 9)
		] as Set
	}

	def "can find overlapping locked candidates"() {

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

		def actual = new LockedCandidates().findMoves(su) as Set

		then:
		assert actual == [
			move(su, Strategy.LockedCandidateBlockCol, 6, 4, 6),
			move(su, Strategy.LockedCandidateBlockCol, 1, 5, 5),
			move(su, Strategy.LockedCandidateColBlock, 6, 4, 6),
			move(su, Strategy.LockedCandidateColBlock, 1, 5, 5)
		] as Set
	}


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
		def actual = new LockedCandidates().findMoves(su) as Set

		then:
		assert actual == [
			move(su, Strategy.LockedCandidateColBlock, 5, 0, 8)
		] as Set
	}
}
