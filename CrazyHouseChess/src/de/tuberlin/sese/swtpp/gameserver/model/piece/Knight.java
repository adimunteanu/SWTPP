package de.tuberlin.sese.swtpp.gameserver.model.piece;

import de.tuberlin.sese.swtpp.gameserver.model.Field;
import de.tuberlin.sese.swtpp.gameserver.model.Piece;

public class Knight extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8042772383296151683L;

	public Knight(boolean isWhite) {
		super(PieceType.KNIGHT, isWhite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isMoveValid(Piece[][] board, String from, String to) {
		Field bTo = new Field(to), bFrom = new Field(from); 
		
		int rowDiff = Math.abs(bTo.getRow() - bFrom.getRow());
		int colDiff = Math.abs(bTo.getCol() - bFrom.getCol());
		
		if(rowDiff < 1 || colDiff < 1) return false;
		if(rowDiff > 2 || colDiff > 2) return false;
		if(rowDiff + colDiff != 3) return false;
		if(!canCapture(board, from, to)) return false;
		
		return true;
	}
	
	@Override
	public boolean canCapture(Piece[][] board, String from, String to) {
		Field bTo = new Field(to); 
		return board[bTo.getRow()][bTo.getCol()] == null 
				? true 
				: board[bTo.getRow()][bTo.getCol()].getIsWhite() != isWhite;
	}

}
