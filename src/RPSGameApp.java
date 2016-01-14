import java.util.ArrayList;
import java.util.Scanner;

/***************************************************************
 * Author: Geoffrey Miller
 * Zid: z1644162
 * Due Date: Oct 13th 2013
 * Program 3
 * 
 * This class runs the entire game session. It creates an instance of game for
 * each set. It stores the stats for each set and prints a summary of the set
 * statistics at the end of the game session.
 **************************************************************/
class RPSGameApp {
    public static void main(String args[]) {
        boolean isAnotherSet = false;
        String temp = "";
        Scanner reader = new Scanner(System.in);
        ArrayList<String> sessionStats = new ArrayList<String>();
        ArrayList<RPSGame> gameList = new ArrayList<RPSGame>();
        String repeatChoice = "";
        // ListIterator<RPSGame> iterator = gameList.listIterator();

        System.out.println("Welcome to the Rock-paper-scissors Game!");

        do {
            System.out.println("\n" + "Let us start a competition: ");
            // Instantiate a new game and add it to the session list.
            gameList.add(new RPSGame());
            // Run the game until the user indicates to stop game set
            while (gameList.get(gameList.size() - 1).playOnce(reader))
                ;

            // print stats for this game set
            System.out.print(gameList.get(gameList.size() - 1).thisGameStats
                    .getStatSummary());

            // add game set stats to the game session stats
            temp = gameList.get(gameList.size() - 1).thisGameStats
                    .getTimeStamp();
            temp += ": Winner-- ";
            temp += gameList.get(gameList.size() - 1).thisGameStats.getWinner();
            sessionStats.add(temp);

            // Check to see if the user wants to complete another set.
            // Repeat until the user gives valid input.
            do {
                System.out.print("Do you want another run of competition? ");
                repeatChoice = reader.next().toLowerCase();
                if (repeatChoice.equalsIgnoreCase("y"))
                    isAnotherSet = true;
                else if (repeatChoice.equalsIgnoreCase("n"))
                    isAnotherSet = false;
                else
                    System.out
                            .println("Invalid option! Please enter y or n to repeat the game.");
            } while (!repeatChoice.equalsIgnoreCase("y")
                    && !repeatChoice.equalsIgnoreCase("n"));

        } while (isAnotherSet);
        // This is where I print all of the games stats
        for (int i = 0; i < sessionStats.size(); i++) {
            System.out.println(sessionStats.get(i));
        }
    }
}