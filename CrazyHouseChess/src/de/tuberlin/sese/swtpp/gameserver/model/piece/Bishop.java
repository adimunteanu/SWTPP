package de.tuberlin.sese.swtpp.gameserver.model.piece;

import de.tuberlin.sese.swtpp.gameserver.model.Field;
import de.tuberlin.sese.swtpp.gameserver.model.Piece;

public class Bishop extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3103241068920347627L;

	public Bishop(boolean isWhite) {
		super(PieceType.BISHOP, isWhite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isMoveValid(Piece[][] board, String from, String to) {
		Field bTo = new Field(to); 
		Field bFrom = new Field(from); 
		
		if(Math.abs(bTo.getRow() - bFrom.getRow()) != Math.abs(bTo.getCol() - bFrom.getCol())) return false;
		if(!canCapture(board, from, to)) return false;
		
		int colDir = bTo.getCol() > bFrom.getCol() ? 1 : -1;
		int rowDir = bTo.getRow() > bFrom.getRow() ? 1 : -1;
		int steps = Math.abs(bTo.getRow() - bFrom.getRow());
		for(int step = 1; step < steps; step++) {
			if(board[bFrom.getRow() + step * rowDir][bFrom.getCol() + step * colDir] != null) return false;
		} 
		
		return true;
	}
	
	@Override
	public boolean canCapture(Piece[][] board, String from, String to) {
		Field bTo = new Field(to); 
		return board[bTo.getRow()][bTo.getCol()] == null ? true : board[bTo.getRow()][bTo.getCol()].getIsWhite() != isWhite;
	}
}