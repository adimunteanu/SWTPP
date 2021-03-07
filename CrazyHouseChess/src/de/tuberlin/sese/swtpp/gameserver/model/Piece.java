package de.tuberlin.sese.swtpp.gameserver.model;

import java.io.Serializable;

import de.tuberlin.sese.swtpp.gameserver.model.piece.*;

public class Piece implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2761229565440911411L;
	
	public enum PieceType {
		KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN, INVALID;
	}
	
	protected PieceType type;
	protected boolean isWhite;
	
	public Piece(PieceType type, boolean isWhite)
	{
		this.type = type;
		this.isWhite = isWhite;
	}
	
	public PieceType getType() {
		return type;
	}

	public boolean getIsWhite() {
		return isWhite;
	}
	
	public void flipColor() {
		this.isWhite = !this.isWhite;
	}
	
	public boolean isMoveValid(Piece[][] board, String from, String to) { return false; }  
	public boolean canCapture(Piece[][] board, String from, String to) { return false; }
	
	public static String getRepresentation(Piece piece)
	{
		String rep = "";
		
		switch(piece.type)
		{
			case KING: rep = "K"; break;
			case QUEEN: rep = "Q"; break;
			case BISHOP: rep = "B"; break;
			case KNIGHT: rep = "N"; break;
			case ROOK: rep = "R"; break;
			case PAWN: rep = "P"; break;
			default: rep = "";
		}
		
		return piece.isWhite ? rep : rep.toLowerCase();
	}

	public static Piece fromRepresentation(String representation)
	{
		if(representation.length() == 1 && representation.matches("[KkQqBbNnRrPp]")) {
			boolean isWhite = representation.charAt(0) < 'a';
			
			switch(representation.toLowerCase())
			{
				case "k": return new King(isWhite);
				case "q": return new Queen(isWhite);
				case "b": return new Bishop(isWhite);
				case "n": return new Knight(isWhite);
				case "r": return new Rook(isWhite);
				//case "p": return new Pawn(isWhite);
				default: return new Pawn(isWhite);
			}
		}else {
			return null;
		}
	}
}	