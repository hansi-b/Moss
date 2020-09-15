package org.hansi_b.moss;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newTreeSet;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import spock.lang.Specification

public class CellSpec extends Specification {

	def "test iterator and identity"() {

		given:
		Sudoku su1 = Sudoku.empty()
		su1.set(0, 0, 2)
		Sudoku su2 = Sudoku.empty()
		su2.set(0, 0, 2)

		expect:
		su1.iterator().next() == cellAt(su1, 0, 0)
		su1.iterator().next() != su2.iterator().next()
	}

	def "test getCandidates at #x / #y"() {

		given:
		Integer[] values =
				[1, 0, 2, 4]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]
		Sudoku su = Sudoku.filled(values)

		expect:
		cellAt(su, x, y).getCandidates() == vals  as SortedSet

		where:
		x | y | vals
		0 | 0 | [3]
		0 | 1 | [3]
		1 | 0 | [2, 3, 4]
		2 | 1 | [1, 2, 3, 4]
	}

	def "test cell shares groups with itself"() {

		expect:
		Sudoku su = Sudoku.empty()
		cellAt(su, 0, 0).sharesGroups(cellAt(su, 0, 0))
	}

	def "test cell shares block"() {

		expect:
		Sudoku su = Sudoku.empty()
		cellAt(su, 0, 0).sharesGroups(cellAt(su, 1, 1))
	}

	def "test cell shares column"() {

		expect:
		Sudoku su = Sudoku.empty()
		cellAt(su, 0, 0).sharesGroups(cellAt(su, 0, 3))
	}

	def "test cell shares row"() {

		expect:
		Sudoku su = Sudoku.empty()
		cellAt(su, 0, 0).sharesGroups(cellAt(su, 3, 0))
	}

	def "cells without shared groups"() {

		expect:
		Sudoku su = Sudoku.empty()
		!cellAt(su, 0, 0).sharesGroups(cellAt(su, 5, 5))
	}

	def "same-pos cell shares no groups between different sudokus"() {

		expect:
		!cellAt(Sudoku.empty(), 0, 0).sharesGroups(cellAt(Sudoku.empty(), 0, 0))
	}

	static cellAt(su, x, y) {
		su.getCell(Pos.at(x, y))
	}
}
