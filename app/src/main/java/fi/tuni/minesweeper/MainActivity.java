package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Activity messenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messenger = this;
    }

    // handles button click events on main menu
    public void clicked(View v) {
        Intent intent;
        switch(v.getId()) {
            case(R.id.startButton):
                toaster("Play");
                intent = new Intent(this, Game.class);
                startActivity(intent);
                break;
            case(R.id.highScoreButton):
                toaster("High Scores");
                intent = new Intent(this, HighScore.class);
                startActivity(intent);
                break;
            case(R.id.settingsButton):
                toaster("Settings");
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
        }

    }
    public void toaster(String text) {
        Toast.makeText(messenger, text, Toast.LENGTH_LONG).show();
    }
}
