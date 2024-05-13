package loaGame;

public abstract class Player {
	protected String color;
	protected boolean yourTurn;


	public Player(String color) {
		this.color = color;
		if(this.color == "white")
			this.yourTurn = true;
		else
			this.yourTurn = false;
	}

	public boolean isYourTurn() {
		return yourTurn;
	}

	public void setYourTurn(boolean yourTurn) {
		this.yourTurn = yourTurn;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public abstract void makeMove(Game game); 
}
