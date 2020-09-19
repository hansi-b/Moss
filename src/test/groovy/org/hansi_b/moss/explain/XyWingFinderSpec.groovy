package org.hansi_b.moss.explain;

import static org.hansi_b.moss.testSupport.Shortcuts.*;

import org.hansi_b.moss.Sudoku

class XyWingFinderSpec extends spock.lang.Specification {

	def testXyWing() {

		when:
		final Integer[] values =
				[5, 2, 0, 0, 0, 0, 0, 4, 0]+
				[6, 3, 0, 4, 0, 0, 5, 0, 0]+
				[7, 1, 4, 6, 9, 5, 2, 3, 8]+
				[0, 5, 1, 0, 6, 0, 0, 0, 2]+
				[0, 8, 7, 1, 0, 0, 0, 5, 0]+
				[2, 4, 6, 5, 7, 9, 1, 8, 3]+
				[8, 6, 2, 7, 4, 3, 9, 1, 5]+
				[4, 9, 3, 2, 5, 1, 8, 6, 7]+
				[1, 7, 5, 9, 8, 6, 3, 2, 4]
		Sudoku su = Sudoku.filled(values);

		def wings = new XyWingFinder(su).findAllWings() as Set

		then:
		wings.size() == 1
		def w = wings.first()
		w.x == cellAt(su, 3,5)
		w.commonCandidate == 7
		[w.a, w.b] as Set == [
			cellAt(su,3,6),
			cellAt(su,0,5)] as Set
	}

	def "can create mapping in 4x4"() {
		given:
		Integer[] values =
				[1, 0, 2, 4]+
				[0, 0, 0, 0]+
				[3, 0, 0, 0]+
				[0, 0, 0, 0]
		Sudoku su = Sudoku.filled(values)
		when:
		XyWingFinder finder = new XyWingFinder(su)
		def wings = finder.findAllWings() as Set

		def mapping = finder.filterAndMapCellsByCandidates()

		then:
		/*-
		 * [
		 * 	1: [2: [Cell@(2, 3)],
		 * 		3: [Cell@(1, 2), Cell@(1, 3)],
		 * 		4: [Cell@(2, 2)]],
		 * 	2: [1: [Cell@(2, 3)],
		 * 		4: [Cell@(1, 0), Cell@(3, 0)]],
		 * 	3: [1: [Cell@(1, 2), Cell@(1, 3)]],
		 * 	4: [1: [Cell@(2, 2)],
		 * 		2: [Cell@(1, 0), Cell@(3, 0)]]]
		 */
		mapping.keySet() == [1, 2, 3, 4] as Set

		def first = mapping.get(1)
		first.keySet() == [2, 3, 4] as Set
		first.get(2) == [cellAt(su, 2, 3)] as Set
		first.get(3) == [
			cellAt(su, 1, 2),
			cellAt(su, 1, 3)] as Set
		first.get(4) == [cellAt(su, 2, 2)] as Set

		def second = mapping.get(2)
		second.keySet() == [1, 4] as Set
		second.get(1) == [cellAt(su, 2,3 )] as Set
		second.get(4) == [
			cellAt(su, 1, 0),
			cellAt(su, 3, 0)] as Set
	}
}
