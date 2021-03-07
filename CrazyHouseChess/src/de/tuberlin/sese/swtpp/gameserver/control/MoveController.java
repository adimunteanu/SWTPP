package de.tuberlin.sese.swtpp.gameserver.control;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Field;
import de.tuberlin.sese.swtpp.gameserver.model.Piece;

public class MoveController {
	
	public enum MoveType {
		DROP, MOVE, INVALID;
	}
	
	private static boolean isFieldInBoard(String pos) {
		return pos.matches("[a-h]{1}[1-8]{1}");
	}
	
	public static boolean isMoveInBoard(String move) {
		if(!move.contains("-")) return false;
		String[] positions = move.split("-");
		return !positions[0].equals(positions[1]) 
				&& isFieldInBoard(positions[0]) 
				&& isFieldInBoard(positions[1]);
	}
	
	public static boolean isReserveMove(String move) {
		return move.matches("[KkQqBbNnRrPp]{1}[-]{1}[a-h]{1}[1-8]{1}");
	}
	
	public static MoveType getMoveType(String move) {
		return isMoveInBoard(move) 
				? MoveType.MOVE 
				: isReserveMove(move) 
				? MoveType.DROP 
				: MoveType.INVALID;
	}
	
	public static int isValidReserveMove(Piece[][] board, ArrayList<Piece> pocket, String move) {
		if(!isReserveMove(move)) return -1;
		String[] split = move.split("-");
		Field to = new Field(split[1]);
		
		if(board[to.getRow()][to.getCol()] != null) return -1;
		if(split[0].matches("[pP]{1}") 
				&& (to.getRow() == 0 
				|| to.getRow() == 7)) return -1;
		
		for(int i = 0; i < pocket.size(); i++) {
			if(Piece.getRepresentation(pocket.get(i)).equals(split[0])) {
				return i;
			}
		}
		
		return -1;
	}
}