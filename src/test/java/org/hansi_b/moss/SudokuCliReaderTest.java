package org.hansi_b.moss;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.list;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SudokuCliReaderTest {

	@Test
	public void testReadValues() throws Exception {
		try (final InputStream in = givenInput("1 2 3\n2 2 1")) {
			assertThat(SudokuCliReader.readValues(in)).isEqualTo(list(1, 2, 3, 2, 2, 1));
		}
	}

	private static ByteArrayInputStream givenInput(final String text) {
		return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
	}
}
