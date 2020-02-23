package org.hansi_b.moss

class SudokuSpockTest extends spock.lang.Specification{

	def "Creating an empty sudoku"() {

		when: "a default size Sudoku is created"
		def su = Sudoku.empty()

		then: "it has size of 9"
		su.size() == 9

		and:
		su.getValue(2, 3) == null
	}

	def "setting a cell value"() {

		given:
		def su = Sudoku.empty()

		when:
		su.set(2, 3, 5)

		then:
		su.getValue(2,3) == 5
	}
}
