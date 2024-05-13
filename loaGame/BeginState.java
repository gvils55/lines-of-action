package loaGame;

import java.util.List;

public class BeginState extends StateMachine{
	
	public Move getMove(Game game, MachinePlayer player) {
		Board b = game.getBoard();
		List<Piece> Pieces = b.getColorPieces(player.getColor());
		
		Move move = null;
		int max= 0;
		int blockAmount = 0;
		for(Piece currPiece: Pieces) {
			List<Move> moves = currPiece.getPossibleMoves();
			for(Move currMove: moves) {
				blockAmount = BoardScanner.amountOfBlocks(b, currMove, player);
				if(blockAmount > max) {
					max = blockAmount;
					move = currMove;
				}
			}
		}
		System.out.println(max);
		return move;
	}

}
