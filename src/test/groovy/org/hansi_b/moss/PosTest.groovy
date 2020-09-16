package org.hansi_b.moss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import spock.lang.Specification

public class PosTest extends Specification {

	def testFlyweight() {

		expect:
		Pos.at(1, 7).is(Pos.at(1, 7))
		Pos.at(1, 7) != Pos.at(1,6)
	}

	def "comparator on same cell"() {

		expect:
		Pos.positionComparator.compare(Pos.at(2, 7), Pos.at(2, 7)) == 0
	}

	def "row takes precedence over column"() {

		expect:

		Pos.positionComparator.compare(Pos.at(2, 7), Pos.at(3, 5)) < 0
		Pos.positionComparator.compare(Pos.at(4, 5), Pos.at(2, 7)) > 0
	}

	def "comparator in same row"() {

		expect:

		Pos.positionComparator.compare(Pos.at(2, 7), Pos.at(3, 7)) < 0
		Pos.positionComparator.compare(Pos.at(4, 7), Pos.at(2, 7)) > 0
	}

	def "comparator in same column"() {

		expect:

		Pos.positionComparator.compare(Pos.at(3, 5), Pos.at(3, 7)) < 0
		Pos.positionComparator.compare(Pos.at(8, 7), Pos.at(8, 5)) > 0
	}
}
