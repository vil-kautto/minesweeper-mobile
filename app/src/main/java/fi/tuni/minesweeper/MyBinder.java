package fi.tuni.minesweeper;

import android.os.Binder;

/**
 * Binds and retuns SoundPlayers to requesting ServiceConnectors
 * @since 2020.04.21
 * @version 2020.04.21
 * @author Ville Kautto
 */
public class MyBinder extends Binder {
    //the binded soundplayer object
    private SoundPlayer soundPlayer;

    /**
     * Gets a soundPlayer object upon creations
     * @param soundPlayer
     */
    public MyBinder(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    /**
     * returns a ServiceConnection a SoundPlayer object
     * @return
     */
    public SoundPlayer getSoundPlayer() {
        return this.soundPlayer;
    }
}
