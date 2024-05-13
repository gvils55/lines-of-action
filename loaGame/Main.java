package loaGame;


import javax.swing.JFrame;

public class Main {
	
	public static void main(String[] args) {
        JFrame window = new JFrame("Lines Of Action");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Game game = new Game();
        
        // Create player objects
        Player player1 = new HumanPlayer("white");
        Player player2 = new MachinePlayer("black");
        //HumanPlayer
        //MachinePlayer
        // Set the players in the game
        game.setPlayers(player1, player2);
        
        window.add(game);
        
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        // Start the game loop after the window is visible
        game.startGame();
    }
}


