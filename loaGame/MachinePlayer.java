package loaGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MachinePlayer extends Player {
	
	
	public MachinePlayer(String color) {
		super(color);
		
		// TODO Auto-generated constructor stub
	}
	
	public void makeMove(Game game) {
		Move move;
		String currState = BoardScanner.playerState(game, this);
		if(currState == "beginnig") {
			move = StateMachine.getBegginMove(game, this);
			System.out.println("beggin");
			
		}
		else if(currState == "middle") {
			move = StateMachine.getMiddleMove(game, this);
			System.out.println("middle");

			
		}
		else {
			move = StateMachine.getEndMove(game, this);
			System.out.println("end");
		}
		
		
		printMove(move);
		game.getBoard().doMove(move);
		
	}
	
	
	public List<Move> generateMove(Game game){
		List<Move> allMoves = new ArrayList<Move>();
		Board b = game.getBoard();
		
		Map<Integer, Piece> pieces = b.getPieces(); // Get the current board using the getter method
		for(Map.Entry<Integer, Piece> entry :pieces.entrySet()) {
			Piece curr = entry.getValue();
			if(curr.getColor() == color) {
				b.calcMoves(curr);
				allMoves.addAll(curr.getPossibleMoves());
			}
		}
		return allMoves;
	}
	
	

	
	public void printMove(Move move) {
		System.out.println("start: " + move.getStart_row() + "," + move.getStart_col() + " end: " + move.getEnd_row() + "," + move.getEnd_col());
		System.out.println();

	}
}
