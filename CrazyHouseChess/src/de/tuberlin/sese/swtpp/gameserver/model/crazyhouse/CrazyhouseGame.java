package de.tuberlin.sese.swtpp.gameserver.model.crazyhouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import de.tuberlin.sese.swtpp.gameserver.control.MoveController;
import de.tuberlin.sese.swtpp.gameserver.control.MoveController.MoveType;
import de.tuberlin.sese.swtpp.gameserver.model.Field;
import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Piece;
import de.tuberlin.sese.swtpp.gameserver.model.Piece.PieceType;
import de.tuberlin.sese.swtpp.gameserver.model.piece.Queen;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class CrazyhouseGame extends Game implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 5424778147226994452L;

	/************************
	 * member
	 ***********************/

	// just for better comprehensibility of the code: assign white and black player
	private Player blackPlayer;
	private Player whitePlayer;

	// internal representation of the game state

	private Piece[][] gamePieces;
	private ArrayList<Piece>[] reservePieces;
	Field whiteKing, blackKing;

	/************************
	 * constructors
	 ***********************/

	public CrazyhouseGame() {
		super();

		setBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/");
	}

	public String getType() {
		return "crazyhouse";
	}

	/*******************************************
	 * Game class functions already implemented
	 ******************************************/

	@Override
	public boolean addPlayer(Player player) {
		if (!started) {
			players.add(player);

			// game starts with two players
			if (players.size() == 2) {
				started = true;
				this.whitePlayer = players.get(0);
				this.blackPlayer= players.get(1);
				nextPlayer = whitePlayer;
			}
			return true;
		}

		return false;
	}

	@Override
	public String getStatus() {
		if (error)
			return "Error";
		if (!started)
			return "Wait";
		if (!finished)
			return "Started";
		if (surrendered)
			return "Surrendered";
		if (draw)
			return "Draw";

		return "Finished";
	}

	@Override
	public String gameInfo() {
		String gameInfo = "";

		if (started) {
			if (blackGaveUp())
				gameInfo = "black gave up";
			else if (whiteGaveUp())
				gameInfo = "white gave up";
			else if (didWhiteDraw() && !didBlackDraw())
				gameInfo = "white called draw";
			else if (!didWhiteDraw() && didBlackDraw())
				gameInfo = "black called draw";
			else if (draw)
				gameInfo = "draw game";
			else if (finished)
				gameInfo = blackPlayer.isWinner() ? "black won" : "white won";
		}

		return gameInfo;
	}

	@Override
	public String nextPlayerString() {
		return isWhiteNext() ? "w" : "b";
	}

	@Override
	public int getMinPlayers() {
		return 2;
	}

	@Override
	public int getMaxPlayers() {
		return 2;
	}

	@Override
	public boolean callDraw(Player player) {

		// save to status: player wants to call draw
		if (this.started && !this.finished) {
			player.requestDraw();
		} else {
			return false;
		}

		// if both agreed on draw:
		// game is over
		if (players.stream().allMatch(Player::requestedDraw)) {
			this.draw = true;
			finish();
		}
		return true;
	}

	@Override
	public boolean giveUp(Player player) {
		if (started && !finished) {
			if (this.whitePlayer == player) {
				whitePlayer.surrender();
				blackPlayer.setWinner();
			}
			if (this.blackPlayer == player) {
				blackPlayer.surrender();
				whitePlayer.setWinner();
			}
			surrendered = true;
			finish();

			return true;
		}

		return false;
	}

	/* ******************************************
	 * Helpful stuff
	 ***************************************** */

	/**
	 *
	 * @return True if it's white player's turn
	 */
	public boolean isWhiteNext() {
		return nextPlayer == whitePlayer;
	}

	/**
	 * Ends game after regular move (save winner, finish up game state,
	 * histories...)
	 *
	 * @param winner player who won the game
	 * @return true if game was indeed finished
	 */
	public boolean regularGameEnd(Player winner) {
		// public for tests
		if (finish()) {
			winner.setWinner();
			winner.getUser().updateStatistics();
			return true;
		}
		return false;
	}

	public boolean didWhiteDraw() {
		return whitePlayer.requestedDraw();
	}

	public boolean didBlackDraw() {
		return blackPlayer.requestedDraw();
	}

	public boolean whiteGaveUp() {
		return whitePlayer.surrendered();
	}

	public boolean blackGaveUp() {
		return blackPlayer.surrendered();
	}

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 ******************************************/

	private void resetBoard() {
		this.gamePieces = new Piece[8][8];
		this.reservePieces = new ArrayList[2];
		this.reservePieces[0] = new ArrayList<Piece>();
		this.reservePieces[1] = new ArrayList<Piece>();
		this.whiteKing = new Field(7, 4);
		this.blackKing = new Field(0, 4);
	}
	
	private void updateKingPositions(int i, int j) {
		Piece currentPiece = gamePieces[i][j];
		if(currentPiece.getType() == PieceType.KING) {
			if(currentPiece.getIsWhite()) whiteKing = new Field(i, j);
			else blackKing = new Field(i, j);
		}
	}
	
	@Override
	public void setBoard(String state) {
		resetBoard();
		String[] rows = state.split("/", -1);
		
		for (int i = 0; i < rows.length - 1; i++) {
			int currentCol = 0;
			for (int j = 0; j < rows[i].length(); j++) {
				if (rows[i].charAt(j) <= '8') {
					currentCol += Integer.parseInt(String.valueOf(rows[i].charAt(j))) - 1;
				}else {
					gamePieces[i][currentCol] = Piece.fromRepresentation(String.valueOf(rows[i].charAt(j)));
					updateKingPositions(i, currentCol);
				}	
					
				currentCol++;
			}
		}
		
		for (int j = 0; j < rows[rows.length - 1].length(); j++) {
			Piece currentPiece = Piece.fromRepresentation(String.valueOf(rows[rows.length - 1].charAt(j)));
			reservePieces[currentPiece.getIsWhite() == true ? 0 : 1].add(currentPiece);
		}
	}
	
	private String sortReserves() {
		String sorted = "";		
		
		// Concat all reserves.
		for (int i = 0; i < reservePieces[0].size(); i++) 
		{
			sorted += Piece.getRepresentation(reservePieces[0].get(i));
		}
		
		for (int i = 0; i < reservePieces[1].size(); i++) 
		{
			sorted += Piece.getRepresentation(reservePieces[1].get(i));
		}
		
		// Sort alphabetically
		char[] chars = sorted.toCharArray();
		Arrays.sort(chars);
		sorted = new String(chars);
		
		return sorted;
	}
	
	// Returns true if last index is a sum of empty slots.
	private boolean shouldUniteEmptySlots(String board, int index) {
		if(board.charAt(index - 1) <= '7') {
			return true;
		}else {
			return false;
		}
	}
	
	private Piece getPieceFromBoardPosition(String pos) {
		Field bPos = new Field(pos); 
		return gamePieces[bPos.getRow()][bPos.getCol()];
	}

	@Override
	public String getBoard() {
		String board = "";
		
		for (int i = 0; i < gamePieces.length; i++) 
		{
			for (int j = 0; j < gamePieces.length; j++) 
			{
				// Unite empty fields if needed and skip.
				if(j > 0 && this.gamePieces[i][j] == null && shouldUniteEmptySlots(board, board.length())) {
					board = board.substring(0, board.length() - 1) + (Integer.parseInt(String.valueOf(board.charAt(board.length() - 1))) + 1);
					continue;
				}	
				
				// If field has a piece add it to the representation.
				if(this.gamePieces[i][j] != null) {
					board += Piece.getRepresentation(this.gamePieces[i][j]);
				}else { // Else add empty slot.
					board += "1";
				}
			}
			// Add row separation.
			board += '/';
		}
		
		// Concat sorted reserve pieces.
		return board + sortReserves();
	}
	
	private void doMove(String move, MoveType moveType, int reserveIndex, int playerIndex) {
		String[] positions = move.split("-");
		Field to = new Field(positions[1]); 

		if(moveType == MoveType.MOVE) {
			Field from = new Field(positions[0]); 
			
			Piece temp = gamePieces[from.getRow()][from.getCol()];
			gamePieces[from.getRow()][from.getCol()] = null;
			if(gamePieces[to.getRow()][to.getCol()] != null) {

				gamePieces[to.getRow()][to.getCol()].flipColor();
				reservePieces[playerIndex].add(gamePieces[to.getRow()][to.getCol()]);
			}
			gamePieces[to.getRow()][to.getCol()] = temp;
			promote(to, moveType);
			updateKingPositions(to.getRow(), to.getCol());
		}else { // MoveType.DROP
			gamePieces[to.getRow()][to.getCol()] = reservePieces[playerIndex].get(reserveIndex);
			reservePieces[playerIndex].remove(reserveIndex);
		}
	}
	
	private boolean promote(Field pos, MoveType moveType) {				
		if(gamePieces[pos.getRow()][pos.getCol()].getType() == PieceType.PAWN) {
			if(pos.getRow() == 0 || pos.getRow() == 7) {
				gamePieces[pos.getRow()][pos.getCol()] = new Queen(gamePieces[pos.getRow()][pos.getCol()].getIsWhite());
			}
		}
		
		return false;
	}
	

	@Override
	public boolean tryMove(String moveString, Player player) {
		String[] move = moveString.split("-");
		
		int playerIndex = isWhiteNext() ? 0 : 1;
		if(!isPlayersTurn(player)) return false;
		
		MoveType moveType = MoveController.getMoveType(moveString);
		if(moveType == MoveType.INVALID) return false;
		else if(moveType == MoveType.MOVE) {
			if(!getPieceFromBoardPosition(move[0]).isMoveValid(gamePieces, move[0], move[1])) return false;
		}else { // MoveType.DROP
			if(MoveController.isValidReserveMove(gamePieces, reservePieces[playerIndex], moveString) == -1) return false;
		}
		
		int reserveIndex = MoveController.isValidReserveMove(gamePieces, reservePieces[playerIndex], moveString);
		
		String boardBefore = getBoard();
		doMove(moveString, moveType, reserveIndex, playerIndex);
		if(inCheck(playerIndex)) {
			setBoard(boardBefore);
			return false;
		}
		if(isCheckmate(playerIndex, move[1])) {
			nextPlayer.setWinner();
			finish();
		}
		setBoard(getBoard());
		getHistory().add(new Move(moveString, boardBefore, nextPlayer));
		nextPlayer = isWhiteNext() ? blackPlayer : whitePlayer;
				
		return true;
	}
	
	private boolean inCheck(int playerIndex) {
		for (int i = 0; i < gamePieces.length; i++) {
			for (int j = 0; j < gamePieces.length; j++) {
				Piece currentPiece = gamePieces[i][j];
				if(currentPiece == null) continue;
				int currentPieceColor = currentPiece.getIsWhite() ? 0 : 1;
				if(currentPieceColor == playerIndex) continue;
				if(currentPiece.isMoveValid(gamePieces, new Field(i, j).toString(), (playerIndex == 0 ? whiteKing : blackKing).toString())) return true;
			}
		}
		
		return false;
	}
	
	private boolean isCheckmate(int playerIndex, String piecePos) {
		if(!inCheck(1 ^ playerIndex)) return false;
		String boardBefore = getBoard();
		for (int i = 0; i < gamePieces.length; i++) {
			for (int j = 0; j < gamePieces.length; j++) {
				Piece currentPiece = gamePieces[i][j];
				if(currentPiece == null) continue;
				int currentPieceColor = currentPiece.getIsWhite() ? 0 : 1;
				if(currentPieceColor == playerIndex) continue;
				
				if(isKingSavedByEating(currentPiece, new Field(i, j), piecePos, playerIndex, boardBefore)) return false;
				if(isKingSavedByMoving(currentPiece, new Field(i, j), playerIndex, boardBefore)) return false;
				if(isKingSavedByPocket(currentPiece, new Field(i, j), playerIndex, boardBefore)) return false;
			}
		}
		
		return true;
	}
	
	private boolean isKingSavedByEating(Piece currentPiece, Field pos, String piecePos, int playerIndex, String boardBefore) {
		return currentPiece.isMoveValid(gamePieces, pos.toString(), piecePos);
	}
	
	private boolean isKingSavedByMoving(Piece currentPiece, Field pos, int playerIndex, String boardBefore) {
		for(Field f : (playerIndex == 0 ? blackKing : whiteKing).getFieldSurroundings()) {
			String move = pos.toString() + "-" + f.toString();
			if(!currentPiece.isMoveValid(gamePieces, pos.toString(), f.toString())) continue;
			doMove(move, MoveType.MOVE, -1, 1 ^ playerIndex);
			boolean saved = !inCheck(1 ^ playerIndex);
			setBoard(boardBefore);
			if(saved) return true;
		}
		
		return false;
	}
	
	private boolean isKingSavedByPocket(Piece currentPiece, Field pos, int playerIndex, String boardBefore) {
		for(int i = 0; i < reservePieces[1 ^ playerIndex].size(); i++) {
			for(Field f : (playerIndex == 0 
					? blackKing 
					: whiteKing).getFieldSurroundings()) {
				String move = Piece.getRepresentation(reservePieces[1 ^ playerIndex].get(i)) + "-" + f.toString();
				int reserveId = MoveController.isValidReserveMove(gamePieces, reservePieces[1 ^ playerIndex], move);
				if(reserveId == -1) continue;
				doMove(move, MoveType.DROP, reserveId, 1 ^ playerIndex);
				boolean saved = !inCheck(1 ^ playerIndex);
				setBoard(boardBefore);
				if(saved) return true;
			}
		}
		
		return false;
	}

}
