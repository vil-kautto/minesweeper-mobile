package fi.tuni.minesweeper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.IBinder;

public class SoundPlayer extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("Bind created");
        audioSetup();
        return myBinder;
    }

    private IBinder myBinder;
    @Override
    public void onCreate() {
        myBinder = new MyBinder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    // SoundPlayer variables, used in assigning SoundPlayers
    private AudioManager audioManager;
    private static SoundPool soundPool = null;
    private static boolean loaded = false;
    private static float volume = 1;

    // sound files
    private static int soundClick;
    private static int soundGameOver;
    private static int soundVictory;
    private static int soundRestart;

    //Setting manager
    private SharedPreferences settings;
    private final String SETTINGS = "UserSettings";
    private static boolean soundStatus;

    /**
     * Fetches settings and prepares the audio player
     */
    private void audioSetup() {
        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        soundStatus = settings.getBoolean("sound", true);
        System.out.println("Preparing audio manager");
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float currentVolumeIndex = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        this.volume = currentVolumeIndex / maxVolumeIndex;

        // For Android SDK >= 21
        if (Build.VERSION.SDK_INT >= 21 ) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(5);

            soundPool = builder.build();
        }
        // for Android SDK < 21
        else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            return;
        }

        // When Sound Pool load complete.
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        this.soundClick = this.soundPool.load(this, R.raw.click,1);
        this.soundGameOver = this.soundPool.load(this, R.raw.explosion,1);
        this.soundVictory = this.soundPool.load(this, R.raw.gratz,1);
        this.soundRestart = this.soundPool.load(this, R.raw.newgame,1);
        System.out.println("Audio Player ready");
    }

    /**
     * plays a given audio file if the file is loaded and the setting for sound is enabled
     * @param audioId
     */
    public static void playSound(int audioId) {
        if(loaded && soundStatus) {
            int streamId;
            if (audioId == R.raw.click) {
                streamId = soundPool.play(soundClick, volume, volume, 1, 0, 1f);
            } else if (audioId == R.raw.gratz) {
                streamId = soundPool.play(soundVictory, volume, volume, 1, 0, 1f);
            } else if (audioId == R.raw.explosion) {
                streamId = soundPool.play(soundGameOver, volume, volume, 1, 0, 1f);
            } else if (audioId == R.raw.newgame) {
                streamId = soundPool.play(soundRestart, volume, volume, 1, 0, 1f);
            }
        }
    }

    /**
     * Toggles the Setting for playing audio
     * @param status set status in settings
     */
    public void toggleSound(boolean status) {
        soundStatus = status;
    }

}
