package de.tuberlin.sese.swtpp.gameserver.model.piece;

import de.tuberlin.sese.swtpp.gameserver.model.Field;
import de.tuberlin.sese.swtpp.gameserver.model.Piece;

public class King extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2248240883525933245L;

	public King(boolean isWhite) {
		super(PieceType.KING, isWhite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isMoveValid(Piece[][] board, String from, String to) {
		Field bTo = new Field(to), bFrom = new Field(from); 
		
		if(Math.abs(bTo.getCol() - bFrom.getCol()) > 1 || Math.abs(bTo.getRow() - bFrom.getRow()) > 1) return false;
		if(!canCapture(board, from, to)) return false;
		
		return true;
	}

	@Override
	public boolean canCapture(Piece[][] board, String from, String to) {
		Field bTo = new Field(to); 
		return board[bTo.getRow()][bTo.getCol()] == null ? true : board[bTo.getRow()][bTo.getCol()].getIsWhite() != isWhite;
	}

}
