package loaGame;


public class Move {
	private int start_row;
	private int start_col;
	private int end_row;
	private int end_col;
	private char kind;
	
	
	public Move(int start_row, int start_col, int end_row, int end_col, char kind) {
		this.start_row = start_row;
		this.start_col = start_col;
		this.end_row = end_row;
		this.end_col = end_col;
		this.kind = kind;
	}


	public int getStart_row() {
		return start_row;
	}


	public void setStart_row(int start_row) {
		this.start_row = start_row;
	}


	public int getStart_col() {
		return start_col;
	}


	public void setStart_col(int start_col) {
		this.start_col = start_col;
	}


	public int getEnd_row() {
		return end_row;
	}


	public void setEnd_row(int end_row) {
		this.end_row = end_row;
	}


	public int getEnd_col() {
		return end_col;
	}


	public void setEnd_col(int end_col) {
		this.end_col = end_col;
	}


	public char getKind() {
		return kind;
	}


	public void setKind(char kind) {
		this.kind = kind;
	}
	
	
}

