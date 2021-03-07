package de.tuberlin.sese.swtpp.gameserver.test.crazyhouse;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.sese.swtpp.gameserver.control.GameController;
import de.tuberlin.sese.swtpp.gameserver.control.MoveController;
import de.tuberlin.sese.swtpp.gameserver.model.Field;
import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Piece;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;

public class TryMoveIntegrationTest {

	User user1 = new User("Alice", "alice");
	User user2 = new User("Bob", "bob");
	
	Player whitePlayer = null;
	Player blackPlayer = null;
	Game game = null;
	GameController controller;
	
	@Before
	public void setUp() throws Exception {
		controller = GameController.getInstance();
		controller.clear();
		
		int gameID = controller.startGame(user1, "", "crazyhouse");
		
		game =  controller.getGame(gameID);
		whitePlayer = game.getPlayer(user1);

	}
	
	public void startGame() {
		controller.joinGame(user2, "crazyhouse");		
		blackPlayer = game.getPlayer(user2);
	}
	
	public void startGame(String initialBoard, boolean whiteNext) {
		startGame();
		
		game.setBoard(initialBoard);
		game.setNextPlayer(whiteNext? whitePlayer:blackPlayer);
	}
	
	public void assertMove(String move, boolean white, boolean expectedResult) {
		if (white)
			assertEquals(expectedResult, game.tryMove(move, whitePlayer));
		else 
			assertEquals(expectedResult,game.tryMove(move, blackPlayer));
	}
	
	public void assertGameState(String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon) {
		String board = game.getBoard().replaceAll("e", "");
		
		assertEquals(expectedBoard,board);
		assertEquals(finished, game.isFinished());

		if (!game.isFinished()) {
			assertEquals(whiteNext, game.getNextPlayer() == whitePlayer);
		} else {
			assertEquals(whiteWon, whitePlayer.isWinner());
			assertEquals(!whiteWon, blackPlayer.isWinner());
		}
	}
	

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 *******************************************/
	
	@Test
	public void exampleTest() {
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b2-b7",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	}
	
	@Test
	public void testGame1() {
		startGame("rnbqkbnr/ppppp3/7p/5p2/3PP2p/8/PPP2PPP/RN1QKBNR/b",true);
		assertMove("d1-h5",true,true);
		assertGameState("rnbqkbnr/ppppp3/7p/5p1Q/3PP2p/8/PPP2PPP/RN2KBNR/b",false,false,false);
		assertMove("b-f7",false,true);
		assertGameState("rnbqkbnr/pppppb2/7p/5p1Q/3PP2p/8/PPP2PPP/RN2KBNR/",true,false,false);
		assertMove("a2-a3",true,true);
		assertGameState("rnbqkbnr/pppppb2/7p/5p1Q/3PP2p/P7/1PP2PPP/RN2KBNR/",false,false,false);
		assertMove("f7-g6",false,true);
		assertGameState("rnbqkbnr/ppppp3/6bp/5p1Q/3PP2p/P7/1PP2PPP/RN2KBNR/",true,false,false);
		assertMove("h5-g6",true,true);
		assertGameState("rnbqkbnr/ppppp3/6Qp/5p2/3PP2p/P7/1PP2PPP/RN2KBNR/B",false,true,true);
	}
	
	@Test
	public void testPawn() {
		startGame("7k/5p2/1p3P2/1p2P3/2P1p3/1P3p2/PPP2P2/7K/",true);
		assertMove("b2-b4", true, false);
		assertMove("c2-c4", true, false);
		assertMove("a2-a1", true, false);
		assertMove("e5-d6", true, false);
		assertMove("e5-f6", true, false);
		assertMove("a2-a4", true, true);
		
		assertMove("b6-b5", false, false);
		assertMove("b6-b4", false, false);
		assertMove("b5-b4", false, true);
		
		assertMove("e5-e6", true, true);
		assertMove("e4-e3", false, true);
		
		assertMove("e6-f7", true, true);
		assertMove("e3-f2", false, true);
	}
	
	@Test
	public void testKnight() {
		startGame("7k/8/4p3/8/3N4/8/8/7K/",true);
		assertMove("d4-e6", true, true);
		assertMove("h8-h7", false, true);
		assertMove("e6-d4", true, true);
	}
	
	@Test
	public void testBishop() {
		startGame("7k/2b5/8/8/4P3/8/2B5/7K/",true);
		assertMove("c2-d3", true, true);
		assertMove("c7-a5", false, true);
		assertMove("d3-f5", true, false);
	}
	
	@Test
	public void testRook() {
		startGame("7k/1r6/8/8/8/8/1RP5/7K/",true);
		assertMove("b2-a1", true, false);
		assertMove("b2-e2", true, false);
		assertMove("b2-a2", true, true);
		assertMove("b7-b5", false, true);
	}
	
	@Test
	public void testQueen() {
		startGame("7k/1q6/8/8/8/8/1QP5/6K1/",true);
		assertMove("b2-e2", true, false);
		assertMove("b2-a2", true, true);
		assertMove("b7-b5", false, true);
	}
	
	@Test
	public void testMoveController() {
		MoveController mc = new MoveController();
		startGame("1nbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/1NBQKBNR/p",true);
		// isMoveInBoard
		assertMove("a2-a2", true, false);
		assertMove("t2-a3", true, false);
		assertMove("a2-t3", true, false);
		assertMove("a2-a3", true, true);
		// isValidReserveMove
		assertMove("p-a1", false, false);
		assertMove("p-a8", false, false);
		assertMove("q-a5", false, false);
		assertMove("p-a5", false, true);
	}
	
	@Test
	public void testPiece() {
		// getRepresentation
		Piece.getRepresentation(new Piece(Piece.PieceType.INVALID, false));
		// fromRepresentation
		Piece.fromRepresentation("aa");
		Piece.fromRepresentation("a");
		// isMoveValid
		Piece p = new Piece(Piece.PieceType.BISHOP, false);
		p.isMoveValid(new Piece[][] {}, "", "");
		p.canCapture(new Piece[][] {}, "", "");
	}
	
	@Test
	public void testField() {
		Field a = new Field("a1");
		a.getFieldSurroundings();
		Field b = new Field("a8");
		b.getFieldSurroundings();
		Field c = new Field("h1");
		c.getFieldSurroundings();
		Field d = new Field("h8");
		d.getFieldSurroundings();
	}
	
	@Test
	public void testInvalidMove() {
		startGame("1nbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/1NBQKBNR/P",true);
		assertMove("aaaa", true, false);
	}
	
	@Test
	public void testPromotion() {
		startGame("8/P7/8/2k5/5K2/8/p7/8/",true);
		assertMove("a7-a8", true, true);
		assertMove("a2-a1", false, true);
	}
	
	@Test
	public void testSavedByPocket() {
		// Succeed
		startGame("k7/pp4Q1/8/8/8/8/q5PP/7K/Nn",true);
		assertMove("g7-g8", true, true);
		assertMove("n-b8", false, true);
		assertMove("g8-f8", true, true);
		assertMove("a2-a1", false, true);
		assertMove("N-g1", true, true);
		// Fail
		startGame("3k3Q/Q1ppp3/8/8/8/8/8/7K/n", true);
		assertMove("a7-a8", true, true);
	}
	
	@Test
	public void testSavedByMoving() {
		// Succeed
		startGame("k7/6Q1/8/8/8/8/q7/7K/",true);
		assertMove("g7-g8", true, true);
		assertMove("a8-a7", false, true);
		assertMove("g8-f8", true, true);
		assertMove("a2-a1", false, true);
		assertMove("h1-h2", true, true);
		// Fail happens in testSavedByPocket()
	}
	
	@Test
	public void testSavedByEating() {
		// Succeed
		startGame("k7/n1Q5/8/8/8/8/q7/7K/",true);
		assertMove("c7-c8", true, true);
		assertMove("a7-c8", false, true);
		// Fail happens in testSavedByPocket()
	}
	
	@Test
	public void testTryMove() {
		startGame("k7/2Q5/8/8/8/8/q7/7K/",true);
		assertMove("a8-a7", false, false);
		assertMove("h1-h2", true, false);
	}
}
