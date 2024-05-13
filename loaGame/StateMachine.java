package loaGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class StateMachine {

	public static Move getBegginMove(Game game, MachinePlayer player) {
		Board b = game.getBoard();
		Move move = null;
		int max= 0;
		int blockAmount = 0;
		List<Move> moves = b.generateMove(player.getColor());
		for(Move currMove: moves) {

			blockAmount = BoardScanner.amountOfBlocks(b, currMove, player);
			if(blockAmount > max && notInEdges(currMove)) {
				max = blockAmount;
				move = currMove;
			}
		}


		return move;
	}

	public static Move getMiddleMove(Game game, MachinePlayer player) {
		Move best = null;
		Board b = game.getBoard();

		List<Move> moves = b.generateMove(player.getColor());
		List<Move> goodMoves = new ArrayList<Move>();

		for(Move currMove: moves) {
			if(BoardScanner.isMoveToCenter(currMove)) {
				if(currMove.getStart_row() > 1 && currMove.getStart_row() < 6)
					if(currMove.getStart_col() > 1 && currMove.getStart_col() < 6)
						goodMoves.add(currMove);
			}
		}
		Move move;
		Iterator<Move> iterator = goodMoves.iterator();
		while (iterator.hasNext()) {
			move = iterator.next();
			if (BoardScanner.doesMoveBlock(b, move, player)) 
				return move;

		}
		return BoardScanner.getNeutralMove(game, player);
	}

	public static Move getEndMove(Game game, MachinePlayer player) {
		String aheadPlayer = BoardScanner.isAhead(game);
		Move move;
		if(aheadPlayer == player.getColor()) {
			System.out.println("do defense");
			move = BoardScanner.getDefensiveMove(game, player);

		}

		else if(aheadPlayer == "none") {
			System.out.println("do nutral");
			move = BoardScanner.getNeutralMove(game, player);

		}
		else {
			System.out.println("do offense");
			move = BoardScanner.getOfensiveMove(game, player);

		}
		return move;
	}

	public static boolean notInEdges(Move move) {
		if(move.getEnd_row() != 0 && move.getEnd_row() != 7) {
			if(move.getEnd_col() != 0 && move.getEnd_col() != 7) 
				return true;

		}
		return false;
	}
}
