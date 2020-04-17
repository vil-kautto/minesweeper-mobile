package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

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

    private ServiceConnection connectService;
    private SoundPlayer soundService;
    private boolean soundBound = false;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SoundPlayer.class);
        bindService(intent, connectService, Context.BIND_AUTO_CREATE);
    }

    /**
     * Clicked handles button click events from main menu. Also opens a new activity upon clicking
     * @param v Clicked View
     */
    public void clicked(View v) {
        Intent intent;
        playSound(R.raw.click);
        switch(v.getId()) {
            case(R.id.playButton):
                System.out.println("play");
                intent = new Intent(this, LevelSelectionActivity.class);
                startActivity(intent);
                break;
            case(R.id.highScoreButton):
                System.out.println("High Scores");
                intent = new Intent(this, HighScoreActivity.class);
                startActivity(intent);
                break;
            case(R.id.settingsButton):
                System.out.println("Settings");
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * playSound calls to a soundplayer which plays a given sound
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
