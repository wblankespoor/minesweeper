import javax.swing.*; 
import java.awt.event.*;
import java.util.*;
import java.awt.Color;

public class Square extends JButton {
    public boolean isRevealed;
    public boolean hasMine;
    public boolean isFlagged;
    public int numMinesNearby;
    public int row;
    public int col;

    public Square(Board gameBoard, int r, int c) {
        super();
        isRevealed = false;
        hasMine = false;
        isFlagged = false;
        row = r;
        col = c;
        setBounds(c * 45, r * 45, 45, 45);
        gameBoard.add(this);
        addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e){gameBoard.actionSquare(row, col);}  
        });
        setBackground(new Color(163, 225, 230));   
        setFocusable(false); 
    }

    public void revealAndUpdate() {
        isRevealed = true;
        update();
    }

    public void update() {
        setBackground(new Color(163, 225, 230));
        if(isFlagged) {
            setBackground(new Color(66, 88, 89));
        }
        if(isRevealed) {
            if(hasMine) {
                setBackground(new Color(232, 26, 26));
            } else {
                setBackground(new Color(189, 255, 128));
            }

            if(numMinesNearby > 0) {
                setText("" + numMinesNearby);
            }
        }
    }

    public void gameWon() {
        if(hasMine) {
            setBackground(new Color(100, 133, 69));
        }
    }
}