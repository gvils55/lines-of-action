package loaGame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;


public class Input extends MouseAdapter{

	private Game game;

	public Input(Game game) {
		this.game = game;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println(game.getCurrPlayer().color);
		if(game.isGameOver()== false)
			handleMouseClick(e);

	}

	public void handleMouseClick(MouseEvent e) {

		int col = e.getX() / Constants.SQ_SIZE;
		int row = e.getY() / Constants.SQ_SIZE;
		int pos = (row*Constants.DIMENSION) + col;
		Board board = game.getBoard();
		Map<Integer, Piece> currentBoard = board.getPieces(); // Get the current board using the getter method

		if (currentBoard.containsKey(pos)) {
			Piece pieceAtPos = currentBoard.get(pos);
			if(pieceAtPos.getColor() == game.getCurrPlayer().getColor()) {
				game.selectedPiece = pieceAtPos;
				board.calcMoves(game.selectedPiece);
				//System.out.println(game.selectedPiece.getRow() + ", " + game.selectedPiece.getCol());
			}
			else if(game.selectedPiece != null && pieceAtPos.getColor() != game.getCurrPlayer().getColor()) {
				Move move = game.selectedPiece.getMove(row, col); 
				if(move != null) {
					board.doMove(move);
					game.selectedPiece = null;
					game.turnDone = true;
				}
			}
		}
		else {
			if(game.selectedPiece != null) {
				Move move = game.selectedPiece.getMove(row, col);  
				if(move != null) {
					board.doMove(move);
					game.selectedPiece = null;
					game.turnDone = true;
				}
			}
		}
		game.repaint();
	}

}
