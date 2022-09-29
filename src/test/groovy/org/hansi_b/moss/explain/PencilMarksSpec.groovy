package org.hansi_b.moss.explain

import static org.hansi_b.moss.testsupport.Shortcuts.*

import java.util.stream.Collectors

import org.hansi_b.moss.GroupType
import org.hansi_b.moss.Sudoku

import spock.lang.Specification

public class PencilMarksSpec extends Specification {


	def "getCellsByCandidate for empty row returns full matrix"() {

		given:
		Sudoku su = Sudoku.empty()
		def group = su.getGroup(GroupType.Row, 0)
		def cells = group.streamEmptyCells().collect(Collectors.toList()) as Set

		when:
		def result = new PencilMarks().getCellsByCandidate(group)

		then:
		result.size() == su.possibleValues().size()
		su.possibleValues().each { i ->
			result.get(i) == cells
		}
	}

	def "getCellsByCandidate does not contain filled-in values"() {

		when:
		final Integer[] values = //
				// first row contains a naked pair of 3,4
				[1, 2, 3, 4]+
				[0, 3, 0, 1]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values)

		then:
		new PencilMarks().getCellsByCandidate(su.getGroup(GroupType.Row, 0)).isEmpty()

		new PencilMarks().getCellsByCandidate(su.getGroup(GroupType.Block, 0)) == [ 4 : [cellAt(su, 1, 0)] as Set ]
	}

	def "getCandidatesByCell for empty row returns all values"() {

		given:
		Sudoku su = Sudoku.empty()
		def group = su.getGroup(GroupType.Row, 0)
		def cells = group.streamEmptyCells().collect(Collectors.toList()) as Set

		when:
		def result = new PencilMarks().getCandidatesByCell(group)

		then:
		result.size() == group.size()
		cells.each { i ->
			result.get(i) == su.possibleValues()
		}
	}

	def "getCandidatesByCell does not contain filled-in cells and values"() {

		when:
		final Integer[] values = //
				// first row contains a naked pair of 3,4
				[1, 2, 3, 4]+
				[0, 3, 0, 1]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values)

		then:
		new PencilMarks().getCandidatesByCell(su.getGroup(GroupType.Row, 0)).isEmpty()

		new PencilMarks().getCandidatesByCell(su.getGroup(GroupType.Block, 0)) == [ (cellAt(su, 1, 0)) : [4] as Set ]
		new PencilMarks().getCandidatesByCell(su.getGroup(GroupType.Row, 1)) == [
			(cellAt(su, 1, 0)) : [4] as Set,
			(cellAt(su, 1, 2)) : [2] as Set
		]
	}

	def "can remove candidate"() {

		given:
		final Integer[] values = //
				[1, 2, 3, 4]+
				[0, 3, 0, 1]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values)
		def marks = new PencilMarks()

		when:
		marks.remove(cellAt(su, 2, 1), 4)

		then:
		marks.candidates(cellAt(su, 2, 1)) == [1] as Set

		when:
		marks.remove(cellAt(su, 2, 1), 4)

		then:
		thrown IllegalArgumentException
	}

	def "update removes candidates"() {

		given:
		Sudoku su = Sudoku.empty()
		def marks = new PencilMarks()

		when:
		marks.updateByInsertion(cellAt(su, 1, 0), 4)

		then:
		marks.candidates(cellAt(su,1,0)).isEmpty()
		(0..su.size()-1).each{ col ->
			!marks.candidates(cellAt(su, 1, col)).contains(4)
		}
		(0..su.size()-1).each{ row ->
			!marks.candidates(cellAt(su, row, 0)).contains(4)
		}
	}
}
