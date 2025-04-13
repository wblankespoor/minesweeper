import javax.swing.*;   
import java.awt.*;
import java.awt.event.*; 
import java.util.*;

public class Board extends JFrame{
    private ArrayList<ArrayList<Square>> squares;
    private boolean inFlagMode;
    private boolean isFirstMove;
    private boolean gameOver;
    private JButton restartButton;
    private int numRows;
    private int numCols;
    private int numMines;
    
    public Board(int argNumRows, int argNumCols, int argNumMines) {
        super("Minesweeper");
        numRows = argNumRows;
        numCols = argNumCols;
        numMines = argNumMines;
        inFlagMode = false;
        isFirstMove = true;
        gameOver = false;
        setSize(750, 750);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Setup toggle for flagging
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(!isFirstMove) {
                    inFlagMode = true;
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                inFlagMode = false;
            }
        });
        
        //Necessary to make sure board is always focused for flagging toggle (Squares aren't focusable)
        setFocusable(true);

        //Restart button
        restartButton = new JButton();
        restartButton.setBounds(numCols * 45 + 10,  0, 100, 45);
        restartButton.setText("Restart");
        add(restartButton);
        restartButton.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e){restart();}  
        });

        //Setup squares
        setLayout(null);      
        squares = new ArrayList<ArrayList<Square>>();
        for(int r = 0; r < numRows; r++) {
            squares.add(new ArrayList<Square>());
            for(int c = 0; c < numCols; c++) {
                squares.get(r).add(new Square(this, r, c));
            }
        }

        setVisible(true);
    }

    public void restart() {  
        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numCols; c++) {
                remove(squares.get(r).get(c));
            }
        }   
        squares = new ArrayList<ArrayList<Square>>();
        for(int r = 0; r < numRows; r++) {
            squares.add(new ArrayList<Square>());
            for(int c = 0; c < numCols; c++) {
                squares.get(r).add(new Square(this, r, c));
                squares.get(r).get(c).update();
            }
        }
        isFirstMove = true;
        gameOver = false;
        requestFocus();
    }

    public void actionSquare(int r, int c) {  
        if(gameOver) {
            return;
        }     
        if(isFirstMove) {
            populateMines(r, c);
            populateNumMinesAdjacent();
            isFirstMove = false;
        }
        if(inFlagMode) {
            if(!squares.get(r).get(c).isRevealed) {
                squares.get(r).get(c).isFlagged = !squares.get(r).get(c).isFlagged;
                squares.get(r).get(c).update();
            }
        } else if(!squares.get(r).get(c).isFlagged) {
            squares.get(r).get(c).revealAndUpdate();
            if(squares.get(r).get(c).hasMine) {
                System.out.println("Game Over");
                revealAll();
                gameOver = true;
                return;
            }
            if(!squares.get(r).get(c).hasMine && squares.get(r).get(c).numMinesNearby == 0) {
                for(int i = -1; i < 2; i++) {
                    for(int j = -1; j < 2; j++) {
                        if((r+i) > -1 && (r+i) < numRows && (c+j) > -1 && (c+j) < numCols && squares.get(r + i).get(c + j).isRevealed == false) {
                            actionSquare(r + i, c + j);
                        }
                    }
                }
            }
        }
        checkVictory();
    }

    public void checkVictory() {
        boolean isUnrevealedSquare = false;
        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numCols; c++) {
                if(!squares.get(r).get(c).hasMine && !squares.get(r).get(c).isRevealed) {
                    isUnrevealedSquare = true;
                    break;
                }
            }
        }
        if(!isUnrevealedSquare) {
            for(int r = 0; r < numRows; r++) {
                for(int c = 0; c < numCols; c++) {
                    squares.get(r).get(c).gameWon();
                }
            }
            gameOver = true;
            System.out.println("Victory!");
        }
    }

    public void revealAll() {
        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numCols; c++) {
                squares.get(r).get(c).revealAndUpdate();
            }
        }
    }

    public void populateMines(int rowToAvoid, int colToAvoid) {
        int minesSoFar = 0;
        int r;
        int c;
        while(minesSoFar < numMines) {
            r = (int) (Math.random() * numRows);
            c = (int) (Math.random() * numCols);
            if((Math.abs(r - rowToAvoid) > 1 || Math.abs(c - colToAvoid) > 1) && squares.get(r).get(c).hasMine == false) {
                squares.get(r).get(c).hasMine = true;
                minesSoFar++;
                squares.get(r).get(c).update();
            }
        }
    }
 
    public void populateNumMinesAdjacent() {
        int mineCount;

        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numCols; c++) {
                mineCount = 0;
                if(!squares.get(r).get(c).hasMine) {
                    for(int i = -1; i < 2; i++) {
                        for(int j = -1; j < 2; j++) {
                            if((r+i) > -1 && (r+i) < numRows && (c+j) > -1 && (c+j) < numCols && squares.get(r + i).get(c + j).hasMine) {
                                mineCount++;
                            }
                        }
                    }
                    squares.get(r).get(c).numMinesNearby = mineCount;    
                }    
            }
        }
    }    
}