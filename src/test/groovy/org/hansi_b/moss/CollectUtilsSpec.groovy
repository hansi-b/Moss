package org.hansi_b.moss;

import static org.hansi_b.moss.testSupport.Shortcuts.cellAt

import java.util.function.BiFunction

import spock.lang.Specification
import spock.lang.Unroll

public class CollectUtilsSpec extends Specification {

	@Unroll def"get empty combinations for size #size"() {
		given:
		def x = CollectUtils.combinations([1, 2, 3] as SortedSet, size)

		expect:
		x == []

		where:
		size << [-1, 0]
	}

	def"get sole combination"() {

		expect:
		CollectUtils.combinations([1, 2, 3] as SortedSet, 3) == sortedTreeSets([[1, 2, 3]])
	}

	def"get combinations 1 from 3"() {

		expect:
		CollectUtils.combinations([1, 2, 3] as SortedSet, 1) == sortedTreeSets([[1], [2], [3]])
	}

	def"get more combinations"() {

		expect:
		CollectUtils.combinations([1, 2, 3, 4, 5] as SortedSet, 3) == sortedTreeSets([
			[1, 2, 3],
			[1, 2, 4],
			[1, 3, 4],
			[2, 3, 4],
			[1, 2, 5],
			[1, 3, 5],
			[2, 3, 5],
			[1, 4, 5],
			[2, 4, 5],
			[3, 4, 5]
		])
	}

	def"get combinations for custom comparator"() {

		given:
		Sudoku su = Sudoku.empty()
		def cells = Cell.newPosSortedSet();
		def c1 = cellAt(su, 1, 0)
		def c2 = cellAt(su, 1, 1)
		def c3 = cellAt(su, 2, 0)
		cells.addAll([c1, c2, c3])

		expect:

		def res = [[c1, c2], [c1, c3], [c2, c3]].collect {
			def s = Cell.newPosSortedSet()
			s.addAll(it)
			return s
		}

		CollectUtils.combinations(cells, 2) == res
	}

	def sortedTreeSets(final collOfColls) {
		collOfColls.collect { new TreeSet(it) }
	}

	def "pair combinations"() {
		expect:
		CollectUtils.pairCombinations(0) == []
		CollectUtils.pairCombinations(1) == []
		CollectUtils.pairCombinations(2) == [[0, 1]]
		CollectUtils.pairCombinations(3) == [[0, 1], [0, 2], [1, 2]]
		CollectUtils.pairCombinations(4) == [
			[0, 1],
			[0, 2],
			[0, 3],
			[1, 2],
			[1, 3],
			[2, 3]
		]
	}

	def "pair combinations from list"() {
		given:
		Sudoku su = Sudoku.empty()
		def c1 = cellAt(su, 1, 0)
		def c2 = cellAt(su, 1, 1)
		def c3 = cellAt(su, 2, 1)
		
		expect:
		CollectUtils.pairCombinations(List.of(c1, c2, c3)) == [[c1, c2],[c1, c3],[c2, c3]]
	}

	def "intersection for no sets"() {
		expect:
		CollectUtils.intersection() == Collections.emptySet()
	}

	def "intersection for single list is set"() {
		expect:
		CollectUtils.intersection([1, 1, 2]) == [1, 2] as Set
	}

	def "intersection for three collections"() {
		given:
		def a = 1 .. 4 as Set
		def b = [1, 2, 2]
		def c = [2, 3] as Set

		expect:
		CollectUtils.intersection(a, b, c) == [2] as Set
	}

	def "union for no sets"() {
		expect:
		CollectUtils.union() == Collections.emptySet()
	}

	def "union for single list is set"() {
		expect:
		CollectUtils.union([1, 1, 2]) == [1, 2] as Set
	}

	def "union for three collections"() {
		given:
		def a = 1 .. 4 as Set
		def b = [2, 1, 2]
		def c = [22, 3] as Set

		expect:
		CollectUtils.union(a, b, c) == [1, 2, 3, 4, 22] as Set
	}

	def "map sorted map to list"() {
		expect:
		def addit = { Integer a, Integer b -> a + b } as BiFunction
		CollectUtils.mapSortedMapToList([7: 2, 1: 2, 3 : 4] as TreeMap, addit) == [3, 7, 9]
	}
}
