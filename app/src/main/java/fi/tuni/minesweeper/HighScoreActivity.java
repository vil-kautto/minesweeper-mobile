package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import java.util.List;

/**
 * HighScore activity keeps track of user's completed games
 * @author      Ville Kautto <ville.kautto@hotmail.fi>
 * @version     2020.03.24
 * @since       2020.03.24
 */
public class HighScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        connectService = new MyConnection();
    }

    private static ScoreDatabase scoreDatabase;
    private ServiceConnection connectService;
    private SoundPlayer soundService;
    private boolean soundBound = false;

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
    }

    int color = Color.rgb(150, 150, 150);
    /**
     * Clicked handles button click events from main menu. Also opens a new activity upon clicking
     * @param v Clicked View
     */
    public void clicked(View v) {
        Button button;
        playSound(R.raw.click);
        switch(v.getId()) {
            case(R.id.buttonEasy):
                button = findViewById(R.id.buttonEasy);
                System.out.println("easy");
                loadData("easy");
                break;
            case(R.id.buttonMedium):
                button = findViewById(R.id.buttonMedium);
                System.out.println("medium");
                loadData("medium");
                break;
            case(R.id.buttonHard):
                button = findViewById(R.id.buttonHard);
                System.out.println("hard");
                loadData("hard");
                break;
            case(R.id.buttonExtreme):
                button = findViewById(R.id.buttonExtreme);
                System.out.println("extreme");
                loadData("extreme");
                break;
        }
    }

    public void addScore (View v) {
        for(int i = 10; i > 0; i--) {
            Score score = new Score(i*10, "medium");
            HighScoreActivity.scoreDatabase.scoreDao().addScore(score);
        }
        loadData("medium");
    }

    private void loadData(String difficulty) {
        int i = 1;
        List<Score> scoreData;
        if(difficulty.equals("easy")) {
            scoreData = scoreDatabase.scoreDao().getEasyScores();
        }else if(difficulty.equals("medium")) {
            scoreData = scoreDatabase.scoreDao().getMediumScores();
        }else if(difficulty.equals("hard")) {
            scoreData = scoreDatabase.scoreDao().getHardScores();
        }else if(difficulty.equals("extreme")) {
            scoreData = scoreDatabase.scoreDao().getExtremeScores();
        } else {
            System.out.println("Something went wrong");
            scoreData = scoreDatabase.scoreDao().getAllScores();
        }
        for(Score score : scoreData) {
            System.out.println(String.format("%d: %s %d", i, score.getDate(), score.getScore()));
            i++;
        }
    }

    public void deleteAll(View v) {
        HighScoreActivity.scoreDatabase.scoreDao().deleteAll();
        System.out.println("Deleted All database entries");
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
