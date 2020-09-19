package org.hansi_b.moss.explain;

import org.hansi_b.moss.Cell;

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

	@Override
	public String toString() {
		return String.format("Wing on %s (%s -> %d): %s (%s), %s (%s)", x, x.getCandidates(), commonCandidate, a,
				a.getCandidates(), b, b.getCandidates());
	}
}