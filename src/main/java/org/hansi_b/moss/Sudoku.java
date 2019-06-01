package org.hansi_b.moss;

public class Sudoku {

	private static final int DEFAULT_SIZE = 9;

	private final int size;
	private final int[] fields;

	public Sudoku() {
		this(DEFAULT_SIZE);
	}

	public Sudoku(final int size) {
		final double sqrt = Math.sqrt(size);
		if (Math.floor(sqrt) != sqrt)
			throw new IllegalArgumentException(
					String.format("Sudoku cannot be initialised with a non-square size (got %d)", size));
		this.size = size;
		this.fields = new int[size * size];
	}

}
