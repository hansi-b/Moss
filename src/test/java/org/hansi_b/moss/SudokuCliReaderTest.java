package org.hansi_b.moss;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class SudokuCliReaderTest {

	@Test
	void testReadValues() throws IOException {
		try (final InputStream in = givenInput("1 2 3\n2 2 1")) {
			assertThat(SudokuCliReader.readValues(in)).isEqualTo(list(1, 2, 3, 2, 2, 1));
		}
	}

	@Test
	void testSkipLinesOfWrongLength() throws IOException {
		try (final InputStream in = givenInput("1 2 3\n2 2\n1 1 1")) {
			assertThat(SudokuCliReader.readValues(in)).isEqualTo(list(1, 2, 3, 1, 1, 1));
		}
	}

	@Test
	void testSkipWrongValues() throws IOException {
		try (final InputStream in = givenInput("1 2 3\n7 2 2\n1 1 1")) {
			assertThat(SudokuCliReader.readValues(in)).isEqualTo(list(1, 2, 3, 1, 1, 1));
		}
	}

	private static ByteArrayInputStream givenInput(final String text) {
		return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
	}
}
