package com.mikekuzn.mplayer.Domain;

import android.graphics.Bitmap;

public class LogicModel {
    public int currTime;
    public int duration;
    public boolean playing;
    public int numCurrentSong;
    public Bitmap currentSongBitmap;
    public String currentSongTitle;

    public interface Update {
        void execute(LogicModel info);
    }
}
