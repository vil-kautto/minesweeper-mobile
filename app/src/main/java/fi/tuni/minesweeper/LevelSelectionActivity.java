package fi.tuni.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * LevelSelection -activity contains difficulty selection and launches the game activity
 * @author      Ville Kautto <ville.kautto@hotmail.fi>
 * @version     2020.04.07
 * @since       2020.04.07
 */
public class LevelSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);
    }

    /**
     * Clicked handles button click events from main menu. Also opens a new activity upon clicking
     * @param v Clicked View
     */
    public void clicked(View v) {
        Intent intent;
        switch(v.getId()) {
            case(R.id.easyButton):
                System.out.println("easy");
                intent = new Intent(this, Game.class);
                intent.putExtra("rows", 9);
                intent.putExtra("cols", 9);
                intent.putExtra("mines", 10);
                startActivity(intent);
                break;
            case(R.id.mediumButton):
                System.out.println("medium");
                intent = new Intent(this, Game.class);
                intent.putExtra("rows", 16);
                intent.putExtra("cols", 16);
                intent.putExtra("mines", 40);
                startActivity(intent);
                break;
            case(R.id.hardButton):
                System.out.println("hard");
                intent = new Intent(this, Game.class);
                intent.putExtra("rows", 20);
                intent.putExtra("cols", 20);
                intent.putExtra("mines", 99);
                startActivity(intent);
                break;
            case(R.id.extremeButton):
                System.out.println("extreme");
                intent = new Intent(this, Game.class);
                intent.putExtra("rows", 20);
                intent.putExtra("cols", 20);
                intent.putExtra("mines", 120);
                startActivity(intent);
                break;
            case(R.id.customButton):
                System.out.println("custom");
                intent = new Intent(this, Game.class);
                intent.putExtra("rows", 30);
                intent.putExtra("cols", 30);
                intent.putExtra("mines", 150);
                startActivity(intent);
                break;
        }
    }
}
