package loaGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BoardScanner {



	public static String playerState(Game game, MachinePlayer player) {
		Board b = game.getBoard();

		List<Piece> Pieces = b.getColorPieces(player.getColor());
		List<Integer> Positions = positionsList(Pieces);
		List<Integer> maxLine = getMaxLine(game, Positions, player.getColor());

		String color;
		if(player.getColor() == "white")
			color = "black";
		else
			color = "white";

		List<Piece> oppPieces = b.getColorPieces(color);
		List<Integer> oppPositions = positionsList(oppPieces);
		List<Integer> oppMaxLine = getMaxLine(game, oppPositions, color);


		int leftForMe =Positions.size()- maxLine.size();
		int leftForOpponent =oppPositions.size()- oppMaxLine.size();

		if(leftForMe < 5 || leftForOpponent < 5)
			return "end";



		int row,col;
		int count= 0, playerCount = 0 ;

		for(Piece currPiece: Pieces) {
			
			row = currPiece.getRow();
			col = currPiece.getCol();
			if(player.getColor() == "black") {
				if(row == 0 || row == 7)
					count++;
			}
			else {
				if(col == 0 || col == 7)
					count++;
			}
			playerCount++;
		}

		int diff = playerCount - count; 
		String result; 
		if(diff < 4)
			result = "beginnig";	
		else {
			 if (diff > 6 )
					result = "end";	
			 else
					result = "middle";	

		}
		return result;
	}


	public static String isAhead(Game game) {
		Board b = game.getBoard();
		List<Piece> blackPieces = b.getColorPieces("black");
		List<Integer> blackPositions = positionsList(blackPieces);
		List<Integer> maxBlack = getMaxLine(game, blackPositions, "black");

		List<Piece> whitePieces = b.getColorPieces("white");
		List<Integer> whitePositions = positionsList(whitePieces);
		List<Integer> maxWhite = getMaxLine(game, whitePositions, "white");


		int leftForWhite =whitePositions.size()- maxWhite.size();
		int leftForBlack =blackPositions.size()- maxBlack.size();

		if(leftForWhite+1< leftForBlack)
			return "white";

		else if(leftForBlack+1 < leftForWhite)
			return "black";

		return "none";
	}


	public static List<Integer> positionsList(List<Piece> colorPieces){

		List<Integer> colorPositions = new ArrayList<>();
		for(Piece p: colorPieces) {
			int pos = p.getRow()*Constants.DIMENSION + p.getCol();
			colorPositions.add(pos);
		}
		return colorPositions;
	}


	public static List<Integer> getMaxLine(Game game, List<Integer> positions, String color) {
		List<Integer> maxLine = new ArrayList<>();
		Board b = game.getBoard();

		// Create a copy of the positions list to avoid modifying the original list
		List<Integer> positionsCopy = new ArrayList<>(positions);

		while (!positionsCopy.isEmpty()) {
			List<Integer> currentLine = new ArrayList<>();
			int startPos = positionsCopy.get(0); // Start from the first unvisited position

			// Find all connected pieces starting from startPos
			b.countConnectedPieces(color, startPos, currentLine);

			if (currentLine.size() > maxLine.size()) {
				maxLine = new ArrayList<>(currentLine); // Found a larger connected group
			}

			// Remove all positions that were part of the current connected group
			positionsCopy.removeAll(currentLine);
		}
		return maxLine;
	}



	public static Move getNeutralMove(Game game, MachinePlayer player) {
		Board b = game.getBoard();
		List<Piece> Pieces = b.getColorPieces(player.getColor());

		List<Integer> positions = positionsList(Pieces);
		List<Integer> maxLine = getMaxLine(game, positions, player.getColor());
		List<Integer> copiedPositions = new ArrayList<>(positions);
		b.generateMove(player.getColor());

		copiedPositions.removeAll(maxLine);

		Move connective = checkConnectiveMove(game, player, copiedPositions, maxLine);
		if(connective!= null) {
			return connective;
		}
		//System.out.println(1);
		Move usefull = checkUsefullMoves(game, player, copiedPositions, maxLine);
		//System.out.println(2);
		return usefull;
	}


	public static Move getDefensiveMove(Game game, MachinePlayer player) {
		Board b = game.getBoard();
		List<Piece> Pieces = b.getColorPieces(player.getColor());

		List<Integer> positions = positionsList(Pieces);
		List<Integer> maxLine = getMaxLine(game, positions, player.getColor());
		List<Integer> copiedPositions = new ArrayList<>(positions);
		b.generateMove(player.getColor());
		copiedPositions.removeAll(maxLine);
		Move move = null;
		Piece threatenedPiece = getThreatenedPiece(game, player, maxLine);
		if(threatenedPiece != null) {
			move = defensiveMove(game, player, threatenedPiece, b, maxLine);
			if(move == null)
				move = getNeutralMove(game, player);
		}
		else {
			move = getNeutralMove(game, player);
		}
		return move;
	}


	public static Move getOfensiveMove(Game game, MachinePlayer player) {
		Board b = game.getBoard();
		String color = player.getColor();
		String opponentColor = (color.equals("white")) ? "black" : "white";

		List<Piece> myPieces = b.getColorPieces(color);
		List<Integer> myPositions = positionsList(myPieces);
		List<Integer> myMaxLine = getMaxLine(game, myPositions, color);

		List<Piece> opponentPieces = b.getColorPieces(opponentColor);
		List<Integer> opponentPositions = positionsList(opponentPieces);
		List<Integer> opponentMaxLine = getMaxLine(game, opponentPositions, opponentColor);


		List<Integer> copiedPositions = new ArrayList<>(myPositions);
		//b.generateMove(color);
		copiedPositions.removeAll(myMaxLine);
		Move move = null;
		if(opponentPieces.size()>5) {
			move = bestCapturingMove(game, player, b, opponentMaxLine , copiedPositions);
			if(move!=null)
				return move;
			move = bestCapturingMove(game, player, b, opponentMaxLine , myMaxLine);
			if(move!=null)
				return move;
		}

		move = getNeutralMove(game, player);

		return move;
	}



	public static List<Move> checkCapturingMove(Game game, MachinePlayer player, Board board, List<Integer> opponentMaxLine, List<Integer> myPiecePos) {
		Board copiedBoard = new Board(board);
		Map<Integer, Piece> pieces = copiedBoard.getPieces();
		Piece piece;
		int pos = 0;
		List<Move> result = new ArrayList<>();
		for(Integer piecePos: myPiecePos) {
			piece = pieces.get(piecePos);
			copiedBoard.calcMoves(piece);
			List<Move> moves = piece.getPossibleMoves();
			for(Move move: moves) {
				pos = (move.getEnd_row()*Constants.DIMENSION) + move.getEnd_col();
				if(opponentMaxLine.contains(pos)) 
					result.add(move);
			}

		}
		return result;
	}



	public static Move bestCapturingMove(Game game, MachinePlayer player, Board board, List<Integer> opponentMaxLine , List<Integer> myPiecePos) {

		List<Move> moves = checkCapturingMove(game, player, board, opponentMaxLine, myPiecePos);
		int originalMaxLine = opponentMaxLine.size();
		Move best = null;
		int minMaxLine = Integer.MAX_VALUE;
		List<Piece> opponentPieces;
		List<Integer> opponentPositions;
		List<Integer> opponentMaxLine1;
		String color = player.getColor();
		String opponentColor = (color.equals("white")) ? "black" : "white";

		for(Move move:moves) {
			Board copy = new Board(board);
			copy.doMove(move);
			opponentPieces = copy.getColorPieces(opponentColor);
			opponentPositions = positionsList(opponentPieces);
			opponentMaxLine1 = getMaxLine(game, opponentPositions, opponentColor);
			if(opponentMaxLine1.size()<minMaxLine) {
				minMaxLine = opponentMaxLine1.size();
				best = new Move(move.getStart_row(), move.getStart_col(), move.getEnd_row(), move.getEnd_col(), move.getKind());
			}
		}
		if(originalMaxLine-minMaxLine==1)
			best = null;
		return best;

	}



	public static Move defensiveMove(Game game, MachinePlayer player, Piece threatenedPiece, Board board, List<Integer> maxLine) {

		Board copiedBoard = new Board(board);
		copiedBoard.calcMoves(threatenedPiece);
		List<Move> moves = threatenedPiece.getPossibleMoves();
		List<Move> copiedMoves = new ArrayList<>(moves);
		List<Move> bestMoves = new ArrayList<>();
		//copiedBoard.printV(maxLine);
		for(Move move: moves) {
			if(canBeCaptured(copiedBoard, move, player))
				copiedMoves.remove(move);
			//System.out.println(move.getStart_row() + "," + move.getStart_col() + "-" + move.getEnd_row() + "," + move.getEnd_col());
		}

		for(Move move: copiedMoves) {
			if(moveIsConnective(move.getEnd_row(), move.getEnd_col(), player, copiedBoard,maxLine))
				return move;
			if(isMoveToCenter(move) || doesMoveBlock(copiedBoard, move, player))
				bestMoves.add(move);
		}
		if(bestMoves.isEmpty()) {
			if(!copiedMoves.isEmpty())
				return copiedMoves.get(0);
			else 
				return null;
		}
		else {
			for(Move move: bestMoves) {
				if(isMoveToCenter(move) && doesMoveBlock(copiedBoard, move, player))
					return move;
			}
		}
		return bestMoves.get(0);
	}



	public static Piece getThreatenedPiece(Game game, MachinePlayer player, List<Integer> maxLine) {
		Board b = game.getBoard();
		Map <Integer, Piece> pieces = b.getPieces();

		for(Integer pos: maxLine) {
			Piece curr = pieces.get(pos);
			if(pieceIsThreatened(b, player, curr))
				return curr;
		}

		return null;
	}



	public static boolean pieceIsThreatened(Board board, MachinePlayer player, Piece piece) {
		String color = player.getColor(); // Get the player's color
		Board copiedBoard = new Board(board);
		// Switch color for opponent
		String opponentColor = (color.equals("white")) ? "black" : "white";
		// Generate opponent's moves
		List<Move> opponentMoves = copiedBoard.generateMove(opponentColor);
		for (Move opponentMove : opponentMoves) {
			if (opponentMove.getEnd_row() == piece.getRow() && opponentMove.getEnd_col() == piece.getCol()) {
				return true; // Piece can be captured
			}
		}
		return false;

	}


	public static Move checkConnectiveMove(Game game, MachinePlayer player, List<Integer> positions, List<Integer> maxLine) {
		Board b = game.getBoard();
		Map<Integer, Piece> pieces = b.getPieces();
		for(int pos: positions) {
			Piece p = pieces.get(pos);
			Move move = canPieceConnect(game, player, p, maxLine);
			if(move != null)
				return move;
		}
		return null;

	}



	public static Move canPieceConnect(Game game, MachinePlayer player, Piece curr, List<Integer> maxLine) {
		List<Move> moves = curr.getPossibleMoves();

		for(Move move: moves) {
			if(moveIsConnective(move.getEnd_row(), move.getEnd_col(), player, game.getBoard(), maxLine) && !canBeCaptured(game.getBoard(), move, player))
				return move;
		}
		return null;

	}


	public static boolean moveIsConnective(int row, int col, MachinePlayer player, Board b, List<Integer> maxLine) {
		for(int pos: maxLine) {
			int r = pos / Constants.DIMENSION;
			int c = pos % Constants.DIMENSION;
			if(r-row < 2 && r-row > -2) {
				if(c-col < 2 && c-col > -2) {
					return true;
				}
			}
		}
		return false;
	}



	public static Move checkUsefullMoves(Game game, MachinePlayer player, List<Integer> positions, List<Integer> maxLine) {
		Move best = null;
		Board board = game.getBoard();
		Map<Integer, Piece> pieces = board.getPieces();
		List<Move> bestPossibleMoves = new ArrayList<>();

		for (Integer pos : positions) {
			Piece piece = pieces.get(pos);
			List<Move> moves = piece.getPossibleMoves();
			best = findMoveClosestToConnectedPiecesAndSafe(board, player, maxLine, moves);
			if(best!= null) {
				bestPossibleMoves.add(best);

			}

		}
		List<Move> copyOfMoves = new ArrayList<>(bestPossibleMoves); // Make a copy of the move list
		Iterator<Move> iterator1 = copyOfMoves.iterator();
		Move move;

		while (iterator1.hasNext()) {
			move = iterator1.next();
			if (isMoveToCenter(move) && doesMoveBlock(board, move, player)) 
				return move;
			else {
				if (!isMoveToCenter(move) && !doesMoveBlock(board, move, player)) 
					iterator1.remove(); // Safe removal using iterator
			}
		}
		if (!copyOfMoves.isEmpty()) 
			return copyOfMoves.get(0);

		move = findMoveClosestToConnectedPiecesAndSafe(board, player, maxLine, bestPossibleMoves);
		return move;

	}



	public static Move findMoveClosestToConnectedPiecesAndSafe(Board board, MachinePlayer player, List<Integer> maxLine, List<Move> moves) {
		Move bestMove = null;
		double minDistance = Double.POSITIVE_INFINITY;

		for (Move move : moves) {
			// Check if the move can be captured
			if (!canBeCaptured(board, move, player)) {
				// Calculate distance to connected pieces
				double distance = calculateDistanceToConnectedPieces(move, maxLine);
				if (distance < minDistance) {
					minDistance = distance;
					bestMove = move;
				}
			}
		}
		return bestMove;
	}



	public static double calculateDistanceToConnectedPieces(Move move, List<Integer> maxLine) {
		double minDistance = Double.POSITIVE_INFINITY;
		// Calculate distance from move to each connected position
		for (Integer position : maxLine) {
			int rowPos = position / Constants.DIMENSION;
			int colPos = position % Constants.DIMENSION;
			double distance = calculateDistance(move.getEnd_row(), move.getEnd_col(), rowPos, colPos);
			if (distance < minDistance) {
				minDistance = distance;
			}
		}
		return minDistance;
	}



	public  static double calculateDistance(int x1, int y1, int x2, int y2) {
		// Calculate distance between two positions (using appropriate metric)
		// You can use Manhattan distance, Euclidean distance, etc.
		// Here's a simple example using Manhattan distance:
		int dx = Math.abs(x1 - x2);
		int dy = Math.abs(y1 - y2);
		return dx + dy; // Manhattan distance
	}



	public static boolean isMoveToCenter(Move move) {
		if(move == null)
			return false;
		if(move.getEnd_row() > 1 && move.getEnd_row() < 6)
			if(move.getEnd_col() > 1 && move.getEnd_col() < 6)
				return true;
		return false;
	}



	public static boolean doesMoveBlock(Board board, Move move, MachinePlayer player ) {
		String color = "white";
		if(color == player.getColor())
			color = "black";

		Board copiedBoard = new Board(board);
		List<Move> movesBefore = copiedBoard.generateMove(color);
		copiedBoard.doMove(move);
		List<Move> movesAfter = copiedBoard.generateMove(color);
		if(movesBefore.size() >movesAfter.size()+2)
			return true;
		return false;
	}


	public static int amountOfBlocks(Board board, Move move, MachinePlayer player) {
		String color = "white";
		if(color == player.getColor())
			color = "black";
		Board copiedBoard = new Board(board);
		List<Move> movesBefore = copiedBoard.generateMove(color);
		copiedBoard.doMove(move);
		List<Move> movesAfter = copiedBoard.generateMove(color);
		return movesBefore.size() -movesAfter.size();

	}

	public static boolean canBeCaptured(Board board, Move move, MachinePlayer player) {
		String color = player.getColor(); // Get the player's color
		Board copiedBoard = new Board(board);
		copiedBoard.doMove(move);
		// Switch color for opponent
		String opponentColor = (color.equals("white")) ? "black" : "white";

		// Generate opponent's moves
		List<Move> opponentMoves = copiedBoard.generateMove(opponentColor);

		for (Move opponentMove : opponentMoves) {
			//System.out.println(opponentMove.getStart_row() + "," + opponentMove.getStart_col() + "-" + opponentMove.getEnd_row() + "," + opponentMove.getEnd_col());
			if (opponentMove.getEnd_row() == move.getEnd_row() && opponentMove.getEnd_col() == move.getEnd_col()) {
				return true; // Piece can be captured
			}
		}
		return false; // Piece cannot be captured
	}
}
