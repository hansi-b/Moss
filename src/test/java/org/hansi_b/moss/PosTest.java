package org.hansi_b.moss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

import java.util.Comparator;

import org.junit.Test;

public class PosTest {

	@Test
	public void testFlyweight() throws Exception {

		final Pos p_1_7 = Pos.at(1, 7);

		assertSame(p_1_7, Pos.at(1, 7));
		assertNotEquals(p_1_7, Pos.at(1, 6));
	}

	@Test
	public void testComparator() throws Exception {

		final Comparator<Pos> pc = Pos.positionComparator;

		assertEquals(0, pc.compare(Pos.at(2, 7), Pos.at(2, 7)));
	}
}
