package de.tuberlin.sese.swtpp.gameserver.model.piece;

import de.tuberlin.sese.swtpp.gameserver.model.Field;
import de.tuberlin.sese.swtpp.gameserver.model.Piece;

public class Pawn extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5809946335854042362L;

	public Pawn(boolean isWhite) {
		super(PieceType.PAWN, isWhite);
	}

	@Override
	public boolean isMoveValid(Piece[][] board, String from, String to) {
		Field bTo = new Field(to), bFrom = new Field(from); 
		
		int secondRow = isWhite ? 6 : 1;
		int moveDir = isWhite ? 1 : -1;
		int steps = Math.abs(bFrom.getRow() - bTo.getRow());
		if(steps > 2) return false;
		
		boolean wrongDir = isWhite ? bTo.getRow() > bFrom.getRow() : bTo.getRow() < bFrom.getRow();
		if(wrongDir) return false;
		if(bTo.getCol() != bFrom.getCol()) return canCapture(board, from, to);
		if(steps == 1) 
			return board[bTo.getRow()][bTo.getCol()] == null;
		if(steps == 2) 
			return bFrom.getRow() == secondRow
			&& board[bTo.getRow()][bTo.getCol()] == null
			&& board[bTo.getRow() + moveDir][bTo.getCol()] == null;
		
		return true;
	}

	@Override
	public boolean canCapture(Piece[][] board, String from, String to) {
		Field bTo = new Field(to); 
		Field bFrom = new Field(from); 
		
		if(Math.abs(bTo.getRow() - bFrom.getRow()) != 1) return false;
		if(Math.abs(bTo.getCol() - bFrom.getCol()) != 1) return false;
		if(board[bTo.getRow()][bTo.getCol()] == null) return false;
		if(board[bTo.getRow()][bTo.getCol()].getIsWhite() == isWhite) return false;
		
		return true;
	}

}
