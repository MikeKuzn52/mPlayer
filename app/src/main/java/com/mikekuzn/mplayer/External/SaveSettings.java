package com.mikekuzn.mplayer.External;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveSettings {

    private SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private static final String NumSongKey = "NumSong";
    private static final String NumSongHashKey = "NumSongHash";
    private static final String PlayKey = "Play";
    private static final String CurrentPosKey = "CurrentPos";

    public SaveSettings(Context context) {
        sharedPrefs = context.getSharedPreferences("mPlayer", Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
    }

    public void saveNumSong(int num, int hash) {
        editor.putInt(NumSongKey, num);
        editor.putInt(NumSongHashKey, hash);
        editor.apply();
    }

    public void savePlay(boolean play) {
        editor.putBoolean(PlayKey, play);
        editor.apply();
    }

    public void saveCurrentPos(int currentPos) {
        editor.putInt(CurrentPosKey, currentPos);
        editor.apply();
    }

    public int[] loadNumSong() {
        return new int[] {
                sharedPrefs.getInt(NumSongKey, -1),
                sharedPrefs.getInt(NumSongHashKey, 0)
        };
    }

    public boolean loadPlay() {
        return sharedPrefs.getBoolean(PlayKey, false);
    }
    public int loadCurrentPos() {
        return sharedPrefs.getInt(CurrentPosKey, 0);
    }
}
