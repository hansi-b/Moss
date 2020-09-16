package org.hansi_b.moss;

import spock.lang.Specification
import spock.lang.Unroll

public class CollectUtilsSortedSetComp extends Specification {

	def cmp = CollectUtils.sortedSetComparator()
	SortedSet<Integer> mockedSoSe = Mock()

	def "comparator shortcuts for argument identity" () {

		when:
		def r = cmp.compare(mockedSoSe, mockedSoSe)

		then:
		r == 0
		0 * mockedSoSe.iterator()
	}

	def "ordering for same lengths"() {

		expect:
		cmp.compare(s1 as SortedSet, s2 as SortedSet) == result

		where:
		s1 | s2 | result
		[1, 2]| [1, 2]| 0
		[1, 2]| [1, 3]| -1
		[2, 3]| [1, 3]| 1
	}

	def "ordering for different lengths"() {

		expect:
		cmp.compare(s1 as SortedSet, s2 as SortedSet) == result

		where:
		s1 | s2 | result
		[1,]| [1, 2]|  -1
		[1, 2]| [1,]| 1
		[2, 3]|[1, 2, 3]|  1
		[1, 2, 3]| [2, 3]| -1
	}
}
