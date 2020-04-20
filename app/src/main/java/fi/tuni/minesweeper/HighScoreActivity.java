package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
        List<Score> scoreData = scoreDatabase.scoreDao().getMediumScores();
        loadData(scoreData);
    }

    int color = Color.rgb(150, 150, 150);
    /**
     * Clicked handles button click events from main menu. Also opens a new activity upon clicking
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

    public void addScore (View v) {
        for(int i = 10; i > 0; i--) {
            Score score = new Score(i*10, "medium", "Test");
            HighScoreActivity.scoreDatabase.scoreDao().addScore(score);
        }
        List<Score> scoreData = scoreDatabase.scoreDao().getMediumScores();
        loadData(scoreData);
    }



    private int grey;
    private int darkGrey;

    private void loadData(List<Score> scoreData) {
        TableLayout tl = findViewById(R.id.scoreScreen);
        tl.removeAllViews();

        int dps = 6;
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        int i = 1;

        if(scoreData.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("No high scores set yet, play to set them");
            tl.setBackgroundColor(Color.WHITE);
            tv.setTextSize(pixels);
            tv.setGravity(Gravity.CENTER);
            tl.addView(tv);
        } else {
            tl.setBackgroundColor(Color.BLACK);
            tl.setPadding(1,1,1,1);

            grey = Color.rgb(150,150,150);
            darkGrey = Color.rgb(120,120,120);

            TextView infotv = new TextView(this);
            TableRow inforow = new TableRow(this);

            String infodata = String.format("%-10s%-10s%-15s%-20s",
                    "#", "Time:", "Name:", "Date:");
            System.out.println(infodata);
            infotv.setText(infodata);
            infotv.setTextSize(pixels);
            infotv.setBackgroundColor(Color.WHITE);
            inforow.addView(infotv);
            tl.addView(inforow);

            for(Score score : scoreData) {
                TableRow tr = new TableRow(this);
                TextView tv = new TextView(this);

                String data = String.format("%-10s %-10s %-15s %-20s",
                        i + ":", score.getScore() + "  ", score.getName(),  score.getDate());
                System.out.println(data);
                tv.setText(data);
                tv.setTextSize(pixels);
                tv.setTextColor(Color.WHITE);
                tv.setPadding(2, 2, 2, 2);
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
