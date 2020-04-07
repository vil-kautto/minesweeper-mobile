package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CustomGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game);
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

            if(boardSize * boardSize / mineAmount > 0.4) {
                intent.putExtra("rows", boardSize);
                intent.putExtra("cols", boardSize);
                intent.putExtra("mines", mineAmount);
                startActivity(intent);
            } else {
                toaster("the ratio of mines should be lesser than 40%!");
                return;
            }

            startActivity(intent);
        } catch (NumberFormatException e) {
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

    MediaPlayer mediaPlayer = null;
    /**
     * playSound creates a local broadcast to audioManager which plays a given sound
     * Please note, that this is currently just a temporary solution, that will be changed soon
     * @param audioId
     */
    private void playSound(int audioId) {
        mediaPlayer = MediaPlayer.create(this, audioId);
        try {
            mediaPlayer.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
