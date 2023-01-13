package org.hansi_b.moss.explain;

import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.technique.Technique;

public class Solver {

	private final Technique[] techniques;

	public Solver() {
		this(Technique.allTechniques());
	}

	public Solver(final Technique... techniques) {
		this.techniques = techniques;
	}

	/**
	 * Tries to solve the argument Sudoku
	 *
	 * @return a copy of the argument Sudoku, solved as far as possible
	 */
	public Sudoku solve(final Sudoku su) {
		Stepper stepper = new Stepper(su, techniques);

		while (stepper.hasNext())
			stepper.next();

		return stepper.getSudoku();
	}
}
