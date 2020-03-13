package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
        switch(v.getId()) {
            case(R.id.startButton):
                toaster("Play");
                break;
            case(R.id.highScoreButton):
                toaster("High Scores");
                break;
            case(R.id.settingsButton):
                toaster("Settings");
                break;
        }

    }
    public void toaster(String text) {
        Toast.makeText(messenger, text, Toast.LENGTH_LONG).show();
    }
}
