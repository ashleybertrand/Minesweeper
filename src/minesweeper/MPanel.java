package minesweeper;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 * MPanel will display and hold the logic for the game.
 *
 * @author bertrand
 * @version 21 Jan 2015
 */

public class MPanel extends JPanel {

    //board dimensions
    private int size = 500;
    //change parameters in Minesweeper.java to change board size
    private int numRows = 10;
    private int numCols = 10;

    // back-end: board data
    private Box[][] board;
    private int totalMines;
    private int visitedSpots = 0;           //used to determine end of game
    private boolean sweptMine = false;      //no mines have been revealed
    private boolean gameIsOver = false; 

    //constructor
    public MPanel(int i, int j) {
        numRows = i;
        numCols = j;
        setPreferredSize(new Dimension(size, size));
        addMouseListener(new MMouseListener());

        //back-end:  initialize the board
        board = new Box[i][j];
        for (i = 0; i < board.length; i++) {
            for (j = 0; j < board[i].length; j++) {
                board[i][j] = new Box();
            }
        }
        //mines are placed when the new array is created
        placeMine();
    }

    //back-end to front-end
    //get data from board and draw the GUI
    public void paint(Graphics g) {
        //background
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, getWidth(), getHeight());

        //lines
        g.setColor(Color.WHITE);

        for(int i = 0; i <= numRows; i++) {
            for(int j = 0; j <= numCols; j++) {
                g.drawLine(0, size/numRows*i, size, size/numRows*i);   //horizontal lines
                g.drawLine(size/numCols*j, 0, size/numCols*j, size);   //vertical lines
            }
        }    

        //font properties
        Font f = new Font("Times", Font.PLAIN, 50);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();

        int a = fm.getAscent();
        int h = fm.getHeight();

        //reveal entire board when gameIsOver
        if(gameIsOver){
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    String curSquare;
                    if(board[i][j].isMine()){
                        curSquare = "*";
                    }else{
                        curSquare = String.valueOf(board[i][j].getNearbyMines());
                    }
                    int w = fm.stringWidth(curSquare);
                    //centering each value in a box
                    g.drawString(curSquare, (int)((size/numRows) * i + (.5*size/numRows) - w / 2), (int)((size/numCols) * j + (.5*size/numCols) + a - h / 2));
                }
            }
        }else{
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    String curSquare = board[i][j].getValue();
                    int w = fm.stringWidth(curSquare);
                    //centering each value in a box
                    g.drawString(curSquare, (int)((size/numRows) * i + (.5*size/numRows) - w / 2), (int)((size/numCols) * j + (.5*size/numCols) + a - h / 2));
                }
            }
        }
    }

    // INNER CLASS for a Mouse events:
    private class MMouseListener implements MouseListener {
        //left and right clicking
        private static final int BUTTON1 = 0;
        private static final int BUTTON2 = 0;

        @Override
        public void mousePressed(MouseEvent e) {
            //System.out.println("press");
        }

        public void mouseReleased(MouseEvent e) {
            //System.out.println("release");
        }

        public void mouseEntered(MouseEvent e) {
            //System.out.println("mouse entered");
        }

        public void mouseExited(MouseEvent e) {
            //System.out.println("mouse exited");
        }

        //front-end to back-end
        public void mouseClicked(MouseEvent e) {
            //get click data from the GUI and convert to back end board spot reference
            int x = e.getX() / 50;
            int y = e.getY() / 50;

            if(e.getButton() == 1){
                //process click
                if (" ".equals(board[x][y].getValue())) {
                    repaint();
                    sweepMine(x,y);
                    checkForGameEnd();
                }
                //flagging    
            }else if(e.getButton() == 2){
                System.err.println("flagging");
                repaint();
                board[x][y].flag();
                repaint();
            }
        }
    }

    //back-end
    public boolean onBoard(int i, int j){
        if(i >=0 && i < numRows){
            if(j >=0 && j < numCols){
                //System.err.println(i +", "+ j + " is on the board");
                return true;
            }
        }
        return false;
    }

    //back-end
    public void placeMine() {
        //totalMines will be a random number between 5 and number of boxes -1
        //for an easier game, reduce this number
        //for a more challenging game, increase this number
        for (totalMines=0; totalMines <= (Math.random()*(numRows*numCols - 1) + 1); totalMines++) {
            int i = (int) (Math.random()*(board.length));                     
            int j = (int) (Math.random()*(board[i].length));
            //increment surrounding boxes
            if(board[i][j].isMine() == false){
                board[i][j].setMine(true);
                for(int row= i-1; row < i+2; row++){
                    for(int col= j-1; col < j+2; col++){
                        if(onBoard(row, col)){      
                            board[row][col].incrementMine();
                        }
                    }
                }
            }
        }
    }

    //back-end
    public void sweepMine(int x, int y){
        board[x][y].setVisited(true);
        visitedSpots++;
        //implementation of flooding
        //for each surrounding box
        if(board[x][y].getNearbyMines() == 0){
            for(int row= x-1; row < x+2; row++){
                for(int col= y-1; col < y+2; col++){
                    //if coordinates are on the board
                    if(onBoard(row,col)){
                        //if the box is a mine, dont reveal it
                        if(board[row][col].isMine()){
                            //do nothing
                            //if the box has a proximity of 0, reveal the other boxes around it
                        }else if(board[row][col].getNearbyMines() == 0 && !board[row][col].getVisited()){
                            sweepMine(row,col);
                            //sets and increments visited spots 
                        }else if(!board[row][col].getVisited()){
                            board[row][col].setVisited(true);
                            visitedSpots++;
                        }
                    }
                }
            }
        }
        //a mine is clicked
        if(board[x][y].isMine()){
            sweptMine = true;
        }
    }

    //back-end
    private boolean checkForWin() {
        boolean win = false;
        //all non-mine spots have been guessed
        if(visitedSpots == numRows*numCols-totalMines) {
            win = true;
        } 
        return win;
    }

    //back-end
    public void checkForGameEnd() {
        //for a loss
        if(sweptMine == true){
            JOptionPane.showMessageDialog(this, "Oops! That was a mine! You lose!");
            gameIsOver = true;
            repaint();
            //for a win
        } else if (checkForWin()) {
            JOptionPane.showMessageDialog(this, "Contrats, you beat Ashley's challenge!");
            gameIsOver= true;
            repaint();
            //keep playing otherwise    
        } else {

        }
    } 
}