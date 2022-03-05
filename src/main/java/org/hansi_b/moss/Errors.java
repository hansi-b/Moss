package org.hansi_b.moss;

public class Errors {

	public static IllegalArgumentException illegalArg(final String fmt, final Object... args) {
		return new IllegalArgumentException(String.format(fmt, args));
	}

	public static IllegalStateException illegalState(final String fmt, final Object... args) {
		return new IllegalStateException(String.format(fmt, args));
	}
}