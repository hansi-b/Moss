package org.hansi_b.moss;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.hansi_b.moss.CellGroup.Block;
import org.junit.Test;

public class CellTest {

	@Test
	public void testCompare() throws Exception {

		final Sudoku su = new Sudoku(9);
		final Block block = su.getBlock(2);
		final List<Cell> cells = Lists.newArrayList(block);
		final Cell first = cells.get(0);
		final Cell last = cells.get(cells.size() - 1);

		for (int i = 1; i < cells.size() - 2; i++) {
			final Cell curr = cells.get(i);

			assertThat(Cell.positionComparator.compare(curr, curr)).isEqualTo(0);

			assertThat(Cell.positionComparator.compare(curr, first)).isGreaterThan(0);
			assertThat(Cell.positionComparator.compare(first, curr)).isLessThan(0);
			assertThat(Cell.positionComparator.compare(curr, last)).isLessThan(0);
			assertThat(Cell.positionComparator.compare(last, first)).isGreaterThan(0);
		}
	}

}
