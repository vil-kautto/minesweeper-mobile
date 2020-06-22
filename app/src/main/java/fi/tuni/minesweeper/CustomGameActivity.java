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
import android.widget.EditText;
import android.widget.Toast;

/**
 * CustomGameActivity uses user given variables for custom game generation
 *  @author Ville Kautto <ville.kautto@hotmail.fi>
 *  @version 2020.04.22
 *  @since 2020.04.07
 */
public class CustomGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game);
        connectService = new MyConnection();
    }

    // Sound player variables
    private ServiceConnection connectService;
    private SoundPlayer soundService;
    private boolean soundBound = false;

    /**
     * onStart prepares soundPlayer
     */
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

        System.out.println("Start Custom game");
        intent = new Intent(this, Game.class);
        try {
            // Gathering data from textboxes
            EditText boardSizeText = findViewById(R.id.boardSize);
            EditText mineAmountText = findViewById(R.id.mineAmount);

            // Converting to integers
            int boardSize = Integer.parseInt(boardSizeText.getText().toString());
            if(boardSize < 3) {
                toaster("The board size should be higher than 2");
                return;
            } else if (boardSize > 30) {
                toaster("The board size should be lower than 30");
                return;
            }

            int mineAmount = Integer.parseInt(mineAmountText.getText().toString());
            if(mineAmount < 1) {
                toaster("The amount of mines should be higher than 0");
                return;
            } else if (mineAmount > 360) {
                toaster("The amount of mines should be lower than 360");
                return;
            }

            float boardMineRatio = mineAmount / (boardSize*boardSize);
            System.out.println(boardMineRatio);
            if(boardMineRatio < 0.4) {
                intent.putExtra("rows", boardSize);
                intent.putExtra("cols", boardSize);
                intent.putExtra("mines", mineAmount);
                intent.putExtra("difficulty", "custom");
                startActivity(intent);
            } else {
                toaster("the ratio of mines should be lesser than 40%!");
                return;
            }

        } catch (NumberFormatException e) {
            toaster("All fields must be filled before starting a custom game");
            e.printStackTrace();
        }
    }

    /**
     * Toaster creates toasts and displays them visually to user
     * @param message Displayed message
     */
    public void toaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

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
