package com.bmniman.littleguitarians.GetterSetter;

/**
 * Created by bmnim on 20-Feb-18.
 */

public class BoomButtons
{
    String sound;
    String playMode;
    boolean reverse;

    public BoomButtons() {
    }

    public BoomButtons(String sound, String playMode) {
        this.sound = sound;
        this.playMode = playMode;
    }

    public String getSound() {
        return sound;
    }

    public String getPlayMode() {
        return playMode;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setPlayMode(String playMode) {
        this.playMode = playMode;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
}
