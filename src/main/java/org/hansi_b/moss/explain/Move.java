package org.hansi_b.moss.explain;

public sealed interface Move permits Insertion, Elimination {

	public Strategy strategy();

	public void apply(final PencilMarks marks);
}
