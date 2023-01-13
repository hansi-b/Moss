package org.hansi_b.moss.explain;

public enum Strategy {
	NakedSingleInRow, //
	NakedSingleInCol, //
	NakedSingleInBlock, //
	NakedSingle, //
	NakedSinglePencilMark, //
	HiddenSingleInRow, //
	HiddenSingleInCol, //
	HiddenSingleInBlock, //
	NakedPairInRow, NakedPairInCol, NakedPairInBlock, //
	NakedTripleInRow, NakedTripleInCol, NakedTripleInBlock, //
	HiddenPairInRow, //
	HiddenPairInCol, //
	HiddenPairInBlock, //
	LockedCandidateBlockRow, //
	LockedCandidateBlockCol, //
	LockedCandidateRowBlock, //
	LockedCandidateColBlock, //
	XWingFromRow, //
	XWingFromCol, //
	XyWing //
}