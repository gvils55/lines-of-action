package loaGame;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
	private final int BOARD_SIZE = Constants.DIMENSION; // Size of the game board
	private Map<Integer, Piece> pieces; // HashMap to represent the game board
	private List<Piece>[] rowArr; // ArrayLists to store counts of pieces in rows
	private List<Piece>[] colArr; // ArrayLists to store counts of pieces in columns
	private List<Piece>[] downDiagonalArr; // ArrayLists to store counts of pieces in diagonals
	private List<Piece>[] upDiagonalArr; // ArrayLists to store counts of pieces in diagonals

	private static final int[][] DIRECTIONS = {
			{-1, -1}, {-1, 0}, {-1, 1},
			{0, -1},           {0, 1},
			{1, -1}, {1, 0}, {1, 1}
	};
	
	public Board(Board originalBoard) {
        // Initialize lists and maps
        this.rowArr = new List[BOARD_SIZE];
        this.colArr = new List[BOARD_SIZE];
        this.downDiagonalArr = new List[2 * BOARD_SIZE - 1];
        this.upDiagonalArr = new List[2 * BOARD_SIZE - 1];
		this.pieces = new HashMap<Integer, Piece>();
        
        for (int i = 0; i < BOARD_SIZE; i++) {
			rowArr[i] = new ArrayList<Piece>();
			colArr[i] = new ArrayList<Piece>();
		}
		for (int i = 0; i < downDiagonalArr.length; i++) {
			downDiagonalArr[i] = new ArrayList<Piece>();
			upDiagonalArr[i] = new ArrayList<Piece>();

		}
		
        // Copy pieces from original board to the new board
        for (Map.Entry<Integer, Piece> entry : originalBoard.getPieces().entrySet()) {
            int pos = entry.getKey();
            Piece originalPiece = entry.getValue();
            // Create a new piece with the same properties
            Piece copiedPiece = new Piece(originalPiece.getRow(), originalPiece.getCol(), originalPiece.getColor());
            // Add the copied piece to the new board
            addPieceToAll(copiedPiece);
        }
    }

	
	public Board() {

		this.pieces = new HashMap<Integer, Piece>();
		this.rowArr = new List[BOARD_SIZE];
		this.colArr = new List[BOARD_SIZE];
		this.downDiagonalArr = new List[2 * BOARD_SIZE - 1];
		this.upDiagonalArr = new List[2 * BOARD_SIZE - 1];


		// Initialize ArrayLists for rows, columns, and diagonals
		for (int i = 0; i < BOARD_SIZE; i++) {
			rowArr[i] = new ArrayList<Piece>();
			colArr[i] = new ArrayList<Piece>();
		}
		for (int i = 0; i < downDiagonalArr.length; i++) {
			downDiagonalArr[i] = new ArrayList<Piece>();
			upDiagonalArr[i] = new ArrayList<Piece>();

		}
		createPieces();
	}

	public void createPieces() {

		for(int i=1; i<Constants.DIMENSION-1; i++) {
			int row_pos = 0;
			int col_pos = i;
			Piece bp1 = new Piece(row_pos, col_pos, "black");
			Piece bp2 = new Piece(Constants.DIMENSION-1, col_pos, "black");

			row_pos = i;
			col_pos =0;
			Piece wp1 = new Piece(row_pos, col_pos, "white");
			Piece wp2 = new Piece(row_pos, Constants.DIMENSION-1, "white");

			addPieceToAll(bp1);
			addPieceToAll(bp2);
			addPieceToAll(wp1);
			addPieceToAll(wp2);


		}
	}




	public void addPieceToAll(Piece currPiece) {
		int row_num = currPiece.getRow();
		int col_num = currPiece.getCol();

		int pos = (row_num*Constants.DIMENSION) + col_num;
		rowArr[row_num].add(currPiece);
		colArr[col_num].add(currPiece);
		downDiagonalArr[row_num-col_num+Constants.DIMENSION-1].add(currPiece);
		upDiagonalArr[row_num+col_num].add(currPiece);
		pieces.put(pos, currPiece);


	}



	public void removePiecesFromAll(Piece currPiece) {
		if(currPiece == null)
			return;
		int row_num = currPiece.getRow();
		int col_num = currPiece.getCol();

		int pos = (row_num*Constants.DIMENSION) + col_num;
		rowArr[row_num].remove(currPiece);
		colArr[col_num].remove(currPiece);
		downDiagonalArr[row_num-col_num+Constants.DIMENSION-1].remove(currPiece);
		upDiagonalArr[row_num+col_num].remove(currPiece);
		pieces.remove(pos);
	}



	public void calcMoves(Piece currPiece) {
		if(!currPiece.getPossibleMoves().isEmpty()){
			return;
		}
		int colPos = currPiece.getCol();
		int rowPos = currPiece.getRow();

		int colCount = colArr[colPos].size();
		int rowCount = rowArr[rowPos].size();
		int upDiagCount = upDiagonalArr[colPos + rowPos].size();
		int downDiagCount = downDiagonalArr[rowPos-colPos+Constants.DIMENSION-1].size();

		

		calcVertMoves(currPiece, rowCount, "h");
		calcVertMoves(currPiece, colCount, "v");
		calcVertMoves(currPiece, upDiagCount, "du");
		calcVertMoves(currPiece, downDiagCount, "dd");


	}



	public void calcVertMoves(Piece currPiece, int count, String lineKind) {
		int row  = currPiece.getRow();
		int col = currPiece.getCol();
		int rowCount =count;
		int colCount =count;

		if(lineKind == "v")
			colCount = 0;
		else if(lineKind == "h")
			rowCount = 0;
		
		else if(lineKind == "du") {
			rowCount *=-1;
		}
		int helper = 1;

		int end_row, end_col;
		Move currMove;

		for(int i = 0; i<2; i++) {
			end_row = row+(rowCount*helper);
			end_col = col+(colCount*helper);
			if(isInBoundries(end_row, end_col)) {
				if(noEnemyBetween(currPiece, count*helper, lineKind)) {
					int pos = Constants.DIMENSION*(end_row) + (end_col);
					Piece curr_pos = pieces.get(pos);
					if(curr_pos!= null && curr_pos.getColor() == currPiece.getColor()) {
					}
					else {
						if(curr_pos == null)
							currMove = new Move(row, col, end_row, end_col, 'm');
						else
							currMove = new Move(row, col, end_row, end_col, 'e');
						currPiece.addMove(currMove);
					}
				}
			}
			helper *=-1;
		}
	}



	public boolean isInBoundries(int row, int col) {
		boolean bool;
		bool= row <Constants.DIMENSION && row >=0 && col <Constants.DIMENSION && col >=0;
		return bool;
	}



	public boolean noEnemyBetween(Piece currPiece, int count, String lineKind) {
		int row  = currPiece.getRow();
		int col = currPiece.getCol();
		int rowDir=1;
		int colDir =1;
		int positiveCount = count;

		if(lineKind == "v")
			colDir = 0;
		else if(lineKind == "h")
			rowDir = 0;
		else if(lineKind == "du") {
			rowDir *=-1;
		}
		if(count <0) {
			rowDir *= -1;
			colDir *= -1;
			positiveCount *= -1;
		}
		for(int i=1; i<positiveCount; i++) {
			int endRow = row + i*rowDir;
			int endCol = col + i*colDir;

			int pos = Constants.DIMENSION*(row + i*rowDir) + (col + i*colDir);
			//if (lineKind != "h" && lineKind != "v")
			//System.out.println(pos + "," + lineKind);
			Piece curr_pos = pieces.get(pos);
			if(curr_pos!= null && curr_pos.getColor() != currPiece.getColor()) {
				return false;
			}
		}
		return true;
	}



	public void doMove(Move move) {
		int start_pos = (move.getStart_row()*Constants.DIMENSION) + move.getStart_col();
		int end_pos = (move.getEnd_row()*Constants.DIMENSION) + move.getEnd_col();
		Piece startPosPiece = pieces.get(start_pos);
		Piece endPosPiece = pieces.get(end_pos);
		removePiecesFromAll(startPosPiece);
		removePiecesFromAll(endPosPiece);

		startPosPiece.setRow(move.getEnd_row());
		startPosPiece.setCol(move.getEnd_col());
		addPieceToAll(startPosPiece);
		clearAllMoves();
	}



	public int countPiecesOfColor(String color) {
		// Count the total number of pieces of the specified color on the board
		int count = 0;
		for (Piece piece : pieces.values()) {
			if (piece.getColor().equals(color)) {
				count++;
			}
		}
		return count;
	}



	public boolean checkWin(String color) {
		Piece p = getFirstPiece(color);
		List<Integer> visited = new ArrayList<>();
		int pos = p.getRow()*Constants.DIMENSION + p.getCol();
		List<Piece> colorPiece = getColorPieces(color);
		int pieceCount = colorPiece.size();
		countConnectedPieces(color, pos, visited);
		int connCount = visited.size();


		if(connCount == pieceCount) {
			System.out.println("game over: " + color + "player wins");
			return true;
		}
		return false;
	}



	public void printV(List<Integer> visited) {
		for(int v:visited)
			System.out.print(v + ",");
		System.out.println("end");
	}



	public List<Piece> getColorPieces(String color) {
		List<Piece> colorPiece = new ArrayList<>();

		for (Piece piece : pieces.values()) {
			if (piece != null && piece.getColor().equals(color)) 
				colorPiece.add(piece);

		}
		return colorPiece;
	}



	public List<Move> generateMove(String color){
		List<Move> allMoves = new ArrayList<Move>();


		for(Map.Entry<Integer, Piece> entry :pieces.entrySet()) {
			Piece curr = entry.getValue();
			if(curr.getColor() == color) {
				calcMoves(curr);
				allMoves.addAll(curr.getPossibleMoves());
			}
		}
		return allMoves;
	}



	public void countConnectedPieces(String color, int pos, List<Integer> visited) {
		int row = pos / Constants.DIMENSION;
		int col = pos % Constants.DIMENSION;
		if (pieces.get(pos) == null || pieces.get(pos).getColor()!= color || visited.contains(pos)) {
			return; // If the position is empty, not of the desired color, or already visited, return 0
		}

		visited.add(pos); // Mark the current position as visited

		// Iterate through all directions
		for (int i = 0; i < DIRECTIONS.length; i++) {
			int[] direction = DIRECTIONS[i];
			int newRow = row + direction[0];
			int newCol = col + direction[1];
			int newPos = Constants.DIMENSION * newRow + newCol;

			// Check if the new position is within the board boundaries
			if (isInBoundries(newRow, newCol)) {
				// Recursively count connected pieces in each direction and accumulate the count
				countConnectedPieces(color, newPos, visited);
			}
		}
		return;
	}



	public Piece getFirstPiece(String color) {
		for(Piece p: pieces.values()) {
			if(p.getColor() == color)
				return p;
		}
		return null;
	}



	public void clearAllMoves() {
		for(Piece p: pieces.values())
			p.clearMoves();
	}


	
	public List<Integer> positionsList(List<Piece> colorPieces){

		List<Integer> colorPositions = new ArrayList<>();
		for(Piece p: colorPieces) {
			int pos = p.getRow()*Constants.DIMENSION + p.getCol();
			colorPositions.add(pos);
		}
		return colorPositions;
	}
	
	
	public Map<Integer, Piece> getPieces() {
		return pieces;
	}

	// Setter for the board
	public void setBoard(Map<Integer, Piece> board) {
		this.pieces = board;
	}

}