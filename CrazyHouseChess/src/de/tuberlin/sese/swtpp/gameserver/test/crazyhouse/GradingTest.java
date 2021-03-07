package de.tuberlin.sese.swtpp.gameserver.test.crazyhouse;


import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class GradingTest extends TryMoveTestVorgabe{
	
	private static int illegalMoves;
	private static int regularMoves;
	private static int checkMoves;
	private static int captureAndDropMoves;
	private static int gameStartupFinish;
	
	private static int illegalMovesTotal;
	private static int regularMovesTotal;
	private static int checkMovesTotal;
	private static int captureAndDropMovesTotal;	
	private static int gameStartupFinishTotal;
	

	private static StringBuilder outputBuilder;

	@Rule
	public TestWatcher watcher = new TestWatcher () {
		    @Override
		    protected void starting(Description description) {
		    	outputBuilder.append("Test case " + description.getMethodName() + " ... "); 
		    }
			
		    @Override
		    protected void failed(Throwable e, Description description) {
		    	outputBuilder.append("FAILED\n");
		    }
		    
		    @Override
		    protected void succeeded(Description description) {
		    	outputBuilder.append("OK\n");
		    }
	};
	
	@BeforeClass
	public static void init() {
		illegalMoves=0;
		regularMoves=0;
		checkMoves=0;
		captureAndDropMoves =0;
		gameStartupFinish=0;
		
		illegalMovesTotal=0;
		regularMovesTotal=0;
		checkMovesTotal=0;
		captureAndDropMovesTotal =0;
		gameStartupFinishTotal=0;
		
		outputBuilder = new StringBuilder();
	}
	
	@AfterClass
	public static void print() {
		double points = 0;
		double pointsTotal = 5;
		
		System.out.println(outputBuilder.toString());
		points += ((double) illegalMoves) / illegalMovesTotal;
		points += ((double) regularMoves) / regularMovesTotal;
		points += ((double) captureAndDropMoves) / captureAndDropMovesTotal;
		points += ((double) checkMoves) / checkMovesTotal;
		points += ((double) gameStartupFinish) / gameStartupFinishTotal;
		System.out.println("----------------------------------------------------------");
		System.out.println("Checks for illegal moves: " + illegalMoves + "/" + illegalMovesTotal);
		System.out.println("Performing regular moves: " + regularMoves + "/" + regularMovesTotal);
		System.out.println("Performing moves to capture or drop figure: " + captureAndDropMoves + "/" + captureAndDropMovesTotal);
		System.out.println("Performing moves in or into check: " + checkMoves + "/" + checkMovesTotal);
		System.out.println("Init board and game finish: " + gameStartupFinish + "/" + gameStartupFinishTotal);
		System.out.println("----------------------------------------------------------");
		DecimalFormat df = new DecimalFormat("#.##");
		DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
		sym.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(sym);
		df.setRoundingMode(RoundingMode.CEILING);
		df.setMinimumFractionDigits(1);
        if(Double.isNaN(points)) points = 0;
		System.out.println("Percent solved (implementation): " + df.format(points) + "/" + (int)pointsTotal);
	}
	
	@Test
	public void pawnTooFar_White() {
		illegalMovesTotal++;
	    startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
	    assertMove("c2-c5",true,false);
	    assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void pawnDiagonal_White() {
		illegalMovesTotal++;
	    startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
	    assertMove("c2-b3",true,false);
	    assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void rookThroughPawn_White() {
		illegalMovesTotal++;
	    startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
	    assertMove("a1-a5",true,false);
	    assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void bishopAttackOwnPlayer_White() {
		illegalMovesTotal++;
	    startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
	    assertMove("c1-b2",true,false);
	    assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void pawnLongJump_White() {
		regularMovesTotal++;
	    startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
	    assertMove("d2-d4",true,true);
	    assertGameState("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",false,false,false);
	    regularMoves++;
	}
	
	@Test
	public void pawnTooFar_Black() {
		illegalMovesTotal++;
	    startGame("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",false);
	    assertMove("e7-e4",false,false);
	    assertGameState("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",false,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void pawnDiagonal_Black() {
		illegalMovesTotal++;
	    startGame("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",false);
	    assertMove("d7-c6",false,false);
	    assertGameState("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",false,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void rookHitOwnFigure_Black() {
		illegalMovesTotal++;
	    startGame("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",false);
	    assertMove("h8-h7",false,false);
	    assertGameState("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",false,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void bishopThroughOwnFigure_Black() {
		illegalMovesTotal++;
	    startGame("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",false);
	    assertMove("f8-a3",false,false);
	    assertGameState("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",false,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void pawnMoveRegular_Black() {
		regularMovesTotal++;
	    startGame("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR/",false);
	    assertMove("e7-e6",false,true);
	    assertGameState("rnbqkbnr/pppp1ppp/4p3/8/3P4/8/PPP1PPPP/RNBQKBNR/",true,false,false);
	    regularMoves++;
	}
	
	@Test
	public void pawnLongJumpWrongPosition_White() {
		illegalMovesTotal++;
	    startGame("rnbqkbnr/pppp1ppp/4p3/8/3P4/8/PPP1PPPP/RNBQKBNR/",true);
	    assertMove("d4-d6",true,false);
	    assertGameState("rnbqkbnr/pppp1ppp/4p3/8/3P4/8/PPP1PPPP/RNBQKBNR/",true,false,false);
	    illegalMoves++;
	}

	@Test
	public void bishopRegular_White() {
		regularMovesTotal++;
	    startGame("rnbqkbnr/pppp1ppp/4p3/8/3P4/8/PPP1PPPP/RNBQKBNR/",true);
	    assertMove("c1-g5",true,true);
	    assertGameState("rnbqkbnr/pppp1ppp/4p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",false,false,false);
	    regularMoves++;
	}
	
	@Test
	public void pawnWrongDirection_Black() {
		illegalMovesTotal++;
	    startGame("rnbqkbnr/pppp1ppp/4p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",false);
	    assertMove("e6-e7",false,false);
	    assertGameState("rnbqkbnr/pppp1ppp/4p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",false,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void knightRegular_Black() {
		regularMovesTotal++;
	    startGame("rnbqkbnr/pppp1ppp/4p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",false);
	    assertMove("b8-c6",false,true);
	    assertGameState("r1bqkbnr/pppp1ppp/2n1p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",true,false,false);
	    regularMoves++;
	}
	
	@Test
	public void queenNotStraight_White() {
		illegalMovesTotal++;
	    startGame("r1bqkbnr/pppp1ppp/2n1p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",true);
	    assertMove("d1-b4",true,false);
	    assertGameState("r1bqkbnr/pppp1ppp/2n1p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",true,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void bishopHorizontal_White() {
		illegalMovesTotal++;
	    startGame("r1bqkbnr/pppp1ppp/2n1p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",true);
	    assertMove("g5-g3",true,false);
	    assertGameState("r1bqkbnr/pppp1ppp/2n1p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",true,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void kingTooFar_White() {
		illegalMovesTotal++;
	    startGame("r1bqkbnr/pppp1ppp/2n1p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",true);
	    assertMove("e1-c3",true,false);
	    assertGameState("r1bqkbnr/pppp1ppp/2n1p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",true,false,false);
	    illegalMoves++;
	}

	@Test
	public void knightTooFar_White() {
		illegalMovesTotal++;
	    startGame("r1bqkbnr/pppp1ppp/2n1p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",true);
	    assertMove("b1-c4",true,false);
	    assertGameState("r1bqkbnr/pppp1ppp/2n1p3/6B1/3P4/8/PPP1PPPP/RN1QKBNR/",true,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void knightCapturesPawn_Black() {
	    captureAndDropMovesTotal++;
	    startGame("r1bqkbnr/pppp1ppp/2n1p3/6B1/P2P4/8/1PP1PPPP/RN1QKBNR/",false);
	    assertMove("c6-d4",false,true);
	    assertGameState("r1bqkbnr/pppp1ppp/4p3/6B1/P2n4/8/1PP1PPPP/RN1QKBNR/p",true,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void queenCapturesKnight_White() {
		captureAndDropMovesTotal++;
	    startGame("r1bqkbnr/pppp1ppp/4p3/6B1/P2n4/8/1PP1PPPP/RN1QKBNR/p",true);
	    assertMove("d1-d4",true,true);
	    assertGameState("r1bqkbnr/pppp1ppp/4p3/6B1/P2Q4/8/1PP1PPPP/RN2KBNR/Np",false,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void kingMovesIntoCheck_Black() {
	    checkMovesTotal++;
	    startGame("r1bqkbnr/pppp1ppp/4p3/6B1/P2Q4/8/1PP1PPPP/RN2KBNR/Np",false);
	    assertMove("e8-e7",false,false);
	    assertGameState("r1bqkbnr/pppp1ppp/4p3/6B1/P2Q4/8/1PP1PPPP/RN2KBNR/Np",false,false,false);
	    checkMoves++;
	}
	
	@Test
	public void knightWrongDistance_Black() {
		illegalMovesTotal++;
	    startGame("r1bqkbnr/pppp1ppp/4p3/6B1/P2Q4/8/1PP1PPPP/RN2KBNR/Np",false);
	    assertMove("g8-e6",false,false);
	    assertGameState("r1bqkbnr/pppp1ppp/4p3/6B1/P2Q4/8/1PP1PPPP/RN2KBNR/Np",false,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void pawnLongJump_Black() {
		regularMovesTotal++;
	    startGame("r1bqkbnr/pppp1ppp/4p3/6B1/P2Q4/8/1PP1PPPP/RN2KBNR/Np",false);
	    assertMove("c7-c5",false,true);
	    assertGameState("r1bqkbnr/pp1p1ppp/4p3/2p3B1/P2Q4/8/1PP1PPPP/RN2KBNR/Np",true,false,false);
	    regularMoves++;
	}
	
	@Test
	public void queenHorizontal_White() {
		regularMovesTotal++;
	    startGame("r1bqkbnr/pp1p1ppp/4p3/2p3B1/P2Q4/8/1PP1PPPP/RN2KBNR/Np",true);
	    assertMove("d4-e4",true,true);
	    assertGameState("r1bqkbnr/pp1p1ppp/4p3/2p3B1/P3Q3/8/1PP1PPPP/RN2KBNR/Np",false,false,false);
	    regularMoves++;
	}

	@Test
	public void dropPawn_Black() {
	    captureAndDropMovesTotal++;
	    startGame("r1bqkbnr/pp1p1ppp/4p3/2p3B1/P3Q3/8/1PP1PPPP/RN2KBNR/Np",false);
	    assertMove("p-d6",false,true);
	    assertGameState("r1bqkbnr/pp1p1ppp/3pp3/2p3B1/P3Q3/8/1PP1PPPP/RN2KBNR/N",true,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void kingDiagonal_White() {
		regularMovesTotal++;
	    startGame("r1bqkbnr/pp1p1ppp/3pp3/2p3B1/P3Q3/8/1PP1PPPP/RN2KBNR/N",true);
	    assertMove("e1-d2",true,true);
	    assertGameState("r1bqkbnr/pp1p1ppp/3pp3/2p3B1/P3Q3/8/1PPKPPPP/RN3BNR/N",false,false,false);
	    regularMoves++;
	}
	
	@Test
	public void queenHitsBishop_Black() {
		captureAndDropMovesTotal++;
	    startGame("r1bqkbnr/pp1p1ppp/3pp3/2p3B1/P3Q3/8/1PPKPPPP/RN3BNR/N",false);
	    assertMove("d8-g5",false,true);
	    assertGameState("r1b1kbnr/pp1p1ppp/3pp3/2p3q1/P3Q3/8/1PPKPPPP/RN3BNR/Nb",true,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void dropKnightToCheck_White() {
		captureAndDropMovesTotal++;
	    startGame("r1b1kbnr/pp1p1ppp/3pp3/2p3q1/P3Q3/8/1PPKPPPP/RN3BNR/Nb",true);
	    assertMove("N-c7",false,false);
	    assertGameState("r1b1kbnr/pp1p1ppp/3pp3/2p3q1/P3Q3/8/1PPKPPPP/RN3BNR/Nb",true,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void queenCaptureSetCheck_White() {
		captureAndDropMovesTotal++;
	    startGame("rnbqkbnr/pppp1ppp/8/8/Q1P1p3/8/PP1PPPPP/RNB1KBNR/",true);
	    assertMove("a4-d7",true,true);
	    assertGameState("rnbqkbnr/pppQ1ppp/8/8/2P1p3/8/PP1PPPPP/RNB1KBNR/P",false,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void kingMoveStillCheck_Black() {
	    checkMovesTotal++;
	    startGame("rnbqkbnr/pppQ1ppp/8/8/2P1p3/8/PP1PPPPP/RNB1KBNR/P",false);
	    assertMove("e8-e7",false,false);
	    assertGameState("rnbqkbnr/pppQ1ppp/8/8/2P1p3/8/PP1PPPPP/RNB1KBNR/P",false,false,false);
	    checkMoves++;
	}
	
	@Test
	public void knightCaptureResolveCheck_Black() {
		checkMovesTotal++;
	    startGame("rnbqkbnr/pppQ1ppp/8/8/2P1p3/8/PP1PPPPP/RNB1KBNR/P",false);
	    assertMove("b8-d7",false,true);
	    assertGameState("r1bqkbnr/pppn1ppp/8/8/2P1p3/8/PP1PPPPP/RNB1KBNR/Pq",true,false,false);
	    checkMoves++;
	}
	
	@Test
	public void bishopMoveNotDiagonal_Black() {
	    illegalMovesTotal++;
	    startGame("r1bqkbnr/pppn1ppp/8/8/2P1p3/3P4/PP2PPPP/RNB1KBNR/Pq",false);
	    assertMove("f8-a5",false,false);
	    assertGameState("r1bqkbnr/pppn1ppp/8/8/2P1p3/3P4/PP2PPPP/RNB1KBNR/Pq",false,false,false);
	    illegalMoves++;
	}

	@Test
	public void bishopMoveRegular_Black() {
	    regularMovesTotal++;
	    startGame("r1bqkbnr/pppn1ppp/8/8/2P1p3/3P4/PP2PPPP/RNB1KBNR/Pq",false);
	    assertMove("f8-b4",false,true);
	    assertGameState("r1bqk1nr/pppn1ppp/8/8/1bP1p3/3P4/PP2PPPP/RNB1KBNR/Pq",true,false,false);
	    regularMoves++;
	}
	
	@Test
	public void bishopMoveResolveCheck_White() {
	    checkMovesTotal++;
	    startGame("r1bqk1nr/pppn1ppp/8/8/1bP1p3/3P4/PP2PPPP/RNB1KBNR/Pq",true);
	    assertMove("c1-d2",true,true);
	    assertGameState("r1bqk1nr/pppn1ppp/8/8/1bP1p3/3P4/PP1BPPPP/RN2KBNR/Pq",false,false,false);
	    checkMoves++;
	}
	
	@Test
	public void kingMoveRegular_Black() {
	    regularMovesTotal++;
	    startGame("r1bqk1nr/pppn1ppp/8/8/1bP1p3/3P4/PP1BPPPP/RN2KBNR/Pq",false);
	    assertMove("e8-e7",false,true);
	    assertGameState("r1bq2nr/pppnkppp/8/8/1bP1p3/3P4/PP1BPPPP/RN2KBNR/Pq",true,false,false);
	    regularMoves++;
	}
	
	@Test
	public void pawnDropRowOne_White() {
	    captureAndDropMovesTotal++;
	    startGame("r1bq2nr/pppnkppp/8/8/1bP1p3/3P4/PP1BPPPP/RN2KBNR/Pq",true);
	    assertMove("P-c1",true,false);
	    assertGameState("r1bq2nr/pppnkppp/8/8/1bP1p3/3P4/PP1BPPPP/RN2KBNR/Pq",true,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void pawnDropRowEight_White() {
		captureAndDropMovesTotal++;
	    startGame("r1bq2nr/pppnkppp/8/8/1bP1p3/3P4/PP1BPPPP/RN2KBNR/Pq",true);
	    assertMove("P-f8",true,false);
	    assertGameState("r1bq2nr/pppnkppp/8/8/1bP1p3/3P4/PP1BPPPP/RN2KBNR/Pq",true,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void bishopCaptureBishop_White() {
		captureAndDropMovesTotal++;
	    startGame("r1bq2nr/pppnkppp/8/8/1bP1p3/3P4/PP1BPPPP/RN2KBNR/Pq",true);
	    assertMove("d2-b4",true,true);
	    assertGameState("r1bq2nr/pppnkppp/8/8/1BP1p3/3P4/PP2PPPP/RN2KBNR/BPq",false,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void dropQueenWhileCheck_Black() {
	    checkMovesTotal++;
	    startGame("r1bq2nr/pppnkppp/8/8/1BP1p3/3P4/PP2PPPP/RN2KBNR/BPq",false);
	    assertMove("q-c1",false,false);
	    assertGameState("r1bq2nr/pppnkppp/8/8/1BP1p3/3P4/PP2PPPP/RN2KBNR/BPq",false,false,false);
	    checkMoves++;
	}
	
	@Test
	public void dropOnFigure_White() {
		captureAndDropMovesTotal++;
	    startGame("r1bq2nr/pppn1ppp/5k2/8/1BP1p3/3P4/PP2PPPP/RN2KBNR/BPq",true);
	    assertMove("B-d8",true,false);
	    assertGameState("r1bq2nr/pppn1ppp/5k2/8/1BP1p3/3P4/PP2PPPP/RN2KBNR/BPq",true,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void dropIntoCheckMate_Black() {
	    gameStartupFinishTotal++;
	    startGame("r1bq2nr/pppn1ppp/4Bk2/8/1BP1p3/3P4/PP2PPPP/RN2KBNR/Pq",false);
	    assertMove("q-c1",false,true);
	    assertGameState("r1bq2nr/pppn1ppp/4Bk2/8/1BP1p3/3P4/PP2PPPP/RNq1KBNR/P",true,true,false);
	    gameStartupFinish++;
	}
	
	@Test
	public void captureSecondPawn_White() {
	    captureAndDropMovesTotal++;
	    startGame("rnbqkbnr/pppp2pp/5p2/4P3/8/8/PPP1PPPP/RNBQKBNR/P",true);
	    assertMove("e5-f6",true,true);
	    assertGameState("rnbqkbnr/pppp2pp/5P2/8/8/8/PPP1PPPP/RNBQKBNR/PP",false,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void dropOneOfTwoPawns_White() {
		captureAndDropMovesTotal++;
	    startGame("rnbqk2r/pppp2pp/5P1n/8/1b6/2P5/PP2PPPP/RNBQKBNR/PP",true);
	    assertMove("P-f7",true,true);
	    assertGameState("rnbqk2r/pppp1Ppp/5P1n/8/1b6/2P5/PP2PPPP/RNBQKBNR/P",false,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void captureAndPromotion_White() {
		captureAndDropMovesTotal++;
	    startGame("rnbq3r/pppp1kPp/8/8/1P6/8/PP2PnPP/RNBQKBNR/BPPpp",true);
	    assertMove("g7-h8",true,true);
	    assertGameState("rnbq3Q/pppp1k1p/8/8/1P6/8/PP2PnPP/RNBQKBNR/BPPRpp",false,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void captureIntoFullPocket_Black() {
		captureAndDropMovesTotal++;
	    startGame("rnbq3Q/pppp1k1p/8/8/1P6/8/PP2PnPP/RNBQKBNR/BPPRpp",false);
	    assertMove("f2-d1",false,true);
	    assertGameState("rnbq3Q/pppp1k1p/8/8/1P6/8/PP2P1PP/RNBnKBNR/BPPRppq",true,false,false);
	    captureAndDropMoves++;
	}
	
	@Test
	public void pawnCaptureWrongDirection_Black() {
	    illegalMovesTotal++;
	    startGame("rnbQ4/pppp1kPp/8/8/1P6/8/Pn2P1PP/RNB1KBNR/BPQRpppq",false);
	    assertMove("c7-d8",false,false);
	    assertGameState("rnbQ4/pppp1kPp/8/8/1P6/8/Pn2P1PP/RNB1KBNR/BPQRpppq",false,false,false);
	    illegalMoves++;
	}
	
	@Test
	public void promotionRegular_White() {
	    regularMovesTotal++;
	    startGame("rnbQ4/pppp2Pp/6k1/8/1P6/8/Pn1pPKPP/RNB2BNR/BPQRppq",true);
	    assertMove("g7-g8",true,true);
	    assertGameState("rnbQ2Q1/pppp3p/6k1/8/1P6/8/Pn1pPKPP/RNB2BNR/BPQRppq",false,false,false);
	    regularMoves++;
	}
	
	@Test
	public void checkMateRegular_White() {
	    gameStartupFinishTotal++;
	    startGame("rnbqkbnr/ppppp2p/8/6p1/4Pp2/1P1P4/P1P2PPP/RNBQKBNR/",true);
	    assertMove("d1-h5",true,true);
	    assertGameState("rnbqkbnr/ppppp2p/8/6pQ/4Pp2/1P1P4/P1P2PPP/RNB1KBNR/",false,true,true);
	    gameStartupFinish++;
	}
	
	@Test
	public void startupFormationOk_White() {
		gameStartupFinishTotal++;
	    startGame();
	    assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
	    gameStartupFinish++;
	}
	
	@Test
	public void wrongPlayer() {
		illegalMovesTotal++;
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("b2-b3",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
		illegalMoves++;
	}
	
	@Test
	public void noFigureAtSource_White() {
		illegalMovesTotal++;
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("d3-d4",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
		illegalMoves++;
	}
	
	@Test
	public void useBlackPawn_White() {
		illegalMovesTotal++;
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("a7-a6",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
		illegalMoves++;
	}
	
	@Test
	public void dropQueenNotInPocket_White() {
		illegalMovesTotal++;
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("Q-a5",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
		illegalMoves++;
	}
	
	@Test
	public void noMove_White() {
		illegalMovesTotal++;
		startGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true);
		assertMove("a7-a7",true,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
		illegalMoves++;
	}
	
	@Test
	public void invalidMoveFormat1() {
		illegalMovesTotal++;
		startGame();
		assertMove("k2-b9",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
		illegalMoves++;
	}

	@Test
	public void invalidMoveFormat3() {
		illegalMovesTotal++;
		startGame();
		assertMove("geradeaus-dannlinks",false,false);
		assertGameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/",true,false,false);
		illegalMoves++;
	}
	
	@Test
	public void pawnJumpOver_Black() {
		illegalMovesTotal++;
	    startGame("rnbqkbnr/pppp1ppp/B7/4p3/4P3/8/PPPP1PPP/RNBQK1NR/",false);
	    assertMove("a7-a5",false,false);
	    assertGameState("rnbqkbnr/pppp1ppp/B7/4p3/4P3/8/PPPP1PPP/RNBQK1NR/",false,false,false);
	    illegalMoves++;
	}
}

