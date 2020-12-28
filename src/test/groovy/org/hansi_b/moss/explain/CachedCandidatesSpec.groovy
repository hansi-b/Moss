package org.hansi_b.moss.explain

import org.hansi_b.moss.Cell
import org.hansi_b.moss.CellGroup.Type
import org.hansi_b.moss.Pos

import java.util.stream.Collectors
import static org.hansi_b.moss.testSupport.Shortcuts.*

import org.hansi_b.moss.Sudoku

import spock.lang.Specification

public class CachedCandidatesSpec extends Specification {


	def "getCellsByCandidate for empty row returns full matrix"() {

		given:
		Sudoku su = Sudoku.empty()
		def group = su.getGroup(Type.Row, 0)
		def cells = group.streamEmptyCells().collect(Collectors.toList()) as Set

		when:
		def result = new CachedCandidates().getCellsByCandidate(group)

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
		new CachedCandidates().getCellsByCandidate(su.getGroup(Type.Row, 0)).isEmpty()

		new CachedCandidates().getCellsByCandidate(su.getGroup(Type.Block, 0)) == [ 4 : [cellAt(su, 1, 0)] as Set ]
	}

	def "can remove candidate"() {

		given:
		final Integer[] values = //
				// first row contains a naked pair of 3,4
				[1, 2, 3, 4]+
				[0, 3, 0, 1]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values)
		def caca = new CachedCandidates()
		
		when:
		caca.remove(cellAt(su, 2, 1), 4)
		
		then:
		caca.candidates(cellAt(su, 2, 1)) == [1] as Set
		
		when:
		caca.remove(cellAt(su, 2, 1), 4)

		then:
		thrown IllegalArgumentException
	}
}
