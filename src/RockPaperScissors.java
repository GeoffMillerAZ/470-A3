/***************************************************************
 * Author: Geoffrey Miller 
 * Zid: z1644162 
 * Due Date: Oct 13th 2013
 * Program 3
 * link: http://students.cs.niu.edu/~z1644162/java/a3/index.html
 * 
 * Notes: This contains the class RockPaperScissors. This class
 * creates a GUI for the RockPaperScissors game created in the
 * last assignment.
 **************************************************************/
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RockPaperScissors extends Applet {

    /**
     * Declaration of important GUI elements
     */
    private static final long serialVersionUID = 1L;
    // used to display the user's rock count for this game
    private JTextField txtYouRock;
    // used to display the PC's rock count for this game
    private JTextField txtPCRock;
    // used to display the user's paper count for this game
    private JTextField txtYouPaper;
    // used to display the user's scissors count for this game
    private JTextField txtYouScissors;
    // used to display the PC's paper count for this game
    private JTextField txtPCPaper;
    // used to display the PC's scissors count for this game
    private JTextField txtPCScissors;
    // used to display the tie count for this game
    private JTextField txtStatNumTies;
    // used to display the winner for this game
    private JTextField txtStatWinner;
    // used to group the radio buttions for move selection
    private final ButtonGroup buttonGroup = new ButtonGroup();
    // used to print game action as it occurs
    private JTextPane txtGameAction;
    // used to start a new game
    private JButton btnStartNewGame;
    // radio buttons to select intended move
    private JRadioButton rdbtnRock;
    private JRadioButton rdbtnPaper;
    private JRadioButton rdbtnScissors;
    // button used to enter selected move
    private JButton btnGo;
    // listbox used to display a list of the game history
    private JList lstCompletions;
    private JLabel lblCompletions;

    // model will be used as the model for the lstCompletions list
    private DefaultListModel model = new DefaultListModel();
    // This array list will be used to keep track of each instance of
    // the games that run. Games are created dynamically and stored in a
    // hashmap.
    private HashMap<Integer, RPSGame> gameMap = new HashMap<Integer, RPSGame>();
    // the count of the current game
    private Integer cnt = 0;

    // Called when this applet is loaded into the browser.
    public void init() {
        // Execute a job on the event-dispatching thread; creating this applet's
        // GUI.
        try {
            this.setLayout(new BorderLayout());
            this.setSize(new Dimension(800, 350));
            this.setMaximumSize(new Dimension(800, 350));
            this.setMinimumSize(new Dimension(800, 350));

            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    setupGUI();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't complete successfully");
            e.printStackTrace();
        }
    }

    /*
     * Setup event handlers for different GUI elements
     */
    public void EventHandlers() {
        /****************************************************
         * EVENT HANDLER
         * For: Start New Game Button
         * Action: Left Mouse Click
         * Notes: Used to start a new game and update
         * lstCompletions listbox to show game history
         ****************************************************/
        btnStartNewGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtGameAction.setText("New Game Started!\n");
                // Enable Radial buttons at start of new game
                rdbtnRock.setEnabled(true);
                rdbtnPaper.setEnabled(true);
                rdbtnScissors.setEnabled(true);
                // Reset Radial button selection from last move
                rdbtnRock.setSelected(false);
                rdbtnPaper.setSelected(false);
                rdbtnScissors.setSelected(false);
                buttonGroup.clearSelection();
                // Enable Go button at start of new game
                btnGo.setEnabled(false);
                // Update lstCompletions and stat textboxes with game stats
                // unless number of games played is 0
                if (gameMap.size() != 0) {
                    GameStat thisGame = gameMap.get(gameMap.size() - 1).thisGameStats;
                    model.addElement(thisGame.getTimeStamp());
                    lstCompletions.setSelectedIndex(model.size() - 1);
                }
                // Start a New Game
                gameMap.put(cnt++, new RPSGame());
            }
        });
        /****************************************************
         * EVENT HANDLER
         * For: Go Button
         * Action: Left Mouse Click
         * Notes: Enters the user's selected move.
         ****************************************************/
        btnGo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // if the button is disabled don't do anything
                if (btnGo.isEnabled()) {
                    // Disable Go button for next turn
                    // (see radial buttons state changed event handlers
                    // for enabling Go button)
                    btnGo.setEnabled(false);
                    // Start a playOnceGUI for this game and pass the player
                    // move
                    RPSGame thisGame = gameMap.get(gameMap.size() - 1);
                    if (rdbtnRock.isSelected()) {
                        thisGame.playOnceGui(1, txtGameAction);
                    } else if (rdbtnPaper.isSelected()) {
                        thisGame.playOnceGui(2, txtGameAction);
                    } else if (rdbtnScissors.isSelected()) {
                        thisGame.playOnceGui(3, txtGameAction);
                    }
                    // Reset Radial button selection from last move
                    rdbtnRock.setSelected(false);
                    rdbtnPaper.setSelected(false);
                    rdbtnScissors.setSelected(false);
                    buttonGroup.clearSelection();
                } else if (gameMap.size() == 0) {
                    JOptionPane
                            .showMessageDialog(null,
                                    "No game has been started! Click 'Start new game' to begin.");
                } else {
                    JOptionPane.showMessageDialog(null,
                            "No move selected! Select a move, then hit Go.");
                }
            }
        });
        /****************************************************
         * EVENT HANDLERS
         * For: All 3 radial buttons (Rock, Paper, Scissors)
         * Action: Left Mouse Click
         * Notes: These event handlers enable the go button once
         * a selection is made so that the go button cannot be
         * activated unless the user has selected a move.
         ****************************************************/
        rdbtnRock.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (rdbtnRock.isEnabled()) {
                    btnGo.setEnabled(true);
                }
            }
        });
        rdbtnPaper.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (rdbtnPaper.isEnabled()) {
                    btnGo.setEnabled(true);
                }
            }
        });
        rdbtnScissors.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (rdbtnScissors.isEnabled()) {
                    btnGo.setEnabled(true);
                }
            }
        });
        /****************************************************
         * EVENT HANDLER
         * For: List Box (lstCompletions)
         * Action: Selection Changed
         * Notes: This action sets all of the stat boxes on
         * the left of the app to their respective values based
         * on the selected item in the listbox.
         ****************************************************/
        lstCompletions.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                GameStat thisGame = gameMap.get(lstCompletions
                        .getSelectedIndex()).thisGameStats;
                txtPCRock.setText(Integer.toString(thisGame.getCompMove(1)));
                txtPCPaper.setText(Integer.toString(thisGame.getCompMove(2)));
                txtPCScissors.setText(Integer.toString(thisGame.getCompMove(3)));
                txtYouRock.setText(Integer.toString(thisGame.getUserMove(1)));
                txtYouPaper.setText(Integer.toString(thisGame.getUserMove(2)));
                txtYouScissors.setText(Integer.toString(thisGame.getUserMove(3)));
                txtStatNumTies.setText(Integer.toString(thisGame.getNumTies()));
                txtStatWinner.setText(thisGame.getWinner());
            }
        });
    }
    
    /****************************************************
     * setupGUI
     * 
     * This method initializes all important GUI objects
     * in RockPaperScissors and adds them to the applet.
     ****************************************************/
    public void setupGUI(){
     // initialize txtboxes and labels
        txtStatNumTies = new JTextField(3);
        txtStatWinner = new JTextField(3);
        txtPCRock = new JTextField(3);
        txtPCPaper = new JTextField(3);
        txtPCScissors = new JTextField(3);
        txtYouRock = new JTextField(3);
        txtYouPaper = new JTextField(3);
        txtYouScissors = new JTextField(3);
        JLabel lblRock = new JLabel("Rock");
        JLabel lblPaper = new JLabel("Paper");
        JLabel lblScissors = new JLabel("Scissors");
        JLabel lblYou = new JLabel("You");
        JLabel lblPC = new JLabel("PC");
        JLabel lblTies = new JLabel("# Ties");
        JLabel lblWinner = new JLabel("Winner");
        JLabel lblBlank = new JLabel("");
        lblCompletions = new JLabel("List of Completions");
        lblCompletions.setAlignmentX(LEFT_ALIGNMENT);

        
        btnGo = new JButton("Go");
        btnStartNewGame = new JButton("Start new game");
        btnGo.setAlignmentX(LEFT_ALIGNMENT);
        btnStartNewGame.setAlignmentX(CENTER_ALIGNMENT);
        lstCompletions = new JList<>(model);
        lstCompletions.setFixedCellWidth(250);
        lstCompletions.setVisibleRowCount(8);
        Box box1 = Box.createHorizontalBox();
        box1.setAlignmentX(LEFT_ALIGNMENT);
        rdbtnRock = new JRadioButton("Rock");
        rdbtnRock.setAlignmentX(LEFT_ALIGNMENT);
        rdbtnPaper = new JRadioButton("Paper");
        rdbtnPaper.setAlignmentX(LEFT_ALIGNMENT);
        rdbtnScissors = new JRadioButton("Scissors");

        rdbtnScissors.setAlignmentX(LEFT_ALIGNMENT);
        buttonGroup.add(rdbtnRock);
        buttonGroup.add(rdbtnPaper);
        buttonGroup.add(rdbtnScissors);
        btnGo = new JButton("Go!");
        btnGo.setAlignmentX(LEFT_ALIGNMENT);
        txtGameAction = new JTextPane();
        txtGameAction.setSize(250, 250);
        txtGameAction.setMaximumSize(new Dimension(700, 250));
        JScrollPane gameActionScroller = new JScrollPane(txtGameAction);

        JPanel westPane = new JPanel();
        westPane.setLayout(new BoxLayout(westPane,
                BoxLayout.PAGE_AXIS));
        westPane.setBorder(BorderFactory.createEmptyBorder(0, 10,
                10, 10));
        lblCompletions.setAlignmentX(LEFT_ALIGNMENT);

        westPane.add(lblCompletions);
        JScrollPane listScroller = new JScrollPane(lstCompletions);
        westPane.add(listScroller);

        JPanel eastPane = new JPanel();
        eastPane.setSize(new Dimension(5000, 5000));
        eastPane.setLayout(new BoxLayout(eastPane,
                BoxLayout.PAGE_AXIS));
        eastPane.setBorder(BorderFactory.createEmptyBorder(0, 10,
                10, 10));
        eastPane.add(btnStartNewGame);
        box1.add(rdbtnRock);
        box1.add(rdbtnPaper);
        box1.add(rdbtnScissors);
        box1.add(btnGo);
        eastPane.add(box1);
        eastPane.add(gameActionScroller);

        GridLayout statsGrid = new GridLayout(0, 4);
        GridLayout statsGrid2 = new GridLayout(0, 2);
        JPanel statsPane = new JPanel(statsGrid);

        statsPane.add(lblBlank);
        statsPane.add(lblRock);
        statsPane.add(lblPaper);
        statsPane.add(lblScissors);
        statsPane.add(lblYou);
        statsPane.add(txtYouRock);
        statsPane.add(txtYouPaper);
        statsPane.add(txtYouScissors);
        statsPane.add(lblPC);
        statsPane.add(txtPCRock);
        statsPane.add(txtPCPaper);
        statsPane.add(txtPCScissors);

        JPanel statsSubPane = new JPanel(statsGrid2);
        statsSubPane.add(lblTies);
        statsSubPane.add(txtStatNumTies);
        statsSubPane.add(lblWinner);
        statsSubPane.add(txtStatWinner);

        westPane.add(statsPane);
        westPane.add(statsSubPane);

        add(westPane, BorderLayout.LINE_START);
        add(eastPane, BorderLayout.CENTER);

        EventHandlers();

        // go is disabled by default to make the user start a game
        // first
        btnGo.setEnabled(false);
        rdbtnRock.setEnabled(false);
        rdbtnPaper.setEnabled(false);
        rdbtnScissors.setEnabled(false);
    }
}
