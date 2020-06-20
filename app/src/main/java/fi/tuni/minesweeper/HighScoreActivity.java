package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * HighScore activity keeps track of user's completed games
 * @author      Ville Kautto <ville.kautto@hotmail.fi>
 * @version     2020.04.22
 * @since       2020.03.24
 */
public class HighScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        connectService = new MyConnection();
    }
    // High score database related variables
    private static ScoreDatabase scoreDatabase;
    private ServiceConnection connectService;

    // Sound service Variables
    private SoundPlayer soundService;
    private boolean soundBound = false;


    /**
     * preparing under the hood running systems (database, settings, soundService)
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
        List<Score> scoreData = scoreDatabase.scoreDao().getMediumScores();
        loadData(scoreData);
    }
    /**
     * Loads data from High score database upon clicking buttons
     * @param v Clicked View
     */
    public void clicked(View v) {
        List<Score> scoreData;
        Button button;
        playSound(R.raw.click);
        switch(v.getId()) {
            case(R.id.buttonEasy):
                button = findViewById(R.id.buttonEasy);
                System.out.println("easy");
                scoreData = scoreDatabase.scoreDao().getEasyScores();
                loadData(scoreData);
                break;
            case(R.id.buttonMedium):
                button = findViewById(R.id.buttonMedium);
                System.out.println("medium");
                scoreData = scoreDatabase.scoreDao().getMediumScores();
                loadData(scoreData);
                break;
            case(R.id.buttonHard):
                button = findViewById(R.id.buttonHard);
                System.out.println("hard");
                scoreData = scoreDatabase.scoreDao().getHardScores();
                loadData(scoreData);
                break;
            case(R.id.buttonExtreme):
                button = findViewById(R.id.buttonExtreme);
                System.out.println("extreme");
                scoreData = scoreDatabase.scoreDao().getExtremeScores();
                loadData(scoreData);
                break;
        }
    }

    // color variables
    private int grey;
    private int darkGrey;

    /**
     * Displays high score information depending on gathered data
     * @param scoreData data gathered from database
     */
    private void loadData(List<Score> scoreData) {
        TableLayout tl = findViewById(R.id.scoreScreen);
        tl.removeAllViews();

        // converts pixels to dps
        int dps = 6;
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        grey = Color.rgb(150,150,150);
        darkGrey = Color.rgb(120,120,120);

        // Setting default text if database is empty
        if(scoreData.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("No high scores set yet, play to set them");
            tv.setTextSize(pixels);

            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.WHITE);
            tl.setBackgroundColor(grey);
            tl.addView(tv);
        } else {

            // Fetching and displaying top 10 high scores
            tl.setBackgroundColor(Color.BLACK);
            tl.setPadding(2, 2, 2, 2);

            // Example text for describing the scoring
            TextView infotv = new TextView(this);
            TableRow inforow = new TableRow(this);
            inforow.setBackgroundColor(Color.WHITE);
            String infodata = String.format("%-20s%-20s%-20s",
                    "#", "Time:", "Date:");
            System.out.println(infodata);
            infotv.setText(infodata);
            infotv.setTextSize(pixels);
            infotv.setBackgroundColor(Color.WHITE);
            inforow.addView(infotv);
            tl.addView(inforow);

            int i = 1;

            // Setting the actual High score data to the table
            for(Score score : scoreData) {
                TableRow tr = new TableRow(this);
                TextView tv = new TextView(this);

                String j = "" + i;
                if (i < 10) {
                     j = " " + i;
                }

                String data = String.format("%-20s %-20s %-20s",
                        j + ":", score.getScore(),  score.getDate());
                System.out.println(data);
                tv.setText(data);
                tv.setTextSize(pixels);
                tv.setTextColor(Color.WHITE);
                tv.setPadding(2, 2, 2, 2);
                // color coding for score items
                if(i % 2 == 0) {
                    tr.setBackgroundColor(grey);
                } else {
                    tr.setBackgroundColor(darkGrey);
                }
                tr.addView(tv);
                tl.addView(tr);
                i++;
            }
        }
    }

    public void addScore(View v) {
        for(int i =0;i<10; i++) {
            HighScoreActivity.scoreDatabase.scoreDao().addScore(new Score(i*10, "medium"));
            toaster("Added test data to high scores");
        }
    }

    /**
     * Toaster creates toasts and displays them visually to user
     * @param message Displayed message
     */
    public void toaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * playSound calls to a SoundPlayer which plays a given sound
     * @param audioId
     */
    private void playSound(int audioId) {
        if(soundBound) {
            soundService.playSound(audioId);
        }
    }

    /**
     * MyConnection maintains the connetion between SoundPlayer and this activity
     */
    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // After bound to LocalService, cast the IBinder and get SoundService instance
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
