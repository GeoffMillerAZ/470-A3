import java.text.SimpleDateFormat;
import java.util.Calendar;

/***************************************************************
 * Author: Geoffrey Miller
 * Zid: z1644162
 * Due Date: Oct 13th 2013
 * Program 3
 * 
 * This is an object to keep track of stats for a particular game set.
 **************************************************************/

class GameStat {
    // Game start timestamp
    private String timeStamp;
    // win and loss counters
    private int compWin;
    private int userWin;
    private int numTies;
    // Move logs.............................R,.P,.S
    private int[] compMoves = new int[] { 0, 0, 0, 0 };
    private int[] userMoves = new int[] { 0, 0, 0, 0 };

    // Constructor
    public GameStat() {
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(
                " E MMM dd HH:mm:ss z yyyy");
        this.timeStamp = formatter.format(currentDate.getTime());
        this.compWin = 0;
        this.userWin = 0;
        this.numTies = 0;
    } // End Constructor

    // Getters - methods to access private methods of the GameStat
    String getTimeStamp() {
        return this.timeStamp;
    }

    int getCompWin() {
        return this.compWin;
    }

    int getUserWin() {
        return this.userWin;
    }

    int getNumTies() {
        return this.numTies;
    }

    int getCompMove(int input) {
        return this.compMoves[input];
    }

    int getUserMove(int input) {
        return this.userMoves[input];
    }

    // Setters for incrementing private members of GameStat
    void incCompWin() {
        this.compWin++;
    }

    void incUserWin() {
        this.userWin++;
    }

    void incNumTies() {
        this.numTies++;
    }

    void incCompMove(int input) {
        this.compMoves[input]++;
    }

    void incUserMove(int input) {
        this.userMoves[input]++;
    }

    // Determine and retrieve the string of the winner
    String getWinner() {
        String winner = "";
        if (this.userWin == this.compWin) {
            winner = "Tied Game!";
        } else if (this.userWin > this.compWin) {
            winner = "You!";
        } else if (this.userWin < this.compWin) {
            winner = "The computer.";
        }
        return winner;
    }

    // Prepare the final game session summary string
    String getStatSummary() {
        String summary = "* Summary for this competition:\n";
        summary += "* You have tried ";
        summary += this.getUserMove(1);
        summary += " rock(s), ";
        summary += this.getUserMove(2);
        summary += " paper, ";
        summary += this.getUserMove(3);
        summary += " scissors. You won ";
        summary += this.getUserWin();
        summary += " time(s).\n";

        summary += "* The computer has tried ";
        summary += this.getCompMove(1);
        summary += " rock(s), ";
        summary += this.getCompMove(2);
        summary += " paper, ";
        summary += this.getCompMove(3);
        summary += " scissors. The computer won ";
        summary += this.getCompWin();
        summary += " time(s).\n";

        summary += "* Number of ties: ";
        summary += this.getNumTies();
        summary += ".\n* Winner: ";
        summary += this.getWinner();
        summary += "\n";

        return summary;
    }

}