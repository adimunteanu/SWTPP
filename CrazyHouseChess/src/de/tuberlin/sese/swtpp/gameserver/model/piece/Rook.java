package de.tuberlin.sese.swtpp.gameserver.model.piece;

import de.tuberlin.sese.swtpp.gameserver.model.Field;
import de.tuberlin.sese.swtpp.gameserver.model.Piece;

public class Rook extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3598525745020510170L;

	public Rook(boolean isWhite) {
		super(PieceType.ROOK, isWhite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isMoveValid(Piece[][] board, String from, String to) {
		Field bTo = new Field(to), bFrom = new Field(from); 
		
		if(bTo.getRow() != bFrom.getRow() && bTo.getCol() != bFrom.getCol()) return false;
		if(!canCapture(board, from, to)) return false;
		
		int horizontal = bTo.getRow() == bFrom.getRow() ? 1 : 0;
		int steps = horizontal == 1 
				? Math.abs(bTo.getCol() - bFrom.getCol()) 
				: Math.abs(bTo.getRow() - bFrom.getRow());
		int colDir = bTo.getCol() > bFrom.getCol() ? 1 : -1;
		int rowDir = bTo.getRow() > bFrom.getRow() ? 1 : -1;
		
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
