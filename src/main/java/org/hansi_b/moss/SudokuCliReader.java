package org.hansi_b.moss;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.hansi_b.moss.testSupport.VisibleForTesting;

/**
 * Reads a Sudoku from the command line and just prints whether it is solved.
 *
 * The format is space-separated numbers for each row, where empty cells are
 * indicated by a zero.
 */
public class SudokuCliReader {

	private static void out(final String fmt, final Object... args) {
		System.out.println(String.format(fmt, args));
	}

	private static void err(final String fmt, final Object... args) {
		System.err.println(String.format(fmt, args));
	}

	private static Integer getCellVal(final String token, final int size) {
		final Integer val = Integer.parseInt(token);
		if (val < 0 || val > size)
			throw new IllegalArgumentException(
					String.format("Value must be between zero and %d, but is %d", size, val));
		return val > 0 ? val : null;
	}

	@VisibleForTesting
	static List<Integer> readValues(final InputStream inStream) {
		final List<Integer> values = new ArrayList<>();
		int size = -1;

		try (Scanner stdin = new Scanner(inStream)) {
			String line;
			int lineCount = 0;
			while (stdin.hasNextLine() && !(line = stdin.nextLine().trim()).isEmpty()) {

				final String[] tokens = line.split("\\s");
				if (size == -1) {
					size = tokens.length;
				} else if (tokens.length != size) {
					err("Line contains %d values, expected %d (ignoring this line).", tokens.length, size);
					continue;
				}

				try {
					final List<Integer> rowValues = Arrays.stream(tokens).map(t -> getCellVal(t, tokens.length))
							.collect(Collectors.toList());
					values.addAll(rowValues);
				} catch (final RuntimeException ex) {
					err("Encountered parse error for %s (ignoring this line): %s", Arrays.toString(tokens), ex);
				}

				if (++lineCount == size)
					break;
			}
		}
		return values;
	}

	public static void main(final String[] args) {
		out("Please enter your Sudoku, one row a line.");
		out("Separate cell values by whitespace; empty cells should be represented by zero.");

		final List<Integer> values = readValues(System.in);

		final Sudoku su = Sudoku.filled(values.toArray(new Integer[0]));
		out("The sudoku %s solved.", su.isSolved() ? "_is_" : "is _not_");
	}
}
