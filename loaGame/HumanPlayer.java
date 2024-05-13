package loaGame;

public class HumanPlayer extends Player{

	
	public HumanPlayer(String color) {
		super(color);
		// TODO Auto-generated constructor stub
	}
	
	public void makeMove(Game game) {
		game.waitForHumanMove();
		
	}


}
