package loaGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Piece {
	private int row;
	private int col;
	private List<Move> possibleMoves;
	private String color;
	private String shape;
	private BufferedImage image;

	
	
	public Piece(int row, int col, String color) {
		this.row = row;
		this.col = col;
		this.color = color;
		this.possibleMoves = new ArrayList<Move>();
		this.shape = shape_init();
		loadImage(shape);
		
	}

	public String shape_init() {
	    String baseDirectory = "C:\\Users\\Owner\\eclipse-workspace\\Ort-proj\\images";
	    // Combine the base directory with the color and piece filename
	    Path imagePath = Paths.get(baseDirectory, color + "_piece.png");
	    // Convert the Path object to a String
	    String shape = imagePath.toString();
	    //System.out.println(imagePath);
	    //System.out.println(shape);

	    return shape;
	}

	public void loadImage(String imagePath) {
	    try {
	        File file = new File(imagePath);
	        if (file.exists()) {
	            image = ImageIO.read(file);
	        } else {
	            System.err.println("File does not exist: " + imagePath);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	public int getCol() {
		return col;
	}


	public void setCol(int col) {
		this.col = col;
	}


	public List<Move> getPossibleMoves() {
		return possibleMoves;
	}
	
	public int getX() {
		return col*Constants.SQ_SIZE;
	}
	
	public int getY() {
		return row*Constants.SQ_SIZE;
	}
	
	public String getShape() {
		return this.shape;
	}
	
	public void drawPiece(Graphics2D g) {
		if (image != null) 
			g.drawImage(image, getX(), getY(), Constants.SQ_SIZE, Constants.SQ_SIZE, null);
		
		else {
			if(color == "black")
				g.setColor(Color.BLACK);
			else
				g.setColor(Color.WHITE);
			g.fillOval(getX(), getY(), Constants.SQ_SIZE, Constants.SQ_SIZE);
		}
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public void addMove(Move move) {
		possibleMoves.add(move);
	}
	
	public Move getMove(int row, int col) {
		 for(Move move: possibleMoves) {
			 if(move.getEnd_row() == row && move.getEnd_col() == col)
				 return move;
		 }
		 return null;
	}
	
	public void clearMoves() {
		possibleMoves.clear();
	}
}
