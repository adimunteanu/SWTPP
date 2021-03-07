package de.tuberlin.sese.swtpp.gameserver.model.piece;

import de.tuberlin.sese.swtpp.gameserver.model.Field;
import de.tuberlin.sese.swtpp.gameserver.model.Piece;

public class Queen extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5743098378802280542L;

	public Queen(boolean isWhite) {
		super(PieceType.QUEEN, isWhite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isMoveValid(Piece[][] board, String from, String to) {
		Field bTo = new Field(to), bFrom = new Field(from); 
		
		int colDir = bTo.getCol() > bFrom.getCol() ? 1 : -1;
		int rowDir = bTo.getRow() > bFrom.getRow() ? 1 : -1;
		int rowDiff = Math.abs(bTo.getRow() - bFrom.getRow());
		int colDiff = Math.abs(bTo.getCol() - bFrom.getCol());
		if(!canCapture(board, from, to)) return false;
		
		if(rowDiff == colDiff) {
			return bishopLogicValid(board, bFrom, bTo, colDir, rowDir);
		}else if(bTo.getRow() == bFrom.getRow() || bTo.getCol() == bFrom.getCol()) {
			return rookLogicValid(board, bFrom, bTo, colDir, rowDir);
		}else {
			return false;
		}
	}
	
	private boolean bishopLogicValid(Piece[][] board, Field bFrom, Field bTo, int colDir, int rowDir) {
		int steps = Math.abs(bTo.getRow() - bFrom.getRow());
		for(int step = 1; step < steps; step++) {
			if(board[bFrom.getRow() + step * rowDir][bFrom.getCol() + step * colDir] != null) return false;
		} 
		
		return true;
	}
	
	private boolean rookLogicValid(Piece[][] board, Field bFrom, Field bTo, int colDir, int rowDir) {
		int rowDiff = Math.abs(bTo.getRow() - bFrom.getRow());
		int colDiff = Math.abs(bTo.getCol() - bFrom.getCol());
		int horizontal = bTo.getRow() == bFrom.getRow() ? 1 : 0;
		int steps = horizontal == 1 ? colDiff : rowDiff;
		
		for(int step = 1; step < steps; step++) {
			if(board[bFrom.getRow() + step * rowDir * (1 ^ horizontal)][bFrom.getCol() + step * colDir * horizontal] != null) return false;
		} 
		
		return true;
	}

	@Override
	public boolean canCapture(Piece[][] board, String from, String to) {
		Field bTo = new Field(to); 
		return board[bTo.getRow()][bTo.getCol()] == null ? true : board[bTo.getRow()][bTo.getCol()].getIsWhite() != isWhite;
	}

}
