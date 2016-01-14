import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

/***************************************************************
 * Author: Geoffrey Miller
 * Zid: z1644162
 * Due Date: Oct 13th 2013
 * Program 3
 * 
 * This class handles an individual game set. Each game is run one game at a
 * time and when the user indicates that the set is complete then the game
 * prints the final game stats and determines a winner.
 **************************************************************/
class RPSGame {
    // String winner;
    GameStat thisGameStats;
    int trialNum;
    public int passer; // This is used for passing values between methods
    ArrayList<Integer> userHistory;
    int[] lastThree;
    // Stores the names of moves in the order of the index as it corresponds
    // to the user input prompts
    String[] moveMap = new String[] { "None", "Rock", "Paper", "Scissors" };
    // This is a lookup table that represents the outcomes from possible moves
    // Positive number indicates user win. negative is PC win. 0 is a tie.
    int[][] matchUp = new int[][] { { 0, 0, 0, 0 }, { 0, 0, 1, -1 },
            { 0, -1, 0, 1 }, { 0, 1, -1, 0 } };

    // Constructor
    public RPSGame() {
        this.thisGameStats = new GameStat();
        this.trialNum = 0;
        this.userHistory = new ArrayList<Integer>();
        this.lastThree = new int[] { 0, 0, 0 };
    } // end constructor

    /***************************************************************
     * Method: playOnceGui
     * Arguments:
     ***** int - representing the user's move
     ***** JTextPane - the address of the textpane for printing.
     * Notes: This method runs through one pass of the current
     * game and runs through the GUI interface.
     ***************************************************************/
    public void playOnceGui(int userMove, JTextPane txtGameAction) {
        int userMoveChoice = userMove;
        int pcMoveChoice = 0;

        pcMoveChoice = getComputerGesture(++this.trialNum, userMoveChoice);

        // increment move histories/stats
        this.thisGameStats.incCompMove(pcMoveChoice);
        this.thisGameStats.incUserMove(userMoveChoice);
        this.userHistory.add(userMoveChoice);

        // Prompt user of Gesture selections
        txtGameAction.setText(txtGameAction.getText() + "Go! Your gesture is "
                + this.moveMap[userMoveChoice]);
        txtGameAction.setText(txtGameAction.getText() + ". My gesture is "
                + this.moveMap[pcMoveChoice] + ".\n");

        // Determine and print game winner
        if (userMoveChoice == pcMoveChoice) {
            txtGameAction.setText(txtGameAction.getText() + " A tie!\n");
            this.thisGameStats.incNumTies();
        }
        // Checks the lookup table for possible match outcomes
        else if (matchUp[pcMoveChoice][userMoveChoice] > 0) {
            txtGameAction.setText(txtGameAction.getText() + " You win!\n");
            this.thisGameStats.incUserWin();
        } else {
            txtGameAction.setText(txtGameAction.getText() + " I win!\n");
            this.thisGameStats.incCompWin();
        }
    }

    /***************************************************************
     * Method: playOnceGui
     * Arguments: The scanner object used for input
     * Notes: This method runs through one pass of the current
     * game and runs through the console interface.
     ***************************************************************/
    public boolean playOnce(Scanner reader) {
        boolean isContinue = true;
        int userMoveChoice = 0;
        int pcMoveChoice = 0;

        // this do/while ensures a valid entry from the user
        do {
            System.out.print("Please pick a gesture, 1 for rock; 2 "
                    + "for paper; 3 for scissors; 4 ends this run "
                    + "of competition: ");
            try {
                userMoveChoice = reader.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid entry. Please try again.");
                reader.next();
                continue;
            }

            if (userMoveChoice < 1 || userMoveChoice > 4)
                System.out.println("Invalid entry. Please try again.");
        } while (userMoveChoice < 1 || userMoveChoice > 4);

        if (userMoveChoice == 4)
            isContinue = false;
        // This is where the program continues if there is valid input.
        else {
            pcMoveChoice = getComputerGesture(++this.trialNum, userMoveChoice);

            // increment move histories/stats
            this.thisGameStats.incCompMove(pcMoveChoice);
            this.thisGameStats.incUserMove(userMoveChoice);
            this.userHistory.add(userMoveChoice);

            // Prompt user of Gesture selections
            System.out.print("Go! Your gesture is "
                    + this.moveMap[userMoveChoice]);
            System.out.print(". My gesture is " + this.moveMap[pcMoveChoice]
                    + ".");

            // Determine and print game winner
            if (userMoveChoice == pcMoveChoice) {
                System.out.print(" A tie!\n");
                this.thisGameStats.incNumTies();
            }
            // Checks the lookup table for possible match outcomes
            else if (matchUp[pcMoveChoice][userMoveChoice] > 0) {
                System.out.print(" You win!\n");
                this.thisGameStats.incUserWin();
            } else {
                System.out.print(" I win!\n");
                this.thisGameStats.incCompWin();
            }
        }
        return isContinue;
    }

    // Determines the computers move. The first 5 moves are random and
    // every fifth move is random. If the computer can see a common pattern
    // in the user's search history, it will try to predict the optimal move.
    public int getComputerGesture(int trialNum, int userInput) {
        int compsGesture = 0;
        // case for first 5 and every 5th.
        if (trialNum < 5 || trialNum % 5 == 0) {
            compsGesture = (int) (Math.random() * 3 + 1);
        }
        // check to see if move history shows a patter. and if so capture
        // the optimal counter.
        else if (searchHistory()) {
            for (int i = 1; i < 4; i++) {
                if (this.matchUp[i][userInput] < 0) {
                    compsGesture = i;
                    break;
                }
            }
        }
        // Otherwise, go with a random choice.
        else {
            compsGesture = (int) (Math.random() * 3 + 1);
        }
        return compsGesture;
    }

    // Searches the history of the user for common patterns to predict
    // the optimal move.
    Boolean searchHistory() {
        Boolean status = false;

        this.lastThree[0] = this.userHistory.get(this.userHistory.size() - 3);
        this.lastThree[1] = this.userHistory.get(this.userHistory.size() - 2);
        this.lastThree[2] = this.userHistory.get(this.userHistory.size() - 1);

        for (int i = 0; i < this.userHistory.size() - 3; i++) {
            if (this.userHistory.get(i) == this.lastThree[0]
                    && this.userHistory.get(i + 1) == this.lastThree[1]
                    && this.userHistory.get(i + 2) == this.lastThree[2]) {
                this.passer = this.userHistory.get(i + 3);
                status = true;
                break;
            }
        }
        return status;
    }
}