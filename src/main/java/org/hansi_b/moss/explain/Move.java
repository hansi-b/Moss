package org.hansi_b.moss.explain;

import org.hansi_b.moss.Cell;

public class Move {

	enum Strategy {
		SoleCandidateInRow, //
		SoleCandidateInCol, //
		SoleCandidateInBlock, //
	}

	private final Strategy strategy;
	private final Cell cell;
	private final Integer newValue;

	Move(final Strategy strategy, final Cell cell, final Integer newValue) {
		this.strategy = strategy;
		this.cell = cell;
		this.newValue = newValue;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public Cell getCell() {
		return cell;
	}

	public Integer getNewValue() {
		return newValue;
	}
}
