package minesweeper;

import javax.swing.*;


/** 
 * Minesweeper front end
 * 
 * @author bertrand
 * @version 21 Jan 2015
 */

public class Minesweeper extends JFrame {
    //constructor
    public Minesweeper() {
        super("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //change these parameters to change the size of the square board
        getContentPane().add(new MPanel(10,10));
        pack();
        setLocationRelativeTo(null);    //put the GUI in the center of the screen
        setVisible(true);
    }
    
    public static void main(String[] args) {
        Minesweeper board = new Minesweeper(); 
    }
    
}
