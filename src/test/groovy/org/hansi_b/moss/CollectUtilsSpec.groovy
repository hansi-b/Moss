package org.hansi_b.moss;

import spock.lang.Specification
import spock.lang.Unroll

public class CollectUtilsSpec extends Specification {

	@Unroll
	def "get empty pair indices for size #size" () {
		given:
		def x = CollectUtils.getPairCombinations(size)

		expect:
		x == []

		where:
		size << [-1, 0, 1]
	}

	def "get sole pair for size 2" () {
		given:
		def x = CollectUtils.getPairCombinations(2)

		expect:
		x == [[0, 1]]
	}

	def "get index pairs for some bigger size" () {
		given:
		def x = CollectUtils.getPairCombinations(4)

		expect:
		x == [
			[0, 1],
			[0, 2],
			[0, 3],
			[1, 2],
			[1, 3],
			[2, 3]
		]
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
}
