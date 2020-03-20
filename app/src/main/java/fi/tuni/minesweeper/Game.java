package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        startGame();
    }

    private final int DEFAULT = 0;
    private final int WIN = 1;
    private final int LOSE = -1;
    private int gameState;

    /**
     * StartGame is used for defining the state of the game
     */
    public void startGame() {

        gameState = WIN;
        newGame();
        while(gameState == DEFAULT) {

        }

        if(gameState == WIN) {
            System.out.println("win");
        }else if(gameState == LOSE) {
            System.out.println("lose");
        }else if(gameState == DEFAULT) {
            System.out.println("Something went wrong");
        }
    }

    private Button[][] board;

    /**
     * NewGame generates a new game board and resets the stats of current game
     */
    public void newGame() {
        System.out.println("Starting a new game");
        board = generateBoard(10,10);
        setButtons();
        setMines();
        addNumbers();

    }

    public Button[][] generateBoard(int rows, int cols) {
        Button[][] board = new Button[cols][rows];
        for(int i = 0; i<rows; i++) {
            for(int j = 0; j<cols; j++) {
                Button btn = new Button(this);
                btn.setText("3");
                board[i][j] = btn;
            }
        }
        System.out.println("Board generated");
        return board;
    }

    /**
     * SetButtons adds all generated buttons to the game board that is visible to the user
     */
    public void setButtons() {
        System.out.println("Adding buttons");
        TableLayout tl = findViewById(R.id.gameBoard);
        TableRow tr = new TableRow(this);
        for(int i=1;i<board.length-1;i++) {
            System.out.println(i);
            for(int j = 1;j<board[i].length-1;j++) {
                System.out.println(board[i].length);
                System.out.println(j);
                tr.addView(board[i][j]);
            }
            tl.addView(tr);
            tr = new TableRow(this);
        }
        System.out.println("Buttons added");

    }
}
