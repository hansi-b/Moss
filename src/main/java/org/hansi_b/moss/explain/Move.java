package org.hansi_b.moss.explain;

import org.hansi_b.moss.Cell;

public class Move {

	private final Cell cell;
	private final Integer newValue;

	Move(final Cell cell, final Integer newValue) {
		this.cell = cell;
		this.newValue = newValue;
	}

	public Cell getCell() {
		return cell;
	}

	public Integer getNewValue() {
		return newValue;
	}
}
