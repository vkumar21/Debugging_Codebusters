import java.util.List;
import java.io.*;

public class Main {
	
	public static void main(String[] args) throws Exception {
	   int player_age;
	   BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        Dice d1 = new Dice();
        Dice d2 = new Dice();
        Dice d3 = new Dice();

        // Code should be added here asking the player to input his age
        while(true){
        	System.out.print("Enter your age: ");
        	try{
        		player_age = Integer.parseInt(console.readLine());
        		if(player_age > 0 && player_age < 100)
        			break; // Valid age entered
        	}catch(Exception e){
        		// Invalid age
        		System.out.println("Invalid age! Please try again!");
        	}
        	}
        // Once age is read, it will be checked and the game will only continue if player's age greater than 18
        if(player_age < 18){
        	// Player is too young, game is not allowed to start
        	System.out.println("Player under 18 is not allowed to play the game. Terminate now!");
        	return;
        }
       // Bug 7: Player should be allowed to enter his name and initialize his balance here
        System.out.print("Enter your name: ");
        String playerName = console.readLine();
        int playerBalance = 100;
        System.out.print("How much is your balance: ");
        try{
        	playerBalance = Integer.parseInt(console.readLine());
        	if(playerBalance < 0){
        		playerBalance = 100;
        		throw new Exception();
        	}
        }catch(Exception e){
        	System.out.println("Invalid balance! Balance now set to " + playerBalance);
        }
        Player player = new Player(playerName, playerBalance);
        // Bug 8: Bet amount should be asked here
        // Bug 8: Min bet and max bet also notified here
        System.out.print("How much do you want to bet(Min: 5; Max: 50): ");
        //Default bet = 5
        int bet = 5;
        try{
        	bet = Integer.parseInt(console.readLine());
        	if(bet < 5 || bet > 50){
        		bet = 5;
        		throw new Exception();
        	}
        }catch(Exception e){
        	System.out.println("Invalid bet! Bet set to " + bet);
        }
        // Bug 9: Number of times to play should be chosen by the player base on his interest. 
        // Bug 9: It should be from 1 to 100, 5 by default
        int num_of_play = 5;
        System.out.print("How many times do you want to play the game?: ");
        try{
        	num_of_play = Integer.parseInt(console.readLine());
        	if(num_of_play < 1 || num_of_play > 100){
        		num_of_play = 5;
        		throw new Exception();
        	}
        }catch(Exception e){
        	System.out.println("Invalid number! Game plays 5 times by default!");
        }
        Game game = new Game(d1, d2, d3);
        List<DiceValue> cdv = game.getDiceValues();

        int totalWins = 0;
        int totalLosses = 0;

        while (true)
        {
            int winCount = 0;
            int loseCount = 0;
            
            for (int i = 0; i < num_of_play; i++)
            {
            	String name = playerName;
            	int balance = playerBalance;
            	int limit = 0;
                player = new Player(name, balance);
                player.setLimit(limit);

                System.out.println(String.format("Start Game %d: ", i));
                System.out.println(String.format("%s starts with balance %d, limit %d", 
                		player.getName(), player.getBalance(), player.getLimit()));

                int turn = 0;
                while (player.balanceExceedsLimitBy(bet) && player.getBalance() < 200)
                {
                    turn++;                    
                	DiceValue pick = DiceValue.getRandom();
                   
                	System.out.printf("Turn %d: %s bet %d on %s\n",
                			turn, player.getName(), bet, pick); 
                	
                	// Testing incorrect balance increase on winning
                	//System.out.println("----- Main.java: Start calculating winnings..." );
                	//System.out.println("----- Main.java: Balance before play: " + player.getBalance());
                
                	// Bug 3: DiceValues remain the same all the time
                	//System.out.println("--- BUG 3 detector: old dice values: " + cdv.get(0) + "," + cdv.get(1) + "," + cdv.get(2));
                	int winnings = game.playRound(player, pick, bet);
                	//System.out.println("----- Main.java: Winning amount: " + winnings);
                	//System.out.println("----- Main.java: Balance after play: " + player.getBalance());
                	
                    cdv = game.getDiceValues();
                    //System.out.println("--- BUG 3 detector: new dice values: " + cdv.get(0) + "," + cdv.get(1) + "," + cdv.get(2));
                    
                    System.out.printf("Rolled %s, %s, %s\n",
                    		cdv.get(0), cdv.get(1), cdv.get(2));
                    
                    if (winnings > 0) {
	                    System.out.printf("%s won %d, balance now %d\n\n",
	                    		player.getName(), winnings, player.getBalance());
	                	winCount++; 
                    }
                    else {
	                    System.out.printf("%s lost, balance now %d\n\n",
	                    		player.getName(), player.getBalance());
	                	loseCount++;
                    }
                    //code added to test the loop of it continues when balance still positive
                    //System.out.println("---- Debugging: Now balance: " + player.getBalance() + ". Bet: " + bet);
                    //System.out.println("---- Does balance exceeds limit by " + bet + "?: " + player.balanceExceedsLimitBy(bet));
                    //System.out.println("---- Is balance less than 200?: " + (player.getBalance() < 200));
                    //System.out.println("---- Will the loop continue?: " + (player.balanceExceedsLimitBy(bet) && player.getBalance()<200));
                  
                } //while

                System.out.print(String.format("%d turns later.\nEnd Game %d: ", turn, i));
                System.out.println(String.format("%s now has balance %d\n", player.getName(), player.getBalance()));
                
            } //for
            
            System.out.println(String.format("Win count = %d, Lose Count = %d, %.2f", winCount, loseCount, (float) winCount/(winCount+loseCount)));
            totalWins += winCount;
            totalLosses += loseCount;

            String ans = console.readLine();
            if (ans.equals("q")) break;
        } //while true
        
        System.out.println(String.format("Overall win rate = %.1f%%", (float)(totalWins * 100) / (totalWins + totalLosses)));
	}

}