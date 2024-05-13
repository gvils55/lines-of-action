package loaGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;



public class Game extends JPanel{

	private Font font = new Font("monospace", Font.BOLD, 18);
	private Board board;
	private boolean gameOver;
	
	private Player currPlayer;
	private Player player1;
    private Player player2;
	

	Input input = new Input(this);
	public Piece selectedPiece;
	public boolean turnDone = false;



	public Game() {
		this.board = new Board();
		this.gameOver = false;

		this.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
	}
	
	public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currPlayer = this.player1;
    }


	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g; // Store the Graphics object for later use
		drawBoard(g2d);
		drawPieces(g2d);
		if(selectedPiece != null) {
			showMoves(selectedPiece, g2d);
		}
		if(gameOver == true)
			endGame(g2d, currPlayer.getColor());

	}

	public void startGame() {
	    while (!gameOver) {
	        currPlayer.makeMove(this);
	        if(isGameOver())
				setGameOver(true);
	        else
	        	switchPlayer();
	        repaint();
	    }
	}

	public void waitForHumanMove() {
	    // Add mouse listeners for human player input
	    this.addMouseListener(input);
	    this.addMouseMotionListener(input);

	    // Wait for a valid move to be made by the human player
	    while (!turnDone) {
	        // Sleep briefly to avoid consuming CPU resources unnecessarily
	        try {
	            Thread.sleep(100); // Adjust the sleep duration as needed
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	    // Remove mouse listeners after the move is made
	    this.removeMouseListener(input);
	    this.removeMouseMotionListener(input);
	    turnDone = false;
	}
	
	
	public void drawBoard(Graphics2D g) {
		// Draw the board squares
		for (int row = 0; row < Constants.DIMENSION; row++) {
			for (int col = 0; col < Constants.DIMENSION; col++) {
				// Determine color of the square
				Color squareColor, labelColor;
				if ((row + col) % 2 == 0) {
					squareColor = Constants.LIGHT_COLOR; // Use LIGHT_COLOR for even indices
					labelColor = Constants.DARK_COLOR;
				} else {
					squareColor = Constants.DARK_COLOR; // Use DARKER_COLOR for odd indices
					labelColor = Constants.LIGHT_COLOR;

				}
				g.setColor(squareColor);
				g.fillRect(col * Constants.SQ_SIZE, row * Constants.SQ_SIZE, Constants.SQ_SIZE, Constants.SQ_SIZE);

				// Draw column labels at the top right of each square
				if (row == Constants.DIMENSION-1) {
					g.setColor(labelColor); // Set color for labels
					g.setFont(font); // Set font for labels
					String colLabel = String.valueOf((char) ('a' + col));
					int colLabelX = (col + 1) * Constants.SQ_SIZE - 15; // X-coordinate for column labels
					int colLabelY = row * Constants.SQ_SIZE + 18; // Y-coordinate for column labels
					g.drawString(colLabel, colLabelX, colLabelY);
				}

				// Draw row labels at the top left of each square
				if (col == 0) {
					g.setColor(labelColor); // Set color for labels
					g.setFont(font); // Set font for labels
					String rowLabel = String.valueOf(row+1);
					int rowLabelX = 5; // X-coordinate for row labels
					int rowLabelY = row * Constants.SQ_SIZE + 18; // Y-coordinate for row labels
					g.drawString(rowLabel, rowLabelX, rowLabelY);
				}
			}
		}
	}



	public void drawPieces(Graphics2D g) {
	    Map<Integer, Piece> currentBoard = new HashMap<>(board.getPieces()); // Create a copy of the current board
	    for (Map.Entry<Integer, Piece> entry : currentBoard.entrySet()) {
	        Piece curr = entry.getValue();
	        curr.drawPiece(g);
	    }
	}


	public void showMoves(Piece currPiece, Graphics2D g) {
		int dotSize = 30; // Size of the dot
		int circleSize = 95; // Size of the circle

		// Draw the piece's picture

		g.setColor(Color.RED);
		for (Move move : currPiece.getPossibleMoves()) {
			int x = move.getEnd_col() * Constants.SQ_SIZE + Constants.SQ_SIZE / 2 - dotSize / 2;
			int y = move.getEnd_row() * Constants.SQ_SIZE + Constants.SQ_SIZE / 2 - dotSize / 2;


			// Check the kind of move
			if (move.getKind() == 'm') {
				// Paint a dot
				g.fillOval(x, y, dotSize, dotSize);
			} else {
				// Paint a circle around the piece
				g.setStroke(new BasicStroke(5)); // Increased thickness of the circle
				int circleX = move.getEnd_col() * Constants.SQ_SIZE + Constants.SQ_SIZE / 2 - circleSize / 2;
				int circleY = move.getEnd_row() * Constants.SQ_SIZE + Constants.SQ_SIZE / 2 - circleSize / 2;
				g.drawOval(circleX, circleY, circleSize, circleSize);
			}
		}

		// Fill the square where the piece is located with light red color
		g.setColor(new Color(210, 0, 0, 67)); // Light red color with alpha blending (semi-transparent)
		g.fillRect(currPiece.getCol() * Constants.SQ_SIZE, currPiece.getRow() * Constants.SQ_SIZE, Constants.SQ_SIZE, Constants.SQ_SIZE);
	}


	public void endGame(Graphics2D g, String color) {
		g.setColor(Color.RED); // Set the color to red
		Font font = new Font("Arial", Font.BOLD, 100); // Define the font (change the font name and size as needed)
		g.setFont(font); // Set the font
		String message = color+ " wins!!!"; // The message to be displayed
		int x = 145; // X-coordinate of the message (adjust as needed)
		int y = 280; // Y-coordinate of the message (adjust as needed)
		g.drawString(message, x, y); // Draw the message on the board
	}

	
	public void switchPlayer() {
		if(currPlayer == player1)
			setCurrPlayer(player2);
		else
			setCurrPlayer(player1);
	}
		
	
	public Board getBoard() {
		return board;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}
	

	public boolean isGameOver() {
		boolean bool1 = board.checkWin(currPlayer.getColor());
		switchPlayer();
		boolean bool2 = board.checkWin(currPlayer.getColor());
		if(bool1) {
			switchPlayer();
			return true;
		}
		if(bool2)
			return true;
		switchPlayer();
		return false;
	}
	
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	
	public Player getCurrPlayer() {
		return currPlayer;
	}
	
	
	public void setCurrPlayer(Player player) {
		this.currPlayer = player;
	}
}
