package minesweeper;

/**
 * Box class represents a box in the Minesweeper board.
 * 
 * @author bertrand
 * @version 21 Jan 2015
 */

public class Box {
    private boolean mine=false; //is this box a mine?    
    private int nearbyMines=0;  //number of nearbyMines
    private boolean flagged = false;
    private boolean visited;    //has the user guessed this box?
    private int visitedSpots;   //number of visitedSpots

    //implementation of flagging
    public void flag(){
        flagged = true;
    }

    public boolean getVisited() {
        return visited;
    }

    public void setVisited(boolean val) {
        visited = val;
    }

    public boolean isMine(){
        return mine;
    }

    //used to reveal the number of nearbyMines
    public void incrementMine() {
        nearbyMines++;
    }

    public int getNearbyMines(){
        return nearbyMines;
    }

    public String getValue(){
        if(visited){
            if(mine){
                return "*";     //represents a mine
            }else{
                return String.valueOf(nearbyMines);     //represents the number of nearbyMines
            }
        }else if(flagged){
            return "F";         //represents a flag the user sets in a Box they believe is a mine
        }else{
            return " ";
        }
    }

    public void setMine(boolean b) {
        // TODO Auto-generated method stub
        mine = b;
    }   
}