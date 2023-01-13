package org.hansi_b.moss.explain.technique;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hansi_b.moss.Cell;
import org.hansib.sundries.CollectUtils;

class WingTriple {

	int commonCandidate;

	final Cell a;
	final Cell b;
	final Cell x;

	WingTriple(final Cell x, final int commonCandidate, final Cell a, final Cell b) {
		this.commonCandidate = commonCandidate;
		this.a = a;
		this.b = b;
		this.x = x;
	}

	Set<Cell> targetCells() {
		final Set<Cell> empties = Stream.of(a, b).map(e -> e.streamEmptyCellsFromGroups().collect(Collectors.toSet()))
				.reduce(CollectUtils::intersection).orElseThrow(IllegalStateException::new);
		empties.remove(x);
		return empties;
	}

	@Override
	public String toString() {
		return String.format("Wing on %s (%s -> %d): %s (%s), %s (%s)", x, x.getCandidates(), commonCandidate, a,
				a.getCandidates(), b, b.getCandidates());
	}
}