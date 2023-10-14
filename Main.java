import javax.swing.*;   
import java.awt.event.*; 

public class Main {
    public static void main(String[] args) {
        int numRows = 12;
        int numCols = 15;
        int numMines = (int) (0.18 * numRows * numCols);
        Board gameBoard = new Board(numRows, numCols, numMines);
    }
}