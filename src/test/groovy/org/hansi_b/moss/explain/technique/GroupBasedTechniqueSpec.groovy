package org.hansi_b.moss.explain.technique;

import org.hansi_b.moss.explain.Strategy

import spock.lang.Specification

public class GroupBasedTechniqueSpec extends Specification {

	def "strategy requires three arguments"() {

		when:
		GroupBasedTechnique.groupTypeMapper(Strategy.NakedSingleInRow)

		then:
		thrown IllegalArgumentException
	}
}
