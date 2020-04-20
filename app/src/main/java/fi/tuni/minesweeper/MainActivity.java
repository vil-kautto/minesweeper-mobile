package fi.tuni.minesweeper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity launches other application activities
 * @author      Ville Kautto <ville.kautto@hotmail.fi>
 * @version     2020.04.22
 * @since       2020.03.24
 */
public class MainActivity extends AppCompatActivity {

    Activity messenger;
    private ServiceConnection connectService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messenger = this;
        buttonAnimator();
        connectService = new MyConnection();
    }

    // SoundPlayer connection related variables
    private SoundPlayer soundService;
    private boolean soundBound = false;

    /**
     * Binds the SoundPlayer upon application start
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SoundPlayer.class);
        bindService(intent, connectService, Context.BIND_AUTO_CREATE);
    }

    /**
     * Assings and starts the button animation on the main screen
     */
    public void buttonAnimator() {
        Button btn = findViewById(R.id.playButton);
        Animation buttonAnimation =
                AnimationUtils.loadAnimation(this, R.anim.button_bounce);
        btn.startAnimation(buttonAnimation);

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
     * Adds funtionality to clicking the title, probably used later
     * @param v Clicked view
     */
    public void titleClick(View v) {
        toaster("The game was made by Ville Kautto");
    }

    /**
     * Just a method for easier toaster
     * @param text
     */
    public void toaster(String text) {
        Toast.makeText(messenger, text, Toast.LENGTH_LONG).show();
    }

    private void playSound(int audioId) {
        if(soundBound) {
            soundService.playSound(audioId);
        }
    }

    /**
     * MyConnection maintains the connection between SoundPlayer and this activity
     */
    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // After bound to SoundPlayer, cast the IBinder and get SoundService instance
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
