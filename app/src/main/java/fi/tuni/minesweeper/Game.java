package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.room.Room;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Game -activity contains the main functionality for Minesweeper
 * @author      Ville Kautto <ville.kautto@hotmail.fi>
 * @version     2020.04.22
 * @since       2020.03.24
 */
public class Game extends AppCompatActivity {
    static Activity messenger;
    Vibrator v;

    private int rows;
    private int cols;
    private int mines;
    private String difficulty;

    /**
     * onCreate takes parameters from LevelSelectionActivity and CustomGameActivity
     * the parameters store game creation related data (rows, cols, mines and difficulty)
     * @param savedInstanceState stored parameters from another activities
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        messenger = this;
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        Intent intent = getIntent();
        rows = intent.getIntExtra("rows", 5);
        cols = intent.getIntExtra("cols", 5);
        mines = intent.getIntExtra("mines", 5);
        difficulty = intent.getStringExtra("difficulty");

        connectService = new MyConnection();
        newGame();
    }

    private ServiceConnection connectService;
    private SoundPlayer soundService;
    private boolean soundBound = false;
    private static ScoreDatabase scoreDatabase;

    SharedPreferences settings;
    private static final String SETTINGS = "UserSettings";
    private boolean vibrationStatus;

    /**
     * OnStart binds and starts the AudioService.
     * Also creates an instance of scoreDatabase for storing scores to High Scores
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SoundPlayer.class);
        bindService(intent, connectService, Context.BIND_AUTO_CREATE);

        scoreDatabase = Room.databaseBuilder(getApplicationContext(),
                ScoreDatabase.class, "scoredb")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        vibrationStatus = settings.getBoolean("vibration", true);

    }

    private final int RUNNING = 0;
    private final int WIN = 1;
    private final int LOSE = -1;
    private int gameState;

    private Cell[][] board;
    private int timer = 0;
    private boolean timerStarted = false;

    /**
     * NewGame generates a new game for the first game of the session
     */
    public void newGame() {
        gameState = RUNNING;
        resetStats();
        System.out.println("Starting a new game");
        board = generateBoard();
        setMines();
        setScene();
        TextView tv = findViewById(R.id.infoBox);
        tv.setText("Clear the field without triggering the mines");
    }

    /**
     * this newGame accepts calls from a button in-game screen
     * it resets the current progress and starts a new game upon activating
     * @param v ImageButton's view
     */
    public void newGame(View v) {
        System.out.println("This feature is still in progress, please wait for the next release");

        // resetting previous stats
        resetStats();

        TableLayout tl = findViewById(R.id.gameBoard);
        tl.removeAllViews();
        toaster("Resetting current game.");
        if (soundBound) {
            soundService.playSound(R.raw.newgame);
        }

        gameState = RUNNING;
        System.out.println("Starting a new game");
        board = generateBoard();
        setMines();
        setScene();
    }

    /**
     * Incidentally resetStats resets all the game related stats on starting a new game
     */
    private void resetStats() {
        vibrate();
        TextView infobox = findViewById(R.id.infoBox);
        infobox.setText("Clear the field without triggering the mines");
        stopTimer();

        minesFlagged = 0;

        timer = 0;
        timerStarted = false;
        TextView timer = findViewById(R.id.timer);
        timer.setText("Time: 0");
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
                // Regular click opens cells that are clickable
                board[i][j].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {

                        // check if current block is flagged
                        // if flagged the don't do anything
                        if(tempBoard[currentRow][currentCol].isClickable()) {
                            if (soundBound) {
                                SoundPlayer.playSound(R.raw.click);
                            }
                            uncoverCell(currentRow, currentCol);
                            if(!timerStarted) {
                                startTimer();
                                timerStarted = true;
                            }

                            // Mine check on clicked cell that is not flagged
                            if(tempBoard[currentRow][currentCol].hasMine()) {
                                gameState = LOSE;
                                tempBoard[currentRow][currentCol].triggerMine();
                                gameResolve();
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
                        if(tempBoard[currentRow][currentCol].isClickable() ||
                                tempBoard[currentRow][currentCol].isFlagged()) {

                            // for long clicks set:
                            // 1. empty cells to flagged
                            // 2. flagged to question mark
                            // 3. question mark to blank

                            // case 1. set blank block to flagged
                            if (!tempBoard[currentRow][currentCol].isFlagged() &&
                                    !tempBoard[currentRow][currentCol].isQuestionMarked()) {
                                if(minesFlagged < mines) {
                                    tempBoard[currentRow][currentCol].setFlagged(true);
                                    minesFlagged++;
                                    updateMineCountDisplay();
                                    vibrate();
                                }
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
     * SetMines sets mines to random locations in the board
     * This is not really efficient. Too bad.
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
        updateMineCountDisplay();
        System.out.println("Mines Set");
    }

    /**
     * UncoverCell calls to the cells and reveals all related cells (wip)
     * @param row
     * @param col
     */
    private void uncoverCell(int row, int col) {
        // only open non-flagged and mineless cells
        if(board[row][col].isClickable() && !board[row][col].isFlagged()) {
            // assign a number to clicked cell
            setNumber(row, col);

            // reveal clicked cell
            System.out.println("Revealed cell at: " + row + " " + col);
            board[row][col].setRevealed();
            if(board[row][col].getMineCount() == 0) {
                uncoverNearbyCells(row, col);
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
        for(int i=0;i<board.length;i++) {
            for(int j = 0;j<board[i].length;j++) {
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
                    System.out.println("found unrevealed cell at : " + i + ',' + j);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * SetNumbers assigns numbers on cells based on adjacent cells' mines
     * goes trough each mineless cell and adds a number for each nearby mine
     * @param currentRow Current row location on the board
     * @param currentCol Current column loaction on the board
     */
    private void setNumber(int currentRow, int currentCol) {
        //if the cell is not mine, assign a number
        int surroundingMines = 0;
        if(!board[currentRow][currentCol].hasMine()) {

            // rotating through each adjacent cell
            for(int nextRow = -1; nextRow <= 1; nextRow++) {
                for(int nextCol = -1; nextCol <= 1; nextCol++) {
                    if(insideBounds(currentRow+nextRow,currentCol+nextCol)) {
                        if(board[currentRow+nextRow][currentCol+nextCol].hasMine()) {
                            surroundingMines++;
                        }
                    }
                }
            }
            board[currentRow][currentCol].setMineCount(surroundingMines);
        } else {
            board[currentRow][currentCol].setMineCount(-1);
        }
    }

    /**
     * uncoverNearbyCells checks nearby cells and keeps revealing them until there are mines or flags
     * @param currentRow
     * @param currentCol
     */
    private void uncoverNearbyCells(int currentRow, int currentCol) {
        if(!board[currentRow][currentCol].hasMine()) {

            // rotating through each adjacent cell, checking the cells in a 3x3 area
            for(int nextRow = -1; nextRow <= 1; nextRow++) {
                for(int nextCol = -1; nextCol <= 1; nextCol++) {
                    if(insideBounds(currentRow+nextRow,currentCol+nextCol)) {
                        if(!board[currentRow+nextRow][currentCol+nextCol].hasMine() &&
                                !board[currentRow+nextRow][currentCol+nextCol].isRevealed()) {
                            // reveals mineless and unrevealed cells
                            uncoverCell(currentRow+nextRow, currentCol+nextCol);
                        }
                    }
                }
            }

        }
    }

    /**
     * InsideBounds checks if suggested row and column is inside the board
     * @param newRow Suggested row position
     * @param newCol Suggested column position
     * @return is the suggested position inside array bounds
     */
    private boolean insideBounds(int newRow, int newCol) {
        if(rows > newRow  && newRow >= 0) {
            if(cols > newCol && newCol  >= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Updates mine count to the screen after flags are placed
     * mineCount = mines - flagsPlaced
     */
    private void updateMineCountDisplay() {
        int minesDisplayed = mines - minesFlagged;
        TextView tv = findViewById(R.id.mineCounter);
        tv.setText("Mines: "+ minesDisplayed);
    }

    /**
     * RevealMines reveals all the mines on the board after the game has ended
     */
    private void revealMines() {
        for(int i = 0;i<board.length;i++) {
            for(int j = 0; j<board[i].length;j++) {
                if(board[i][j].hasMine()) {
                    board[i][j].setClickable(true);
                    board[i][j].setRevealed();
                }
                if(!board[i][j].isRevealed()) {
                    board[i][j].setCellDisabled(true);
                }
            }
        }
    }


    /**
     * playSound creates a local broadcast to audioManager which plays a given sound
     * Please note, that this is currently just a temporary solution, that will be changed soon
     * @param audioId
     */
    private void playSound(int audioId) {
        if(soundBound) {
            soundService.playSound(audioId);
        }
    }

    /**
     * vibrate checks the vibration status and vibrates the device accordingly
     */
    private void vibrate() {
        if(vibrationStatus) {
            v.vibrate(100);
        }
    }

    /**
     * gameResolve is called when the game is over
     * It displays a different message depending on gameState
     */
    private void gameResolve() {
        TextView tv = findViewById(R.id.infoBox);
        tv.setText("Restart by clicking the circular image on the top");

        vibrate();
        revealMines();
        stopTimer();
        if(gameState == WIN) {
            if (soundBound) {
                soundService.playSound(R.raw.gratz);
            }
            if(!difficulty.equals("custom")) {
                Game.scoreDatabase.scoreDao().addScore(new Score(timer, difficulty));
            }
            toaster("Well done! The field was cleared in " + timer + " seconds!");
        } else if(gameState == LOSE) {
            if (soundBound) {
                soundService.playSound(R.raw.explosion);
            }
            toaster("A mine blew up. Game Over.");
        } else {
            System.out.println("Why am I running");
        }
    }

    /**
     * StarTimer starts a timer service upon clicking the first cell
     * Also updates the tutorial text on the first click
     */
    private void startTimer() {
        TextView infobox = findViewById(R.id.infoBox);
        infobox.setText("The numbers describe how many mines are nearby");

        Intent intent = new Intent(this, Timer.class);
        startService(intent);
    }

    /**
     * StopTimer stops a timer service upon game completion
     */
    private void stopTimer() {
        Intent i = new Intent();
        i.setAction("STOP");

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    /**
     * BroadcastReceiver listens for messages from other services, currently used only for timer
     * Updates the UI's timer and counts up to 999
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // This is used when the player hits the mine on the first try
            if(gameState != RUNNING) {
                stopTimer();
            }
            TextView tv = (TextView) findViewById(R.id.timer);
            TextView infobox = findViewById(R.id.infoBox);
            timer = intent.getIntExtra("time", 0);
            tv.setText("Time: " + timer);
            // this method also updates the tutorial text in the easier difficulties
            if(timer >= 20 && timer < 40) {
                infobox.setText("Hold to place down flags that cannot be dug accidentally");
            } else if(timer >= 40 && timer < 60) {
                infobox.setText("The time is your score \n" +
                        "the faster you finish, the better the score");
            }
        }
    };

    /**
     * OnResume handles incoming requests from Timer
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(Game.this).registerReceiver(broadcastReceiver, new IntentFilter("TIMER"));

    }

    /**
     * Toaster creates toasts and displays them visually to user
     * @param message Displayed message
     */
    public void toaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * MyConnection maintains the connetion between SoundPlayer and this activity
     */
    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // Assigns a binder and the binder has an assigned SoundPlayer
            System.out.println("Fetching soundService from binder");
            MyBinder binder = (MyBinder) service;
            soundService = binder.getSoundPlayer();
            soundBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            soundBound = false;
        }
    }
}
