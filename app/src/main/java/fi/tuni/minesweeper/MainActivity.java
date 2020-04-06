package fi.tuni.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    AppCompatActivity messenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messenger = this;
        buttonAnimator();
    }

    // Assigns and starts button animation
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
     * Resets the title based on activity state, see titleClick method below
     */
    @Override
    protected void onResume() {
        super.onResume();
        TextView tv = findViewById(R.id.title);
        tv.setText("Minesweeper!");
    }

    /**
     * Adds funtionality to clicking the title, probably used later
     * @param v Clicked view
     */
    public void titleClick(View v) {
        TextView tv = findViewById(R.id.title);
        tv.setText("Hello there");
        toaster("The game was made by Ville Kautto");
    }

    public void toaster(String text) {
        Toast.makeText(messenger, text, Toast.LENGTH_LONG).show();
    }
}
