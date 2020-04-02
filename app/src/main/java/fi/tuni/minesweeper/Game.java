package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Minesweeper for android
 * @author      Ville Kautto <ville.kautto@hotmail.fi>
 * @version     2020.03.24
 */
public class Game extends AppCompatActivity {

    static Activity messenger;

    private int rows = 12;
    private int cols = 12;
    private int mines = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        messenger = this;
        newGame();
    }

    private final int RUNNING = 0;
    private final int WIN = 1;
    private final int LOSE = -1;
    private int gameState;

    private Cell[][] board;

    /**
     * NewGame generates a new game board and resets the stats of current game
     */
    public void newGame() {

        gameState = RUNNING;
        System.out.println("Starting a new game");
        board = generateBoard();
        setMines();
        setNumbers();
        setScene();
    }

    /**
     * same as above, but takes a View as a parameter
     * @param v ImageButton's view
     */
    public void newGame(View v) {
        System.out.println("This feature is still in progress, please wait for the next release");

        toaster("Resetting current game.");
        TableLayout tl = findViewById(R.id.gameBoard);
        tl.removeAllViews();

        gameState = RUNNING;
        System.out.println("Starting a new game");
        board = generateBoard();
        setScene();
        setMines();
    }


    private int minesFlagged = 0;

    /**
     * GenerateBoard generates the board and sets all the actions for different button inputs
     * @return after all is set returns the board
     */
    public Cell[][] generateBoard() {
        Cell[][] board = new Cell[cols][rows];
        for(int i = 0; i<rows; i++) {
            for(int j = 0; j<cols; j++) {
                Cell btn = new Cell(this);
                board[i][j] = btn;

                final Cell[][] tempBoard = board;
                final int currentRow = i;
                final int currentCol = j;

                // Prepare yourself mentally, this is going to be a long one
                // Regular click opens cells that are interactable
                board[i][j].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {

                        // check if current block is flagged
                        // if flagged the don't do anything
                        if(!tempBoard[currentRow][currentCol].isFlagged()) {
                            // open nearby blocks till we get mines
                            uncoverCell(currentRow, currentCol);

                            // Minecheck on clicked cell
                            if(tempBoard[currentRow][currentCol].hasMine()) {
                                //gameState = LOSE;
                                //gameResolve();
                            }

                            // Checking for win condition
                            if(gameWinCheck()) {
                                gameState = WIN;
                                gameResolve();
                            }
                        }
                    }
                });

                // LongClickListener is used for changing markers on the cells
                board[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {

                        // if clicked cell is enabled or flagged
                        if(tempBoard[currentRow][currentCol].isEnabled() ||
                                        tempBoard[currentRow][currentCol].isFlagged()) {

                            // for long clicks set:
                            // 1. empty cells to flagged
                            // 2. flagged to question mark
                            // 3. question mark to blank

                            // case 1. set blank block to flagged
                            if (!tempBoard[currentRow][currentCol].isFlagged() &&
                                    !tempBoard[currentRow][currentCol].isQuestionMarked()) {
                                tempBoard[currentRow][currentCol].setFlagged(true);
                                minesFlagged++;
                                updateMineCountDisplay();
                            }
                            // case 2. set flagged to question mark
                            else if (tempBoard[currentRow][currentCol].isFlagged()) {
                                tempBoard[currentRow][currentCol].setQuestionMarked(true);
                                minesFlagged--;
                                updateMineCountDisplay();
                            }
                            // case 3. change to blank square
                            else {
                                tempBoard[currentRow][currentCol].setClickable(true);
                                tempBoard[currentRow][currentCol].clearAllIcons();
                            }
                            updateMineCountDisplay(); // update mine display
                        }
                        return true;

                    }
                });
            }
        }

        System.out.println("Board generated");
        return board;
    }

    /**
     * Setmines sets mines randomly into the board
     */
    public void setMines() {
        System.out.println("Adding mines");
        int i = 0;
        while(i < mines) {
            Random rand = new Random();
            int mineRow = rand.nextInt(rows-1);
            int mineCol = rand.nextInt(cols-1);
            if(!board[mineRow][mineCol].hasMine()) {
                board[mineRow][mineCol].plantMine();
                i++;
            }
        }
        System.out.println("Mines Set");
    }

    /**
     * UncoverCell calls to the cells and reveals all related cells (wip)
     * @param row
     * @param col
     */
    private void uncoverCell(int row, int col) {
        // only open non-flagged and mineless cells
        if(!board[row][col].hasMine() || !board[row][col].isFlagged()) {
            // reveal clicked cell
            System.out.println("Revealed cell at: " + row + " " + col);
            board[row][col].setRevealed();

            // if clicked block have nearby mines then don't open further
            if (board[row][col].getMineCount() != 0 ) {
                return;
            }
        }
    }



    /**
     * Scene adds all generated buttons to the game board that is visible to the user
     */
    public void setScene() {
        System.out.println("Adding cells");
        TableLayout tl = findViewById(R.id.gameBoard);
        TableRow tr = new TableRow(this);
        for(int i=1;i<board.length-1;i++) {
            System.out.println(i);
            for(int j = 1;j<board[i].length-1;j++) {
                tr.addView(board[i][j]);
            }
            tl.addView(tr);
            tr = new TableRow(this);
        }
        System.out.println("Cells added");
    }

    /**
     * gameWincheck goes through the entire board and scans it for
     * cells that are mineless and not revealed
     * @return returns false, unless the player wins the game
     */
    private boolean gameWinCheck() {
        for(int i = 0;i<board.length;i++) {
            for(int j = 0; j<board[i].length;j++) {
                if(!board[i][j].hasMine() && !board[i][j].isRevealed()){
                    return false;
                }
            }
        }
        return true;
    }

    private void setNumbers() {
        for(int i = 0;i<board.length;i++) {
            for(int j = 0; j<board[i].length;j++) {
                board[i][j].setMineCount(mines);
            }
        }
    }

    /**
     * Updates mine count to the screen after flags are placed
     * mineCount = mines - flagsPlaced
     * Still work in progress, please wait for the next release
     */
    private void updateMineCountDisplay() {
        int minesDisplayed = mines - minesFlagged;
        TextView tv = findViewById(R.id.mineCounter);
        tv.setText("Mines: "+ minesDisplayed);
    }


    /**
     * RevealMines reveals all the mines on the board after the game has ended
     */
    public void revealMines() {
        for(int i = 0;i<board.length;i++) {
            for(int j = 0; j<board[i].length;j++) {
                if(board[i][j].hasMine() || !board[i][j].hasMine()) {
                    board[i][j].setRevealed();
                }
                board[i][j].setCellDisabled(true);
            }
        }
    }

    /**
     * gameResolve is called when the game is over
     * It displays a different message depending on
     */
    public void gameResolve() {
        revealMines();
        if(gameState == WIN) {
            toaster("Your winrar!");
        } else if(gameState == LOSE) {
            toaster("Your loose!");
        }

    }
    public void toaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
