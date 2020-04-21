package fi.tuni.minesweeper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.room.Room;

/**
 * Settings activity contains all customizable settings
 * @author      Ville Kautto <ville.kautto@hotmail.fi>
 * @version     2020.04.22
 * @since       2020.03.24
 */
public class SettingsActivity extends AppCompatActivity {

    Activity messenger;
    private ServiceConnection connectService;
    SharedPreferences settings;
    private static final String SETTINGS = "UserSettings";

    /**
     * preparing under the hood running systems (database, settings, soundService)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        messenger = this;
        connectService = new SettingsActivity.MyConnection();
        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    // SoundPlayer connection related variables
    private SoundPlayer soundService;
    private boolean soundBound = false;
    private static ScoreDatabase scoreDatabase;

    private boolean soundStatus;
    private Switch soundSwitch;
    private boolean vibrationStatus;
    private Switch vibrationSwitch;


    /**
     * onStart binds the SoundPlayer upon application start and fetches settings and database
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SoundPlayer.class);
        bindService(intent, connectService, Context.BIND_AUTO_CREATE);

        scoreDatabase = Room.databaseBuilder(getApplicationContext(),
                ScoreDatabase.class, "scoredb")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        soundStatus = settings.getBoolean("sound", true);
        vibrationStatus = settings.getBoolean("vibration", true);

        soundSwitch = findViewById(R.id.soundsStatus);
        vibrationSwitch = findViewById(R.id.vibrationStatus);

        soundSwitch.setChecked(soundStatus);
        vibrationSwitch.setChecked(vibrationStatus);

    }

    SharedPreferences.Editor editor;


    /**
     * Settings are saved on exiting the activity
     */
    @Override
    protected void onPause() {
        editor = settings.edit();
        editor.putBoolean("sound", soundStatus);
        editor.putBoolean("vibration", vibrationStatus);
        editor.commit();

        System.out.println("sound:" + soundStatus + ", vibration:" + vibrationStatus);
        toaster("Settings saved Successfully");
        super.onPause();
    }

    /**
     * responds to user changing the setttings
     * After the user changes a setting, the state of given setting will be updated
     * the user will be notified about the setting change
     * @param v
     */
    public void clicked(View v) {
        switch(v.getId()) {
            case R.id.soundsStatus:
                soundStatus = !soundStatus;
                System.out.println("Soundstatus was changed to " + soundStatus);
                soundService.toggleSound(soundStatus);

                // Feedback after user has toggled
                if(soundStatus) {
                    toaster("Sounds enabled");
                }else if(!soundStatus) {
                    toaster("Sounds disabled");
                }

                playSound(R.raw.click);
                break;
            case R.id.vibrationStatus:
                vibrationStatus = !vibrationStatus;
                System.out.println("VibrationStatus was changed to " + vibrationStatus);
                playSound(R.raw.click);

                // Feedback after user has toggled
                if(vibrationStatus) {
                    toaster("Vibration enabled");
                }else if(!vibrationStatus) {
                    toaster("Vibration disabled");
                }
                break;
        }
    }

    private boolean failsafeDisabled = false;
    private int failsafeCount = 5;

    public void deleteScoreData(View v) {
        if(failsafeCount == 0) {
            failsafeDisabled = true;
        }
        if(failsafeDisabled) {
            SettingsActivity.scoreDatabase.scoreDao().deleteAll();
            System.out.println("Deleted All database entries");
            toaster("Deleted all High Scores");
        }else {
            toaster("Resets permanently all data. Tap " + failsafeCount + " times to confirm the deletion");
            failsafeCount--;
        }

    }

    /**
     * Just a method for easier toasting
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