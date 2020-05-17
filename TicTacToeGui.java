import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.imageio.ImageIO;
import java.io.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;


/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game in a very
 * enhanced GUI window. 
 * 
 * Bonus features: switch the player that goes first, graphic buttons
 * 
 * @author Ishanov Sahni
 * @version November 25, 2019
 */

public class TicTacToeGui
{ 
   public static final String PLAYER_X = "X"; // player using "X"
   public static final String PLAYER_O = "O"; // player using "O"
   public static final String EMPTY = " ";  // empty cell
   public static final String TIE = "T"; // game ended in a tie

   private String player = PLAYER_X;   // current player (PLAYER_X or PLAYER_O)

   private String winner;   // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress

   private int numFreeSquares; // number of squares still free
   
   private String board[][]; // 3x3 array representing the board
   private int Xwins;
   private int Owins;
   private int gamesplayed;
   
   // images for x, o, and empty slots
   private static Image xImg; 
   private static Image oImg;
   private static Image emptyImg;
   
   // 9 buttons as the slots 
   JButton buttons[] = new JButton[9];
   
   // the tic tac toe panel
   JPanel panel = new JPanel();
   //status of the game
   JLabel label = new JLabel("Start the game. "+player+" goes first. \n X wins = " +Xwins+" O wins = "+Owins+" Games Played = "+gamesplayed);
   /** 
    * Constructs a new Tic-Tac-Toe board and sets up the basic
    * JFrame containing a JTextArea in a JScrollPane GUI.
    */
   public TicTacToeGui() throws java.io.IOException
   { 
       //structuring the panel 
       panel.setPreferredSize(new Dimension(700, 725));
       panel.setLayout(new GridLayout(3,3));
       //create the board and add the buttons
       board = new String[3][3];
       addButtons();
       
       //the overall frame holding everything
       JFrame window = new JFrame("Tic Tac Toe: Avatar best anime ever dont at me");
       
       JMenuBar menubar = new JMenuBar();
       window.setJMenuBar(menubar); // add menu bar to our frame

       JMenu gameMenu = new JMenu("Game"); // create a menu
       menubar.add(gameMenu); // and add to our menu bar
           
       JMenuItem newItem = new JMenuItem("New"); //create a new game: menu item
       gameMenu.add(newItem);
       
       JMenu switchheader = new JMenu("Pick who goes first"); //Submenu of who goes first
  
       JMenuItem switchO = new JMenuItem("O goes first"); //switch the player that goes first to O
       gameMenu.add(switchO);
       
       JMenuItem switchX = new JMenuItem("X goes first"); //switch the player that goes first to X
       gameMenu.add(switchX);
       
       switchheader.add(switchO); // Adds Switch to O going first to submenu
       switchheader.add(switchX); // Adds switch to X going first to submenu
       gameMenu.add(switchheader); // Adds submenu to the menu
       
       JMenuItem resetscore = new JMenuItem("Reset the Game"); //Reset the game
       gameMenu.add(resetscore);
       
       JMenuItem toonz = new JMenuItem("Bump Some bangers"); //Reset the game
       gameMenu.add(toonz);
       
       JMenuItem quitItem = new JMenuItem("Quit"); //quit game: menu item
       gameMenu.add(quitItem);
       
       //creating the shortcuts for the menu items
       final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); // to save typing
       newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
       quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));       
       //linking the images to the files
       xImg = ImageIO.read(getClass().getResource("x.jpg"));
       oImg = ImageIO.read(getClass().getResource("o.jfif"));
       emptyImg = ImageIO.read(getClass().getResource("empty.png"));
       
       //initialize the tic tac toe board and its state
       clearBoard();
       
       // listen for menu selections
       quitItem.addActionListener(new ActionListener() // create an anonymous inner class
         { // start of anonymous subclass of ActionListener
          // this allows us to put the code for this action here  
            public void actionPerformed(ActionEvent event)
            {
              System.exit(0); // quit                
            }
        } // end of anonymous subclass
      ); // end of addActionListener parameter list and statement
      
      // listen for menu selections
       resetscore.addActionListener(new ActionListener() // create an anonymous inner class
         { // start of anonymous subclass of ActionListener
          // this allows us to put the code for this action here  
            public void actionPerformed(ActionEvent event)
            {
                Xwins = 0;
                Owins = 0;
                gamesplayed = 0;
                player = PLAYER_X;
                clearBoard(); // clear board 
            }
        } // end of anonymous subclass
      ); // end of addActionListener parameter list and statement
      
       newItem.addActionListener(new ActionListener() // create an anonymous inner class
        { // start of anonymous subclass of ActionListener
          // this allows us to put the code for this action here  
            public void actionPerformed(ActionEvent event)
            {
                clearBoard();
                player = PLAYER_X;
                 //If you wanted player X to start all the time this is how you would start a new game where player X would start everytime
                
                /* wasnt sure if you wanted it to alternate playesrs so that if player X went first last game, plater O starts this game nevertheless this code has been implemented for possible bonus marks
                if(player == PLAYER_X) {
                    player = PLAYER_O;
                    
                    clearBoard();
                } else if (player == PLAYER_O){
                    player = PLAYER_X;
                    
                    clearBoard();
                }; 
                */
            }//new game
        } // end of anonymous subclass
      ); // end of addActionListener parameter list and statement
      
       switchO.addActionListener(new ActionListener() // create an anonymous inner class
        { // start of anonymous subclass of ActionListener
          // this allows us to put the code for this action here  
            public void actionPerformed(ActionEvent event)
            {
                // alternate player goes first from before, menu item text switches
                player = PLAYER_O;
                JOptionPane.showMessageDialog(null, "O goes first now.");
                clearBoard();
            }
        }); // end of anonymous subclass
        
        switchX.addActionListener(new ActionListener() // create an anonymous inner class
        { // start of anonymous subclass of ActionListener
          // this allows us to put the code for this action here  
            public void actionPerformed(ActionEvent event)
            {
               // alternate player goes first from before, menu item text switches
               player = PLAYER_X;
               JOptionPane.showMessageDialog(null, "X goes first now.");
               clearBoard();
               
            }
        } // end of anonymous subclass
      ); // end of addActionListener parameter list and statement
      
             // listen for menu selections
       toonz.addActionListener(new ActionListener() // create an anonymous inner class
         { // start of anonymous subclass of ActionListener
          // this allows us to put the code for this action here  
            public void actionPerformed(ActionEvent event)
            {
               playSound("bangerz.wav"); //BUMP SOME TRACKAZ            
            }
        } // end of anonymous subclass
       ); // end of addActionListener parameter list and statement
      
      
       // add a label
       label.setHorizontalAlignment(JLabel.CENTER); // center justified
       //structuring the window and its properties
       window.getContentPane().add(label,BorderLayout.SOUTH); // south side 
       window.setResizable(true);
       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       window.getContentPane().add(panel);
       window.setBounds(600,600,600,600);

       window.pack();
       window.setVisible(true);
       
       
   }
   //add buttons onto the grid with empty text and images
   public void addButtons() {
       for(int i = 0; i < 9; i++) {
           buttons[i] = new JButton();
           buttons[i].setText(EMPTY);
           buttons[i].addActionListener(new buttonAction());
           panel.add(buttons[i]);
       }
   }
   //clear everything in the panel and reset the state of game
   public void clearBoard() {
       for(int i = 0; i < 9; i++) {
           buttons[i].setText(EMPTY);
           buttons[i].setIcon(new ImageIcon(emptyImg));
       }
       for (int i = 0; i < 3; i++) {
         for (int j = 0; j < 3; j++) {
            board[i][j] = EMPTY;
         }
      }
       winner = EMPTY;
       numFreeSquares = 9;
       player = PLAYER_X;
       label.setText("Start the game. "+player+" goes first. \n X wins = " +Xwins+" O wins = "+Owins+" Games Played = "+gamesplayed);
   }
   //event listener for the button, assigns slot and checks for win or tie
   public class buttonAction implements ActionListener {
       
       public void actionPerformed(ActionEvent e) {
           while(winner == EMPTY) {
               JButton selected = (JButton) e.getSource(); //get specific button
               int i = Arrays.asList(buttons).indexOf(selected);
               //turning 1D array of buttons onto the 2D array of board map
               //so it can reuse board logic from previous lab
               int row = (int) Math.floor(i/3); 
               int col = i%3;
               
               String output = "";
               //set slot to specific player and set its image
               if(selected.getText() == EMPTY) {
                   selected.setText(player);
                   board[row][col] = player;
                   if(player == PLAYER_X) {
                       buttons[i].setIcon(new ImageIcon(xImg));
                   } else {
                       buttons[i].setIcon(new ImageIcon(oImg));
                   } 
                   numFreeSquares--;
               } else {
                   return;
               } 
               //check for a tie or a winner
               if(haveWinner(row, col)) {
                   winner = player;
                   if(winner == PLAYER_X) {
                       output += "X wins.";
                       Xwins+=1;
                       gamesplayed +=1;
                   }
                   else if(winner == PLAYER_O) {
                       output += "O wins.";
                       Owins +=1;
                       gamesplayed +=1;
                   }
               } else if(numFreeSquares == 0) {
                   winner = TIE;
                   output += "Tie."; 
                   gamesplayed +=1;
               }
               //alternate players, otherwise end the game 
               if(winner == EMPTY) {
                   // change to other player (this won't do anything if game has ended)
                   if (player==PLAYER_X) {
                        player=PLAYER_O;
                    } else { 
                        player=PLAYER_X;
                    } 
                   label.setText("Game in progress. "+player+"'s turn. \nX wins = " +Xwins+" O wins = "+Owins+" Games Played = "+gamesplayed);
               } else {
                   label.setText("Game Over! "+output + " \n X wins = " +Xwins+" O wins = "+Owins+" Games Played = "+gamesplayed);
                   return;
               }
               
           }    
       }
   }
   
   /**
    * Returns true if filling the given square gives us a winner, and false
    * otherwise.
    *
    * @param int row of square just set
    * @param int col of square just set
    * 
    * @return true if we have a winner, false otherwise
    */
   private boolean haveWinner(int row, int col) 
   {
       // unless at least 5 squares have been filled, we don't need to go any further
       // (the earliest we can have a winner is after player X's 3rd move).

       if (numFreeSquares>4) return false;

       // Note: We don't need to check all rows, columns, and diagonals, only those
       // that contain the latest filled square.  We know that we have a winner 
       // if all 3 squares are the same, as they can't all be blank (as the latest
       // filled square is one of them).

       // check row "row"
       if ( board[row][0].equals(board[row][1]) &&
            board[row][0].equals(board[row][2]) ) return true;
       
       // check column "col"
       if ( board[0][col].equals(board[1][col]) &&
            board[0][col].equals(board[2][col]) ) return true;

       // if row=col check one diagonal
       if (row==col)
          if ( board[0][0].equals(board[1][1]) &&
               board[0][0].equals(board[2][2]) ) return true;

       // if row=2-col check other diagonal
       if (row==2-col)
          if ( board[0][2].equals(board[1][1]) &&
               board[0][2].equals(board[2][0]) ) return true;

       // no winner yet
       return false;
   }
       public void playSound(String soundName)
     {
           try 
           {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));
            Clip clip = AudioSystem.getClip( );
            clip.open(audioInputStream);
            clip.start( );
           }
           catch(Exception ex)
           {
             System.out.println("Error with playing sound.");
             ex.printStackTrace( );
           }
     }
}