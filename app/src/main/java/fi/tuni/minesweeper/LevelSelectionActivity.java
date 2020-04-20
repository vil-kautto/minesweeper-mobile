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
 * LevelSelection -activity contains difficulty selection and launches the game activity
 * @author      Ville Kautto <ville.kautto@hotmail.fi>
 * @version     2020.04.22
 * @since       2020.04.07
 */
public class LevelSelectionActivity extends AppCompatActivity {

    private ServiceConnection connectService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        connectService = new MyConnection();
    }

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
            case(R.id.easyButton):
                System.out.println("easy");
                intent = new Intent(this, Game.class);
                intent.putExtra("rows", 9);
                intent.putExtra("cols", 9);
                intent.putExtra("mines", 10);
                intent.putExtra("difficulty", "easy");
                startActivity(intent);
                break;
            case(R.id.mediumButton):
                System.out.println("medium");
                intent = new Intent(this, Game.class);
                intent.putExtra("rows", 16);
                intent.putExtra("cols", 16);
                intent.putExtra("mines", 40);
                intent.putExtra("difficulty", "medium");
                startActivity(intent);
                break;
            case(R.id.hardButton):
                System.out.println("hard");
                intent = new Intent(this, Game.class);
                intent.putExtra("rows", 20);
                intent.putExtra("cols", 20);
                intent.putExtra("mines", 99);
                intent.putExtra("difficulty", "hard");
                startActivity(intent);
                break;
            case(R.id.extremeButton):
                System.out.println("extreme");
                intent = new Intent(this, Game.class);
                intent.putExtra("rows", 20);
                intent.putExtra("cols", 20);
                intent.putExtra("mines", 120);
                intent.putExtra("difficulty", "extreme");
                startActivity(intent);
                break;
            case(R.id.customButton):
                System.out.println("custom");
                intent = new Intent(this, CustomGameActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * playSound creates calls to SoundPlayer which plays a given sound
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
