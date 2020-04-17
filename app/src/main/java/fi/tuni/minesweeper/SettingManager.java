package fi.tuni.minesweeper;

public class SettingManager {

    private double volume = 1;
    private boolean audioState = true;

    public void setVolume(double volume) {
        this.volume = volume;
        System.out.println("New volume set to: " + volume);
    }

    public double getVolume() {
        return volume;
    }

    public boolean getAudioState() {
        return audioState;
    }

    public void setAudioState() {
        this.audioState = !this.audioState;
    }
}
