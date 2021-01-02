package org.hansi_b.moss.explain

import static org.hansi_b.moss.testSupport.Shortcuts.*

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
				[2, 9, 0, 0, 0, 0, 0, 0, 0]+
				[5, 0, 8, 0, 0, 0, 0, 0, 0]+
				//
				[1, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]


		Sudoku su = Sudoku.filled(values)

		def actual = new LockedCandidates().findMoves(su, new PencilMarks()) as Set

		then:
		assert actual == [
			eliminate(Strategy.LockedCandidateBlockCol, 3, cellsAt(su, [3, 0], [7, 0], [8, 0]))
		] as Set
	}

	def "can find simple BlockRow example"() {
		when:
		final Integer[] values = //
				[0, 1, 2, 0, 0, 0, 4, 0, 6]+
				[0, 0, 0, 0, 6, 0, 0, 0, 5]+
				[0, 5, 6, 0, 0, 0, 0, 0, 0]+
				//
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				//
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[3, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]


		Sudoku su = Sudoku.filled(values)

		def actual = new LockedCandidates().findMoves(su, new PencilMarks()) as Set

		then:
		assert actual == [
			eliminate(Strategy.LockedCandidateBlockRow, 3, cellsAt(su, [1, 3], [1, 5], [1, 6], [1, 7]))
		] as Set
	}

	def "can find overlapping locked candidates"() {

		when:
		final Integer[] values = //
				[6, 0, 0, 9, 0, 2, 0, 0, 0]+
				[2, 8, 0, 0, 0, 0, 0, 6, 9]+
				[3, 0, 9, 0, 0, 6, 2, 0, 0]+

				[0, 2, 8, 3, 9, 0, 0, 1, 0]+
				[0, 1, 6, 8, 5, 0, 0, 2, 0]+
				[0, 3, 7, 0, 0, 1, 0, 0, 0]+

				[1, 4, 2, 0, 0, 0, 0, 0, 0]+
				[7, 6, 3, 0, 0, 0, 0, 5, 0]+
				[8, 9, 5, 0, 7, 3, 0, 0, 0]


		Sudoku su = Sudoku.filled(values)

		def actual = new LockedCandidates().findMoves(su, new PencilMarks()) as Set

		then:
		// seems to be correct, but double-check expectation if this ever fails
		assert actual == [
			eliminate(Strategy.LockedCandidateBlockRow, 6, cellsAt(su, [5, 6], [5, 8])),
			eliminate(Strategy.LockedCandidateRowBlock, 6, cellsAt(su, [5, 6], [5, 8])),
			eliminate(Strategy.LockedCandidateBlockCol, 7, cellsAt(su, [1, 5])),
			eliminate(Strategy.LockedCandidateColBlock, 7, cellsAt(su, [1, 5])),
			eliminate(Strategy.LockedCandidateBlockCol, 8, cellsAt(su, [6, 4], [7, 4])),
			eliminate(Strategy.LockedCandidateColBlock, 8, cellsAt(su, [6, 4], [7, 4])),
		] as Set
	}


	def "can find contradictory eliminiations for columns 0,1,2 -> block 3"() {
		when:
		final Integer[] values = //
				// ! NB ! this Sudoku is invalid
				[0, 0, 0, 1, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 1, 0, 0]+
				[2, 3, 4, 0, 0, 0, 0, 0, 0]+
				//
				[4, 5, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 7, 0, 9, 0, 0, 0, 0, 0]+
				//
				[3, 4, 5, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 1, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 1, 0]


		Sudoku su = Sudoku.filled(values)
		def actual = new LockedCandidates().findMoves(su, new PencilMarks()) as Set

		then:
		assert actual == [
			// col 0
			eliminate(Strategy.LockedCandidateColBlock, 1, cellsAt(su, [3, 2], [4, 1], [4, 2], [5, 2])),
			// col 1
			eliminate(Strategy.LockedCandidateColBlock, 1, cellsAt(su, [3, 2], [4, 0], [4, 2], [5, 0], [5, 2])),
			// col 2
			eliminate(Strategy.LockedCandidateColBlock, 1, cellsAt(su, [4, 0], [4, 1], [5, 0])),
		] as Set
	}
}
