package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Activity messenger;

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

    // handles button click events on main menu. Also opens a new activity upon clicking
    public void clicked(View v) {
        Intent intent;
        switch(v.getId()) {
            case(R.id.playButton):
                System.out.println("play");
                intent = new Intent(this, Game.class);
                startActivity(intent);
                break;
            case(R.id.highScoreButton):
                System.out.println("High Scores");
                intent = new Intent(this, HighScore.class);
                startActivity(intent);
                break;
            case(R.id.settingsButton):
                System.out.println("Settings");
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
        }
    }

    // Changes title on based on activity state
    @Override
    protected void onResume() {
        super.onResume();
        TextView tv = findViewById(R.id.title);
        tv.setText("Minesweeper!");
    }

    // Handles title click
    public void titleClick(View v) {
        TextView tv = findViewById(R.id.title);
        tv.setText("New theme unlocked!");
        toaster("The game was made by Ville Kautto");
    }

    public void toaster(String text) {
        Toast.makeText(messenger, text, Toast.LENGTH_LONG).show();
    }
}
